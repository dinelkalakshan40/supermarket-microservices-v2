package lk.ijse.orderservice.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(collection = "orders")
public class Orders {
    @Id
    private String id;
    private String orderId;
    private String customerId;
    private LocalDate date;
    private List<OrderItem> items;

}
