package lk.ijse.orderservice.event;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InventoryReduceEvent {
    private String productId;
    private int quantity;
}
