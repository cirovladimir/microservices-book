package se.magnus.microservices.composite.product.services;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

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

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final String productServiceUrl;
    private final String recommendationServiceUrl;
    private final String reviewServiceUrl;

    public ProductCompositeIntegration(RestTemplate restTemplate, ObjectMapper mapper,
            @Value("${app.product-service.host}") String productServiceHost,
            @Value("${app.product-service.port}") String productServicePort,
            @Value("${app.recommendation-service.host}") String recommendationServiceHost,
            @Value("${app.recommendation-service.port}") String recommendationServicePort,
            @Value("${app.review-service.host}") String reviewServiceHost,
            @Value("${app.review-service.port}") String reviewServicePort) {
        this.restTemplate = restTemplate;
        this.mapper = mapper;
        this.productServiceUrl = "http://" + productServiceHost + ":" + productServicePort + "/product/";
        this.recommendationServiceUrl = "http://" + recommendationServiceHost + ":" + recommendationServicePort
                + "/recommendation?productId=";
        this.reviewServiceUrl = "http://" + reviewServiceHost + ":" + reviewServicePort + "/review?productId=";
    }

    @Override
    public List<Review> getReviews(int productId) {
        return restTemplate.exchange(reviewServiceUrl + productId, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Review>>() {
                }).getBody();
    }

    @Override
    public List<Recommendation> getRecommendations(int productId) {
        return restTemplate.exchange(recommendationServiceUrl + productId, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<Recommendation>>() {
                }).getBody();
    }

    @Override
    public Product getProduct(int productId) {
        try {
            return restTemplate.getForObject(productServiceUrl + productId, Product.class);
        } catch (HttpClientErrorException e) {
            switch (e.getStatusCode()) {
                case UNPROCESSABLE_ENTITY:
                    throw new InvalidInputException(e);
                case NOT_FOUND:
                    throw new NotFoundException(e);
                default:
                    log.warn("Unexpected HTTP error: {}, rethrowing.", e.getStatusCode());
                    log.warn("error body", e.getResponseBodyAsString());
                    throw e;
            }
        }

    }

}
