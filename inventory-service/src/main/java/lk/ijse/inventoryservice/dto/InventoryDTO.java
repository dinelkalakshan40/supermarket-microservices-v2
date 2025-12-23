package lk.ijse.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class InventoryDTO {
    private String id;   // for update & response
    private String stockId;
    private String productId;
    private int quantity;
    private String warehouse;
}
