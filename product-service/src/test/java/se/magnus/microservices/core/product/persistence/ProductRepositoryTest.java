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
        log.debug("new entity: {}, foundEntity: {}", newEntity, foundEntity);
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

    @Test
    public void optimisticLockError(){
        ProductEntity productEntity1 = productRepository.findById(savedEntity.getId()).get();
        ProductEntity productEntity2 = productRepository.findById(savedEntity.getId()).get();

        productEntity1.setName("name-update-1");
        productRepository.save(productEntity1);

        productEntity2.setName("name-update-2");
        assertThrows(OptimisticLockingFailureException.class, ()-> {
            productRepository.save(productEntity2);
        });

        ProductEntity foundProductEntity = productRepository.findById(savedEntity.getId()).get();
            assertEquals(1, foundProductEntity.getVersion());
            assertEquals("name-update-1", foundProductEntity.getName());
    }

}
