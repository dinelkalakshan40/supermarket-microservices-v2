package lk.ijse.orderservice.repo;

import lk.ijse.orderservice.model.Orders;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OrderRepo extends MongoRepository<Orders, String> {
    Orders findByOrderId(String orderId);
}
