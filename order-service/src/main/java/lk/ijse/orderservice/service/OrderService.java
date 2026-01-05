package lk.ijse.orderservice.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lk.ijse.orderservice.DTO.OrderDTO;
import lk.ijse.orderservice.DTO.OrderItemDTO;
import lk.ijse.orderservice.config.RabbitMQConstants;
import lk.ijse.orderservice.config.WebClientConfig;
import lk.ijse.orderservice.event.InventoryReduceEvent;
import lk.ijse.orderservice.model.OrderItem;
import lk.ijse.orderservice.model.Orders;
import lk.ijse.orderservice.repo.OrderRepo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private WebClient.Builder webClient;

    @CircuitBreaker(name = "customerService", fallbackMethod = "customerFallback")
    public Mono<OrderDTO> createOrder(OrderDTO orderDTO) {
        System.out.println("method called");
        Orders order = dtoToEntity(orderDTO);

        if (order.getDate() == null) {
            order.setDate(LocalDate.now());
        }
        WebClient client = webClient.build(); // use the injected builder once

        // 1️⃣ Validate Customer
        Mono<Void> customerCheck = client.get()
                .uri("http://CUSTOMER-SERVICE/customers/getCustomer/{id}", order.getCustomerId())
                .retrieve()
                .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        resp -> Mono.error(new RuntimeException("Customer service error"))
                )
                .toBodilessEntity()
                .timeout(Duration.ofSeconds(3))
                .then(); // Mono<Void>

        // 2️⃣ Validate all Products
        Mono<Void> productsCheck = Flux.fromIterable(order.getItems())
                .flatMap(item -> client.get()
                        .uri("http://PRODUCT-SERVICE/products/{id}", item.getProductId())
                        .retrieve()
                        .onStatus(
                                status -> status.is4xxClientError() || status.is5xxServerError(),
                                resp -> Mono.error(new RuntimeException("Product not found: " + item.getProductId()))
                        )
                        .bodyToMono(Void.class)
                )
                .then(); // Mono<Void> after all products validated

        //Combine Customer + Products validation, then save
        return Mono.zip(customerCheck, productsCheck)
                .then(orderRepo.save(order))          // Mono<Orders>
                .map(savedOrder -> {

                    // Send RabbitMQ events
                    savedOrder.getItems().forEach(item -> {
                        InventoryReduceEvent event =
                                new InventoryReduceEvent(item.getProductId(), item.getQuantity());

                        rabbitTemplate.convertAndSend(
                                RabbitMQConstants.EXCHANGE,
                                RabbitMQConstants.ROUTING_KEY,
                                event
                        );
                    });

                    // Entity → DTO (FIX)
                    return entityToDto(savedOrder);
                });
    }

    public Mono<OrderDTO> customerFallback(
            OrderDTO dto,
            Throwable ex
    ) {
        return Mono.error(
                new RuntimeException(
                        "Customer Service unavailable. Cannot place order."
                )
        );
    }

//    public List<OrderDTO> getAllOrders() {
//        return orderRepo.findAll()
//                .stream().map(this::entityToDto).collect(Collectors.toList());
//    }
//    public OrderDTO getOrderById(String orderId) {
//        Orders order = orderRepo.findByOrderId(orderId);
//        return entityToDto(order);
//    }
    // Update Order
//    public OrderDTO updateOrder(String orderId, OrderDTO orderDTO) {
//        Orders existing = orderRepo.findByOrderId(orderId);
//
//        if (existing != null) {
//            existing.setDate(orderDTO.getDate());
//            existing.setItems(
//                    orderDTO.getItems()
//                            .stream()
//                            .map(i -> new OrderItem(
//                                    i.getProductId(),
//                                    i.getQuantity(),
//                                    i.getPrice()
//                            ))
//                            .collect(Collectors.toList())
//            );
//            Orders updated = orderRepo.save(existing);
//            return entityToDto(updated);
//        }
//        return null;
//    }



    //mapping
    private Orders dtoToEntity(OrderDTO dto) {
        return new Orders(
                null,
                dto.getOrderId(),
                dto.getCustomerId(),
                dto.getDate(),
                dto.getItems()
                        .stream()
                        .map(i -> new OrderItem(
                                i.getProductId(),
                                i.getQuantity(),
                                i.getPrice()
                        ))
                        .collect(Collectors.toList())
        );
    }

    private OrderDTO entityToDto(Orders entity) {
        return new OrderDTO(
                entity.getOrderId(),
                entity.getCustomerId(),
                entity.getDate(),
                entity.getItems().stream().map(i-> new OrderItemDTO(
                        i.getProductId(),
                        i.getQuantity(),
                        i.getPrice()
                ))
                        .collect(Collectors.toList())
        );
    }
}
