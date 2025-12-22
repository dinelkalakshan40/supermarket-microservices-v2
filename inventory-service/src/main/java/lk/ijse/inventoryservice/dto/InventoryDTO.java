package lk.ijse.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InventoryDTO {
    private String id;   // for update & response
    private int stockId;
    private int productId;
    private int quantity;
    private String warehouse;
}
