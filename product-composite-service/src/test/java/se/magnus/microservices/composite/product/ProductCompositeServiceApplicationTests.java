package se.magnus.microservices.composite.product;

import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.magnus.microservices.api.composite.ProductAggregate;
import se.magnus.microservices.api.composite.RecommendationSummary;
import se.magnus.microservices.api.composite.ReviewSummary;
import se.magnus.microservices.api.product.Product;
import se.magnus.microservices.api.recommendation.Recommendation;
import se.magnus.microservices.api.review.Review;
import se.magnus.microservices.composite.product.services.ProductCompositeIntegration;
import se.magnus.microservices.util.exceptions.InvalidInputException;
import se.magnus.microservices.util.exceptions.NotFoundException;

@SpringBootTest
@AutoConfigureWebTestClient
class ProductCompositeServiceApplicationTests {

	private static final int PRODUCTID_OK = 1;
	private static final int PRODUCTID_INVALID = 2;
	private static final int PRODUCTID_NOT_FOUND = 3;
	
	@Autowired
	WebTestClient client;

	@MockBean
	private ProductCompositeIntegration integration;

	@BeforeEach
	public void setup(){
		when(integration.getProduct(PRODUCTID_OK))
		.thenReturn(Mono.just(new Product(PRODUCTID_OK, "name", 123, "mock-serviceAddress")));
		
		when(integration.getReviews(PRODUCTID_OK))
		.thenReturn(Flux.fromIterable(Collections.singletonList(new Review(PRODUCTID_OK, 1, "author", "subject", "content", "mock-serviceAddress"))));
		
		when(integration.getRecommendations(PRODUCTID_OK))
		.thenReturn(Flux.fromIterable(Collections.singletonList(new Recommendation(PRODUCTID_OK, 1, "author", 5, "content", "mock-serviceAddress"))));

		when(integration.getProduct(PRODUCTID_INVALID))
			.thenThrow(new InvalidInputException("INVALID:" + PRODUCTID_INVALID));
		
		when(integration.getProduct(PRODUCTID_NOT_FOUND))
			.thenThrow(new NotFoundException("NOT FOUND:" + PRODUCTID_INVALID));
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

	@Test
	public void productNotFound(){
		client.get().uri("/product-composite/" + PRODUCTID_NOT_FOUND)
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().isNotFound();
	}

	@Test
	void createProductComposite(){
		RecommendationSummary recommendation = new RecommendationSummary(PRODUCTID_OK, 1, "author", 5, "content");
		ReviewSummary review = new ReviewSummary(PRODUCTID_OK, 1, "author", "subject", "content");
		ProductAggregate productAggregate = new ProductAggregate(PRODUCTID_OK, "name", 1, Arrays.asList(recommendation), Arrays.asList(review), null);
		client.post().uri("/product-composite")
		.accept(MediaType.APPLICATION_JSON)
		.header("Content-Type", "application/json")
		.body(Mono.just(productAggregate), ProductAggregate.class)
		.exchange()
		.expectStatus().isOk();
	}

	@Test
	void deleteProductComposite(){
		client.delete().uri("/product-composite/" + PRODUCTID_OK)
		.exchange()
		.expectStatus().isOk();
	}
}
