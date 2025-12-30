package lk.ijse.orderservice.controller;
import lk.ijse.orderservice.DTO.OrderDTO;
import lk.ijse.orderservice.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public Mono<OrderDTO> createOrder(@RequestBody OrderDTO orderDTO) {
        return orderService.createOrder(orderDTO);
    }

//    @GetMapping
//    public List<OrderDTO> getAllOrders() {
//        return orderService.getAllOrders();
//    }
//    @GetMapping("/{orderId}")
//    public OrderDTO getOrderById(@PathVariable String orderId) {
//        return orderService.getOrderById(orderId);
//    }
//    @PutMapping("/{orderId}")
//    public OrderDTO updateOrder(
//            @PathVariable String orderId,
//            @RequestBody OrderDTO orderDTO
//    ) {
//        return orderService.updateOrder(orderId, orderDTO);
//    }
}
