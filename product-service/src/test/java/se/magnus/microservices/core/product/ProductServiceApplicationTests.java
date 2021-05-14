package se.magnus.microservices.core.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;
import se.magnus.microservices.api.product.Product;
import se.magnus.microservices.core.product.persistence.ProductRepository;
import se.magnus.microservices.core.product.services.ProductMapper;

@SpringBootTest
@AutoConfigureWebTestClient
class ProductServiceApplicationTests {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	WebTestClient client;

	@Autowired
	ProductMapper productMapper;

	private Product defaultProduct;

	@BeforeEach
	public void setup() {
		productRepository.deleteAll();

		defaultProduct = new Product(1, "name", 1, null);
		productRepository.save(productMapper.apiToEntity(defaultProduct));
	}

	@Test
	void contextLoads() {
	}

	@Test
	public void createProduct() {
		int productId = 2;
		Product newProduct = new Product(productId, "name", 1, null);
		client.post().uri("/product").body(Mono.just(newProduct), Product.class)
				.header("Content-Type", "application/json").accept(MediaType.APPLICATION_JSON).exchange().expectStatus()
				.isOk().expectBody().jsonPath("$.productId").isEqualTo(productId);

		assertEquals(2, productRepository.count());
		assertTrue(productRepository.findByProductId(productId).isPresent());
	}

	@Test
	public void deleteProduct() {
		assertEquals(1, productRepository.count());

		int productId = 1;
		client
		.delete().uri("/product/" + productId)
		.exchange()
		.expectStatus().isOk();

		assertEquals(0, productRepository.count());
	}

}
