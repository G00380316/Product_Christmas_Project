package ie.atu.product.ProductRepoTests;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ie.atu.product.db.ProductRepo;
import ie.atu.product.exception.ProductServiceException;
import ie.atu.product.model.Product;
import ie.atu.product.service.*;

@SpringBootTest
public class ProductRepoTest {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private ProductService productService;

    @Test
    public void shouldSaveAndRetrieveProduct() {
        // Given
        Product product = new Product();
        product.setProductName("Test Product");
        product.setQuantity(10L);
        product.setPrice(20L);

        // When
        Product savedProduct = productRepo.save(product);

        // Then
        Optional<Product> retrievedProduct = productRepo.findById(savedProduct.getProductId());
        assertThat(retrievedProduct).isPresent();
        assertThat(retrievedProduct.get().getProductName()).isEqualTo("Test Product");
        assertThat(retrievedProduct.get().getQuantity()).isEqualTo(10L);
        assertThat(retrievedProduct.get().getPrice()).isEqualTo(20L);
    }

    @Test
    public void shouldThrowExceptionWhenProductIdNotFound() {
        // Given
        long nonExistentProductId = 999L;

        // When/Then
        assertThrows(ProductServiceException.class, () -> productRepo.findById(nonExistentProductId)
                .orElseThrow(() -> new ProductServiceException("Product not found", "PRODUCT_NOT_FOUND")));
    }

    @Test
    public void shouldReduceQuantity() {
        // Given
        Product product = new Product();
        product.setProductName("Test Product");
        product.setQuantity(10L);
        product.setPrice(20L);
        Product savedProduct = productRepo.save(product);

        // When
        productService.reduceQuantity(savedProduct.getProductId(), 5L);

        // Then
        Optional<Product> updatedProduct = productRepo.findById(savedProduct.getProductId());
        assertThat(updatedProduct).isPresent();
        assertThat(updatedProduct.get().getQuantity()).isEqualTo(5L);
    }

    @Test
    public void shouldDeleteProduct() {
        // Given
        Product product = new Product();
        product.setProductName("Test Product");
        product.setQuantity(10L);
        product.setPrice(20L);
        Product savedProduct = productRepo.save(product);

        // When
        productRepo.deleteById(savedProduct.getProductId());

        // Then
        Optional<Product> deletedProduct = productRepo.findById(savedProduct.getProductId());
        assertThat(deletedProduct).isEmpty();
    }
}
