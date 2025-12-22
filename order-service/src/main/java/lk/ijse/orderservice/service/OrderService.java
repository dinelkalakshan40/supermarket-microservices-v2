package lk.ijse.orderservice.service;

import lk.ijse.orderservice.DTO.OrderDTO;
import lk.ijse.orderservice.DTO.OrderItemDTO;
import lk.ijse.orderservice.model.OrderItem;
import lk.ijse.orderservice.model.Orders;
import lk.ijse.orderservice.repo.OrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    @Autowired
    private OrderRepo orderRepo;

    //create order
    public OrderDTO createOrder(OrderDTO orderDTO) {
        Orders newOrder = dtoToEntity(orderDTO);

        if (newOrder.getDate()==null){
            newOrder.setDate(LocalDate.now());
        }
        Orders savedOrder = orderRepo.save(newOrder);
        return entityToDto(savedOrder);
    }
    public List<OrderDTO> getAllOrders() {
        return orderRepo.findAll()
                .stream().map(this::entityToDto).collect(Collectors.toList());
    }
    public OrderDTO getOrderById(String orderId) {
        Orders order = orderRepo.findByOrderId(orderId);
        return entityToDto(order);
    }
    // Update Order
    public OrderDTO updateOrder(String orderId, OrderDTO orderDTO) {
        Orders existing = orderRepo.findByOrderId(orderId);

        if (existing != null) {
            existing.setDate(orderDTO.getDate());
            existing.setItems(
                    orderDTO.getItems()
                            .stream()
                            .map(i -> new OrderItem(
                                    i.getProductId(),
                                    i.getQuantity(),
                                    i.getPrice()
                            ))
                            .collect(Collectors.toList())
            );
            Orders updated = orderRepo.save(existing);
            return entityToDto(updated);
        }
        return null;
    }



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
