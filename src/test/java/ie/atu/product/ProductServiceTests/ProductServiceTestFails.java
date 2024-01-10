package ie.atu.product.ProductServiceTests;

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
import org.springframework.dao.DataAccessException;

import ie.atu.product.db.ProductRepo;
import ie.atu.product.exception.ProductServiceException;
import ie.atu.product.model.Product;
import ie.atu.product.payload.ProductRequest;
import ie.atu.product.service.ProductService;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTestFails {

    @Mock
    private ProductRepo productRepo;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setup() {
        Product.builder()
                .productId(1L)
                .productName("Sample Product")
                .price(100L)
                .quantity(50L)
                .build();
    }

    @Test
    @DisplayName("Add Product - Failed Product Creation")
    public void testGivenProductRequestAddProduct_Failure() {
        // given
        ProductRequest productRequest = new ProductRequest("Sample Product", 100L, 50L);

        // Mocking the case where product creation fails (e.g., due to a database constraint violation)
        given(productRepo.save(any(Product.class))).willThrow(new DataAccessException("Simulated database error") {
        });

        // when and then
        assertThrows(ProductServiceException.class, () -> productService.addProduct(productRequest));

        // Verify that productRepo.save was called with the correct argument (any Product)
        verify(productRepo, times(1)).save(any(Product.class));
    }
    
    @Test
    @DisplayName("Get Product by ID - Product Does Not Exist")
    public void testGetProductById_ProductDoesNotExist() {
        // given
        given(productRepo.findById(1L)).willReturn(Optional.empty()); // Mocking the case where product doesn't exist

        // when
        assertThrows(ProductServiceException.class,
                () -> productService.getProductbyId(1L));

        // then
        verify(productRepo, times(1)).findById(1L); // Verify that findById is called with the expected ID
    }

    @Test
    @DisplayName("Delete Product by ID - Product Does Not Exist")
    public void testDeleteProductById_ProductDoesNotExist() {
        // given
        long productId = 2L;

        // Mocking the existsById behavior
        when(productRepo.existsById(anyLong())).thenReturn(false);

        // when
        assertThrows(ProductServiceException.class,
                () -> productService.deleteProductById(productId));

        // then
        verify(productRepo, never()).deleteById(productId); // Ensure deleteById is not called
    }

    //JUnit test for Reduce Quantity by ID method
    @Test
    @DisplayName("ReduceQuantitybyID_PRODUCT_NOT_FOUND")
    public void testReduceQuantity_ProductNotFound() {
        // given
        long productId = 2L;
        long quantityToReduce = 5L;

        // Mocking the findById behavior to return empty (product not found)
        when(productRepo.findById(anyLong())).thenReturn(Optional.empty());

        // when (this should throw an exception)
        assertThrows(ProductServiceException.class,
                () -> productService.reduceQuantity(productId, quantityToReduce));
    }

    @Test
    public void testReduceQuantity_InsufficientQuantity() {
        // given
        long productId = 3L;
        long existingQuantity = 2L;
        long quantityToReduce = 5L;

        Product existingProduct = new Product();
        existingProduct.setQuantity(existingQuantity);

        // Mocking the findById behavior
        when(productRepo.findById(anyLong())).thenReturn(Optional.of(existingProduct));

        // when (this should throw an exception)
        assertThrows(ProductServiceException.class,
                () -> productService.reduceQuantity(productId, quantityToReduce));
    }
}


