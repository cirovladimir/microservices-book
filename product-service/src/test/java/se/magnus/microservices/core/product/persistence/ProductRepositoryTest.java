package se.magnus.microservices.core.product.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;

@DataMongoTest
public class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    ProductEntity savedEntity;

    @BeforeEach
    public void setup() {
        productRepository.deleteAll();
        ProductEntity productEntity = new ProductEntity(1, "product-1", 1);
        savedEntity = productRepository.save(productEntity);
        assertEquals(productEntity, savedEntity);
    }

    @Test
    public void create() {
        ProductEntity newEntity = new ProductEntity(2, "product-2", 2);
        productRepository.save(newEntity);

        ProductEntity foundEntity = productRepository.findById(newEntity.getId()).get();
        assertEquals(newEntity, foundEntity);

        assertEquals(2, productRepository.count());
    }

    @Test
    public void update() {
        savedEntity.setName("product-updated");
        productRepository.save(savedEntity);

        ProductEntity foundEntity = productRepository.findById(savedEntity.getId()).get();

        assertEquals("product-updated", foundEntity.getName());
    }

    @Test
    public void duplicateError() {
        ProductEntity duplicateProductEntity = new ProductEntity(savedEntity.getProductId(), "name", 1);
        assertThrows(DuplicateKeyException.class, () -> {
            productRepository.save(duplicateProductEntity);
        });
    }

}
