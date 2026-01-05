package lk.ijse.inventoryservice.listener;

import lk.ijse.inventoryservice.config.RabbitMQConfig;
import lk.ijse.inventoryservice.config.RabbitMQConstants;
import lk.ijse.inventoryservice.event.InventoryReduceEvent;
import lk.ijse.inventoryservice.model.Inventory;
import lk.ijse.inventoryservice.repo.InventoryRepo;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class InventoryListener {
    @Autowired
    private InventoryRepo inventoryRepo;

    @RabbitListener(queues = RabbitMQConstants.QUEUE)
    public void reduceInventory(InventoryReduceEvent event){
        System.out.println("Received InventoryReduceEvent: " + event);
        Inventory inventory = inventoryRepo
                .findByProductId(event.getProductId())
                .orElseThrow(() -> new RuntimeException("Inventory not found"));
        System.out.println("Current Product: " + inventory.getQuantity());
        inventory.setQuantity(inventory.getQuantity() - event.getQuantity());

        inventoryRepo.save(inventory);
        System.out.println("Product after reduction: " + inventory.getQuantity());
    }
}
