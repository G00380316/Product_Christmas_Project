package ie.atu.product.db;

import org.springframework.data.jpa.repository.JpaRepository;

import ie.atu.product.model.Product;

public interface ProductRepo extends JpaRepository<Product,Long>{
    
}
