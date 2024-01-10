package ie.atu.product.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
@Data
@EqualsAndHashCode(callSuper=false)
public class ProductServiceException extends RuntimeException{
    
    private String errorCode;

    public ProductServiceException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}
