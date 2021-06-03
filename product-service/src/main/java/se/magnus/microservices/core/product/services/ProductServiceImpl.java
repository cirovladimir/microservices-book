package se.magnus.microservices.core.product.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;
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

    public ProductServiceImpl(ServiceUtil serviceUtil, ProductRepository productRepository,
            ProductMapper productMapper) {
        this.serviceUtil = serviceUtil;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    @Override
    public Mono<Product> getProduct(int productId) {
        log.debug("A request to get the product with id '{}' has been received.", productId);
        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }
        return productRepository.findByProductId(productId)
                .switchIfEmpty(Mono.error(new NotFoundException("No product found for productId: " + productId))).log()
                .map(e -> productMapper.entityToApi(e)).map(e -> {
                    e.setServiceAddress(serviceUtil.getServiceAddress());
                    return e;
                });
    }

    @Override
    public Product createProduct(Product product) {
        log.debug("A request to create a product has been received: {} ", product);
        ProductEntity productEntity = productMapper.apiToEntity(product);
        return productRepository.save(productEntity).log()
                .onErrorMap(DuplicateKeyException.class,
                        e -> new InvalidInputException("Duplicate key, product id: " + product.getProductId(), e))
                .map(e -> productMapper.entityToApi(e)).toProcessor().block();
    }

    @Override
    public void deleteProduct(int productId) {
        log.debug("A request to delete the product with id '{}' has been received", productId);
        if (productId < 1)
            throw new InvalidInputException("Invalid productId: " + productId);

        productRepository.findByProductId(productId).log().map(e -> productRepository.delete(e)).flatMap(e->e).toProcessor().block();
    }

}
