package se.magnus.microservices.core.recommendation;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;
import se.magnus.microservices.api.recommendation.Recommendation;
import se.magnus.microservices.core.recommendation.persistence.RecommendationRepository;
import se.magnus.microservices.core.recommendation.services.RecommendationMapper;

@SpringBootTest
@AutoConfigureWebTestClient
class RecommendationServiceApplicationTests {

	@Autowired
	WebTestClient client;

	@Autowired
	RecommendationRepository repository;

	@Autowired
	RecommendationMapper mapper;

	Recommendation defaultRecommendation;

	@BeforeEach
	public void setup(){
		repository.deleteAll().block();
		defaultRecommendation = new Recommendation(1, 1, "author", 5, "content", null);
		repository.save(mapper.toEntity(defaultRecommendation)).block();

		assertEquals(1, repository.count().block());
	}

	@Test
	void contextLoads() {
	}

	@Test
	void getRecommendations(){
		int productId = defaultRecommendation.getProductId();
		client.get().uri("/recommendation?productId=" + productId)
		.accept(MediaType.APPLICATION_JSON)
		.exchange()
		.expectStatus().isOk()
		.expectBody()
		.jsonPath("$.length()").isEqualTo(1)
		.jsonPath("$[0].productId").isEqualTo(defaultRecommendation.getProductId())
		.jsonPath("$[0].recommendationId").isEqualTo(defaultRecommendation.getRecommendationId());
	}

	@Test
	public void createRecommendation(){
		int productId = 1;
		int recommendationId = 2;
		Recommendation recommendation = new Recommendation(productId, recommendationId, "author", 5, "content", null);
		client.post()
		.uri("/recommendation?productId=" + productId)
		.accept(MediaType.APPLICATION_JSON)
		.header("Content-Type", "application/json")
		.body(Mono.just(recommendation), Recommendation.class)
		.exchange()
		.expectStatus().isOk()
		.expectBody()
		.jsonPath("$.productId").isEqualTo(productId)
		.jsonPath("$.recommendationId").isEqualTo(recommendationId)
		.jsonPath("$.author").isEqualTo("author");

		assertEquals(2, repository.count().block());
	}

	@Test
	public void deleteRecommendations(){
		int productId = 1;
		client.delete().uri("/recommendation?productId=" + productId)
		.exchange()
		.expectStatus().isOk();

		assertEquals(0, repository.count().block());
	}

}
