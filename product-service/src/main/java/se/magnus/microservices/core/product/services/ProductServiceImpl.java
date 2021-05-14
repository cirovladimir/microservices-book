package se.magnus.microservices.core.product.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import se.magnus.microservices.api.product.Product;
import se.magnus.microservices.api.product.ProductService;
import se.magnus.microservices.core.product.persistence.ProductEntity;
import se.magnus.microservices.core.product.persistence.ProductRepository;
import se.magnus.microservices.util.exceptions.InvalidInputException;
import se.magnus.microservices.util.exceptions.NotFoundException;
import se.magnus.microservices.util.http.ServiceUtil;


@RestController
public class ProductServiceImpl implements ProductService {

    private final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ServiceUtil serviceUtil;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductServiceImpl(ServiceUtil serviceUtil, ProductRepository productRepository, ProductMapper productMapper) {
        this.serviceUtil = serviceUtil;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public Product getProduct(int productId) {
        if(productId<1){
            throw new InvalidInputException("Invalid productId: " + productId);
        }
        ProductEntity foundProductEntity = productRepository.findByProductId(productId).orElseThrow(()->new NotFoundException("No product found for productId: " + productId));
        Product response = productMapper.entityToApi(foundProductEntity);
        response.setServiceAddress(serviceUtil.getServiceAddress());
        return response;
    }

    @Override
    public Product createProduct(Product body) {
        ProductEntity productEntity = productMapper.apiToEntity(body);
        ProductEntity savedEntity = productRepository.save(productEntity);
        return productMapper.entityToApi(savedEntity);
    }

    @Override
    public void deleteProduct(int productId) {
        if(productId < 1) throw new InvalidInputException("Invalid productId: " + productId);

        productRepository.findByProductId(productId).ifPresent(e->productRepository.delete(e));
    }
    
}
