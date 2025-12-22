package lk.ijse.orderservice.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderItem {
    private String productId;
    private int quantity;
    private double price;
}
