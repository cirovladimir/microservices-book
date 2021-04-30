package se.magnus.microservices.composite.product.services;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import se.magnus.microservices.api.composite.ProductAggregate;
import se.magnus.microservices.api.composite.ProductCompositeService;
import se.magnus.microservices.api.composite.RecommendationSummary;
import se.magnus.microservices.api.composite.ReviewSummary;
import se.magnus.microservices.api.composite.ServiceAddresses;
import se.magnus.microservices.api.product.Product;
import se.magnus.microservices.api.recommendation.Recommendation;
import se.magnus.microservices.api.review.Review;
import se.magnus.microservices.util.exceptions.InvalidInputException;
import se.magnus.microservices.util.http.ServiceUtil;

@RestController
public class ProductCompositeServiceImpl implements ProductCompositeService {
    private final Logger log = LoggerFactory.getLogger(ProductCompositeServiceImpl.class);

    private final ProductCompositeIntegration integration;
    private final ServiceUtil serviceUtil;

    public ProductCompositeServiceImpl(ProductCompositeIntegration integration, ServiceUtil serviceUtil) {
        this.integration = integration;
        this.serviceUtil = serviceUtil;
    }

    @Override
    public ProductAggregate getProduct(int productId) {
        log.debug("request to get product with id {}", productId);
        throw new InvalidInputException();
        // Product product = integration.getProduct(productId);
        // List<Recommendation> recommendations = integration.getRecommendations(productId);
        // List<Review> reviews = integration.getReviews(productId);
        // log.debug("product, review and recommendation service had been called: {} {} {}", product, recommendations, reviews);
        // return createProductAggregate(product, recommendations, reviews);
    }

    private ProductAggregate createProductAggregate(Product product, List<Recommendation> recommendations,
            List<Review> reviews) {

        List<RecommendationSummary> recommendationSummaries = recommendations.stream().map(recommendation -> {
            return new RecommendationSummary(recommendation.getRecommendationId(), recommendation.getAuthor(),
                    recommendation.getRate());
        }).collect(Collectors.toList());
        List<ReviewSummary> reviewSummaries = reviews.stream().map(review -> {
            return new ReviewSummary(review.getReviewId(), review.getAuthor(), review.getSubject());
        }).collect(Collectors.toList());
        String reviewServiceAddress = reviews.stream().findFirst().map(review -> review.getServiceAddress()).orElse("");
        String recommendationServiceAddress = recommendations.stream().findFirst()
                .map(recommendation -> recommendation.getServiceAddress()).orElse("");

        ServiceAddresses serviceAddresses = new ServiceAddresses(serviceUtil.getServiceAddress(), product.getServiceAddress(), reviewServiceAddress,
                recommendationServiceAddress);
        return new ProductAggregate(product.getProductId(), product.getName(), product.getWeight(),
                recommendationSummaries, reviewSummaries, serviceAddresses);
    }

}
