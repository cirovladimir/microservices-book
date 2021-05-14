package se.magnus.microservices.core.review;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import reactor.core.publisher.Mono;
import se.magnus.microservices.api.review.Review;
import se.magnus.microservices.core.review.persistence.ReviewRepository;
import se.magnus.microservices.core.review.services.ReviewMapper;

@SpringBootTest
@AutoConfigureWebTestClient
class ReviewServiceApplicationTests {

	@Autowired
	WebTestClient client;

	@Autowired
	ReviewRepository repository;

	@Autowired
	ReviewMapper mapper;

	Review defaultReview;

	@BeforeEach
	void setup(){
		repository.deleteAll();

		defaultReview = new Review(1, 1, "author", "subject", "content", null);
		repository.save(mapper.toEntity(defaultReview));

		assertEquals(1, repository.count());
	}

	@Test
	void contextLoads() {
	}

	@Test
	void createReview(){
		int productId = 1;
		int reviewId = 2;
		Review newReview = new Review(productId, reviewId, "author", "subject", "content", null);
		client.post().uri("/review?productId=" + productId)
		.accept(MediaType.APPLICATION_JSON)
		.header("Content-Type", "application/json")
		.body(Mono.just(newReview), Review.class)
		.exchange()
		.expectStatus().isOk()
		.expectBody()
		.jsonPath("$.productId").isEqualTo(productId)
		.jsonPath("$.reviewId").isEqualTo(reviewId);

		assertEquals(2, repository.count());
	}

	@Test
	void deleteReview(){
		client.delete().uri("/review?productId=" + defaultReview.getProductId())
		.exchange()
		.expectStatus().isOk();

		assertEquals(0, repository.count());
	}

}
