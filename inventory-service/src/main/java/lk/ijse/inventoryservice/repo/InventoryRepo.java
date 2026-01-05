package lk.ijse.inventoryservice.repo;


import lk.ijse.inventoryservice.model.Inventory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface InventoryRepo extends MongoRepository<Inventory,String> {
    Optional<Inventory> findByProductId(String productId);
}
