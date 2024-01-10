package ie.atu.product.ProductControllerTests;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import ie.atu.product.controller.ProductController;
import ie.atu.product.exception.ProductServiceException;
import ie.atu.product.payload.ProductRequest;
import ie.atu.product.service.ProductService;

@WebMvcTest(ProductController.class)
public class ProductControllerTestFails {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Perform common setup steps, if any
    }

    @Test
    public void addProduct_shouldReturnBadRequest() throws Exception {
        ProductRequest productRequest = new ProductRequest(null, 100L, 50L);
        when(productService.addProduct(any(ProductRequest.class)))
                .thenThrow(new ProductServiceException("Error Code:", "PRODUCT_NOT_ADDED"));

        mockMvc.perform(post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isBadRequest());
    }
    
    @Test
    public void addProduct_shouldReturnisNotFound() throws Exception {
        ProductRequest productRequest = new ProductRequest("Product", 100L, 50L);
        when(productService.addProduct(any(ProductRequest.class)))
                .thenThrow(new ProductServiceException("Error Code:","PRODUCT_NOT_ADDED"));

        mockMvc.perform(post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getProductById_shouldReturnNotFound() throws Exception {
        long productId = 1L;
        when(productService.getProductbyId(anyLong())).thenReturn(null)
                .thenThrow(new ProductServiceException("Error Code:","PRODUCT_NOT_FOUND"));

        mockMvc.perform(get("/product/{id}", productId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteProductById_shouldReturnNotFound() throws Exception {
        long productId = 1L;
        doThrow(new ProductServiceException("Error Code:","PRODUCT_NOT_FOUND")).when(productService).deleteProductById(anyLong());

        mockMvc.perform(delete("/product/{id}", productId))
                .andExpect(status().isNotFound());
    }

        @Test
        public void reduceQuantity_shouldReturnBadRequest() throws Exception {
        long productId = 1L;
        long quantity = -5L; // Negative quantity is invalid

        mockMvc.perform(put("/product/reduceQuantity/{id}", productId)
                .param("quantity", String.valueOf(quantity)))
                .andExpect(status().isBadRequest());
        }
}
