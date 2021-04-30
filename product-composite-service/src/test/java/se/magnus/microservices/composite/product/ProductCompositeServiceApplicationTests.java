package se.magnus.microservices.composite.product;

import static org.mockito.Mockito.when;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import se.magnus.microservices.api.product.Product;
import se.magnus.microservices.api.recommendation.Recommendation;
import se.magnus.microservices.api.review.Review;
import se.magnus.microservices.composite.product.services.ProductCompositeIntegration;

@SpringBootTest
@AutoConfigureWebTestClient
class ProductCompositeServiceApplicationTests {

	private static final int PRODUCTID_OK = 123;
	
	@Autowired
	WebTestClient client;

	@MockBean
	private ProductCompositeIntegration integration;

	@BeforeEach
	public void setup(){
		when(integration.getProduct(PRODUCTID_OK))
		.thenReturn(new Product(PRODUCTID_OK, "name", 123, "mock-serviceAddress"));
		when(integration.getReviews(PRODUCTID_OK))
		.thenReturn(Collections.singletonList(new Review(PRODUCTID_OK, 1, "author", "subject", "content", "mock-serviceAddress")));
		when(integration.getRecommendations(PRODUCTID_OK))
		.thenReturn(Collections.singletonList(new Recommendation(PRODUCTID_OK, 1, "author", 5, "content", "mock-serviceAddress")));
	}

	@Test
	void contextLoads() {
	}

	@Test
	public void getProductById(){
		client.get().uri("/product-composite/" + PRODUCTID_OK)
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().isOk()
		.expectHeader().contentType(MediaType.APPLICATION_JSON)
		.expectBody()
			.jsonPath("$.productId").isEqualTo(PRODUCTID_OK)
			.jsonPath("$.reviews.length()").isEqualTo(1)
			.jsonPath("$.recommendations.length()").isEqualTo(1)
		;
	}

}
