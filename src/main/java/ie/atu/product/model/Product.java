package ie.atu.product.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder //creates instance of the class with optional parameters
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO) //Lets the database know to generate an ID automatically
    private long productId;

    @Column(name = "PRODUCT_NAME") //Maps the entity attribute and the database column
    private String productName;

    @Column(name = "PRICE")
    private long price;

    @Column(name = "QUANTITY")
    private long quantity;

}
