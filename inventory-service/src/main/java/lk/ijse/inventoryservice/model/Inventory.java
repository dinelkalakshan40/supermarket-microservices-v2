package lk.ijse.inventoryservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "inventory")
public class Inventory {
    @Id
    private String id;
    private int stockId;
    private int productId;
    private int quantity;
    private String warehouse;
}
