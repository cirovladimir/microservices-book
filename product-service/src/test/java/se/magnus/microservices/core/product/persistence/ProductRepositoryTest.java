package se.magnus.microservices.core.product.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.OptimisticLockingFailureException;

@DataMongoTest
public class ProductRepositoryTest {
    private final Logger log = LoggerFactory.getLogger(ProductRepositoryTest.class);

    @Autowired
    ProductRepository productRepository;

    ProductEntity savedEntity;

    @BeforeEach
    public void setup() {
        log.debug("deleting all product documents from db");
        productRepository.deleteAll().block();
        ProductEntity productEntity = new ProductEntity(1, "product-1", 1);
        savedEntity = productRepository.save(productEntity).block();
        assertEquals(productEntity, savedEntity);
        log.debug("saved default product entity on db: {}", savedEntity);
    }

    @Test
    public void create() {
        ProductEntity newEntity = new ProductEntity(2, "product-2", 2);
        productRepository.save(newEntity).block();

        ProductEntity foundEntity = productRepository.findById(newEntity.getId()).block();
        log.debug("new entity: {}, foundEntity: {}", newEntity, foundEntity);
        assertEquals(newEntity, foundEntity);

        assertEquals(2, productRepository.count().block());
    }

    @Test
    public void update() {
        savedEntity.setName("product-updated");
        productRepository.save(savedEntity).block();

        ProductEntity foundEntity = productRepository.findById(savedEntity.getId()).block();

        assertEquals("product-updated", foundEntity.getName());
    }

    @Test
    public void duplicateError() {
        ProductEntity duplicateProductEntity = new ProductEntity(savedEntity.getProductId(), "name", 1);
        assertThrows(DuplicateKeyException.class, () -> {
            productRepository.save(duplicateProductEntity).block();
        });
    }

    @Test
    public void optimisticLockError(){
        ProductEntity productEntity1 = productRepository.findById(savedEntity.getId()).block();
        ProductEntity productEntity2 = productRepository.findById(savedEntity.getId()).block();

        productEntity1.setName("name-update-1");
        productRepository.save(productEntity1).block();

        productEntity2.setName("name-update-2");
        assertThrows(OptimisticLockingFailureException.class, ()-> {
            productRepository.save(productEntity2).block();
        });

        ProductEntity foundProductEntity = productRepository.findById(savedEntity.getId()).block();
            assertEquals(1, foundProductEntity.getVersion());
            assertEquals("name-update-1", foundProductEntity.getName());
    }

}
