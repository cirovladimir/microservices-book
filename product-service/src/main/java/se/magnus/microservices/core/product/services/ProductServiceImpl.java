package se.magnus.microservices.core.product.services;

import org.springframework.web.bind.annotation.RestController;

import se.magnus.microservices.api.product.Product;
import se.magnus.microservices.api.product.ProductService;
import se.magnus.microservices.util.exceptions.InvalidInputException;
import se.magnus.microservices.util.exceptions.NotFoundException;
import se.magnus.microservices.util.http.ServiceUtil;


@RestController
public class ProductServiceImpl implements ProductService {

    private final ServiceUtil serviceUtil;

    public ProductServiceImpl(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public Product getProduct(int productId) {
        if(productId<1){
            throw new InvalidInputException("Invalid productId: " + productId);
        }
        if(productId == 13){
            throw new NotFoundException("No product found for productId: " + productId);
        }
        return new Product(productId, "name-" + productId, 123, serviceUtil.getServiceAddress());
    }
    
}
