package se.magnus.microservices.composite.product.services;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import se.magnus.microservices.api.product.Product;
import se.magnus.microservices.api.product.ProductService;
import se.magnus.microservices.api.recommendation.Recommendation;
import se.magnus.microservices.api.recommendation.RecommendationService;
import se.magnus.microservices.api.review.Review;
import se.magnus.microservices.api.review.ReviewService;
import se.magnus.microservices.util.exceptions.InvalidInputException;
import se.magnus.microservices.util.exceptions.NotFoundException;

@Component
public class ProductCompositeIntegration implements ProductService, RecommendationService, ReviewService {
    private final Logger log = LoggerFactory.getLogger(ProductCompositeIntegration.class);

    private final ObjectMapper mapper;
    private final String productServiceUrl;
    private final String recommendationServiceUrl;
    private final String reviewServiceUrl;
    private final WebClient webClient;

    public ProductCompositeIntegration(RestTemplate restTemplate, ObjectMapper mapper,
            @Value("${app.product-service.host}") String productServiceHost,
            @Value("${app.product-service.port}") String productServicePort,
            @Value("${app.recommendation-service.host}") String recommendationServiceHost,
            @Value("${app.recommendation-service.port}") String recommendationServicePort,
            @Value("${app.review-service.host}") String reviewServiceHost,
            @Value("${app.review-service.port}") String reviewServicePort,
            WebClient.Builder webClientBuilder) {
        this.mapper = mapper;
        this.productServiceUrl = "http://" + productServiceHost + ":" + productServicePort + "/product/";
        this.recommendationServiceUrl = "http://" + recommendationServiceHost + ":" + recommendationServicePort
                + "/recommendation?productId=";
        this.reviewServiceUrl = "http://" + reviewServiceHost + ":" + reviewServicePort + "/review?productId=";
        this.webClient = webClientBuilder.build();
    }

    @Override
    public Flux<Review> getReviews(int productId) {
        return webClient.get().uri(reviewServiceUrl + productId)
            .retrieve()
            .bodyToFlux(Review.class)
            .log()
            .onErrorResume(e->Flux.empty());
    }

    @Override
    public Flux<Recommendation> getRecommendations(int productId) {
        return webClient.get().uri(recommendationServiceUrl + productId)
            .retrieve()
            .bodyToFlux(Recommendation.class)
            .log()
            .onErrorResume(e->Flux.empty());
    }

    @Override
    public Mono<Product> getProduct(int productId) {
        return webClient.get().uri(productServiceUrl + productId)
            .retrieve().bodyToMono(Product.class)
            .log()
            .onErrorMap(WebClientResponseException.class, e->handleException(e));
    }

    private Throwable handleException(Throwable e) {
        if(!(e instanceof WebClientResponseException)){
            log.warn("Unexpected error: {}, rethrowing", e);
            return e;
        }
        WebClientResponseException exception = (WebClientResponseException)e;
        switch (exception.getStatusCode()) {
            case UNPROCESSABLE_ENTITY:
                throw new InvalidInputException(exception);
            case NOT_FOUND:
                throw new NotFoundException(exception);
            default:
                log.warn("Unexpected HTTP error: {}, rethrowing.", exception.getStatusCode());
                log.warn("error body", exception.getResponseBodyAsString());
                return exception;
        }
    }

    @Override
    public Review createReview(Review review) {
        return webClient.post().uri(reviewServiceUrl)
        .accept(MediaType.APPLICATION_JSON)
        .header("Content-Type", "application/json")
        .body(Mono.just(review), Review.class)
        .retrieve()
        .bodyToMono(Review.class)
        .log()
        .block();
    }

    @Override
    public void deleteReviews(int productId) {
        webClient.delete().uri(reviewServiceUrl + productId)
        .exchange()
        .log()
        .block();
    }

    @Override
    public Recommendation createRecommendation(Recommendation body) {
        return webClient.post().uri(recommendationServiceUrl)
        .accept(MediaType.APPLICATION_JSON)
        .header("Content-Type", "application/json")
        .body(Mono.just(body), Recommendation.class)
        .retrieve()
        .bodyToMono(Recommendation.class)
        .log()
        .block();
    }

    @Override
    public void deleteRecommendations(int productId) {
        webClient.delete().uri(recommendationServiceUrl+productId)
        .exchange()
        .log()
        .block();
    }

    @Override
    public Product createProduct(Product body) {
        return webClient.post().uri(productServiceUrl)
        .accept(MediaType.APPLICATION_JSON)
        .header("Content-Type", "application/json")
        .body(Mono.just(body), Product.class)
        .retrieve()
        .bodyToMono(Product.class)
        .log()
        .block();
    }

    @Override
    public void deleteProduct(int productId) {
        webClient.delete().uri(productServiceUrl + productId)
            .exchange()
            .log()
            .block();
    }

}
