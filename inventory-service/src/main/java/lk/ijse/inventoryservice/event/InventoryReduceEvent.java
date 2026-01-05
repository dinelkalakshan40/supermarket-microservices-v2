package lk.ijse.inventoryservice.event;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InventoryReduceEvent{
    private String stockId;
    private int quantity;
}
