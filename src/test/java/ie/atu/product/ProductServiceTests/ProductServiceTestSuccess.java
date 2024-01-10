package ie.atu.product.ProductServiceTests;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ie.atu.product.db.ProductRepo;
import ie.atu.product.model.Product;
import ie.atu.product.payload.ProductRequest;
import ie.atu.product.payload.ProductResponse;
import ie.atu.product.service.ProductService;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTestSuccess {

    @Mock
    private ProductRepo productRepo;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    public void setup() {
        product = Product.builder()
                .productId(1L)
                .productName("Sample Product")
                .price(100L)
                .quantity(50L)
                .build();
    }

    // JUnit test for Add Product method
    @DisplayName("Add Product - New Product")
    @Test
    public void testGivenProductRequestAddProduct() {

        long expectedProductId = 1L;
        // given
        ProductRequest productRequest = new ProductRequest("Sample Product", 100L, 50L);

        given(productRepo.save(any(Product.class))).willReturn(product);

        // when
        long productId = productService.addProduct(productRequest);

        // then
        assertEquals(expectedProductId, productId, "Returned productId should match the expected value");
        verify(productRepo, times(1)).save(any(Product.class));
    }

    // JUnit test for getProductByID method
    @DisplayName("Get Product by ID")
    @Test
    public void testGetProductById() {
        // given
        given(productRepo.findById(1L)).willReturn(Optional.of(product));

        // when
        ProductResponse savedProduct = productService.getProductbyId(1L);

        // then
        assertThat(savedProduct).isNotNull();
    }

    // JUnit test for deleteProductByID method
    @DisplayName("Delete Product by ID")
    @Test
    public void testDeleteProductById() {
        // given
        long productId = 1L;

        // Mocking the existsById behavior
        when(productRepo.existsById(anyLong())).thenReturn(true);

        // when
        productService.deleteProductById(productId);

        // then
        verify(productRepo, times(1)).existsById(productId);
        verify(productRepo, times(1)).deleteById(productId);
    }

    // JUnit test for Reducing Quantity Product method
    @DisplayName("Reduce Quantity of Product by ID")
    @Test
    public void testReduceQuantity_SuccessfulReduction() {
        // given
        long productId = 1L;
        long existingQuantity = 10L;
        long quantityToReduce = 5L;

        Product existingProduct = new Product();
        existingProduct.setQuantity(existingQuantity);

        // Mocking the findById behavior
        when(productRepo.findById(anyLong())).thenReturn(Optional.of(existingProduct));

        // when
        productService.reduceQuantity(productId, quantityToReduce);

        // then
        verify(productRepo, times(1)).findById(productId);
        verify(productRepo, times(1)).save(existingProduct);

        assertEquals(existingQuantity - quantityToReduce, existingProduct.getQuantity());
    }
}
