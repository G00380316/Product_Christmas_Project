package ie.atu.product.service;

import static org.springframework.beans.BeanUtils.*;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import ie.atu.product.db.ProductRepo;
import ie.atu.product.exception.ProductServiceException;
import ie.atu.product.model.Product;
import ie.atu.product.payload.ProductRequest;
import ie.atu.product.payload.ProductResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductService {

    private final ProductRepo productRepo;

    public long addProduct(ProductRequest productRequest) {
        log.info("Adding Product");

        Product product = Product.builder()
                .productName(productRequest.getName())
                .quantity(productRequest.getQuantity())
                .price(productRequest.getPrice())
                .build();

        try {
            product = productRepo.save(product);

            log.info("Product Created");
            log.info("Product Id :" + product.getProductId());

            return product.getProductId();
        } catch (DataAccessException  e) {
            log.error("Error adding product", e);
            throw new ProductServiceException("Error Adding Product", "PRODUCT_NOT_ADDED");
        }
    }

    public ProductResponse getProductbyId(long productId) {
        log.info("Getting Product by ID");
        log.info("Getting Product with this ID: {}", productId);

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product with given ID not found", "PRODUCT_NOT_FOUND"));

        ProductResponse productResponse = new ProductResponse();
        copyProperties(product, productResponse);

        log.info("Product Response:" + productResponse.toString());

        return productResponse;
    }

    public void reduceQuantity(long productId, long quantity) {
        log.info("Reduce Quantity {} of ID: {}", quantity, productId);

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ProductServiceException("Product with ID doesn't exist", "PRODUCT_NOT_FOUND"));

        if (product.getQuantity() < quantity) {
            throw new ProductServiceException("Product does not have sufficient Quantity", "INSUFFICIENT_QUANTITY");
        }

        product.setQuantity(product.getQuantity() - quantity);
        productRepo.save(product);
        log.info("Product Quantity updated Successfully");
    }

    public void deleteProductById(long productId) {
        log.info("Product ID seletected for deletion: {}", productId);

        if (!productRepo.existsById(productId)) {
            log.info("Doesn't look ID exists still checking {}", !productRepo.existsById(productId));
            throw new ProductServiceException("Product with given ID" + productId + "not found:", "PRODUCT_NOT_FOUND");
        }

        log.info("Product with ID: {}" + productId + " is deleted");
        productRepo.deleteById(productId);
    }
}
