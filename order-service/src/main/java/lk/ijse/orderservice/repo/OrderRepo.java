package lk.ijse.orderservice.repo;

import lk.ijse.orderservice.model.Orders;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface OrderRepo extends ReactiveMongoRepository<Orders, String> {
    Mono<Orders> findByOrderId(String orderId);
}
