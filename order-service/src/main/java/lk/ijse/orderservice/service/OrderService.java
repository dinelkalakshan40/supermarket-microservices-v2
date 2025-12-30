package lk.ijse.orderservice.service;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lk.ijse.orderservice.DTO.OrderDTO;
import lk.ijse.orderservice.DTO.OrderItemDTO;
import lk.ijse.orderservice.config.WebClientConfig;
import lk.ijse.orderservice.model.OrderItem;
import lk.ijse.orderservice.model.Orders;
import lk.ijse.orderservice.repo.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private WebClient.Builder webClient;

    @CircuitBreaker(name = "customerService", fallbackMethod = "customerFallback")
    public Mono<OrderDTO> createOrder(OrderDTO orderDTO) {
        System.out.println("method called");
        Orders order = dtoToEntity(orderDTO);

        if (order.getDate() == null) {
            order.setDate(LocalDate.now());
        }

        String url =
                "http://CUSTOMER-SERVICE/customers/getCustomer/" +
                        order.getCustomerId();

        return webClient.build()
                .get()
                .uri(url)
                .retrieve()
                .toBodilessEntity() // Only check if customer exists
                .flatMap(response -> orderRepo.save(order) // Save order reactively
                        .map(this::entityToDto)
                );
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
