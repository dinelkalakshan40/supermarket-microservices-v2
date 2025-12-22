package lk.ijse.inventoryservice.repo;


import lk.ijse.inventoryservice.model.Inventory;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InventoryRepo extends MongoRepository<Inventory,String> {
}
