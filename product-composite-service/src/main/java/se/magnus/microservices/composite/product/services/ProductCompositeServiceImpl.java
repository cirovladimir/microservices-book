package se.magnus.microservices.composite.product.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;
import se.magnus.microservices.api.composite.ProductAggregate;
import se.magnus.microservices.api.composite.ProductCompositeService;
import se.magnus.microservices.api.composite.RecommendationSummary;
import se.magnus.microservices.api.composite.ReviewSummary;
import se.magnus.microservices.api.composite.ServiceAddresses;
import se.magnus.microservices.api.product.Product;
import se.magnus.microservices.api.recommendation.Recommendation;
import se.magnus.microservices.api.review.Review;
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
    public Mono<ProductAggregate> getProduct(int productId) {
        log.debug("request to get product with id {}", productId);

        return Mono.zip(values->createProductAggregate((Product)values[0], (List<Recommendation>)values[1], (List<Review>)values[2]),
            integration.getProduct(productId),
            integration.getRecommendations(productId).collectList(),
            integration.getReviews(productId).collectList()
        ).doOnError(e->{
            log.warn("Get Product aggregate failed with: {}", e);
        }).log();
    }

    private ProductAggregate createProductAggregate(Product product, List<Recommendation> recommendations,
            List<Review> reviews) {

        List<RecommendationSummary> recommendationSummaries = recommendations.stream().map(recommendation -> {
            return new RecommendationSummary(recommendation.getProductId(), recommendation.getRecommendationId(),
                    recommendation.getAuthor(), recommendation.getRate(), recommendation.getContent());
        }).collect(Collectors.toList());
        List<ReviewSummary> reviewSummaries = reviews.stream().map(review -> {
            return new ReviewSummary(review.getProductId(), review.getReviewId(), review.getAuthor(),
                    review.getSubject(), review.getContent());
        }).collect(Collectors.toList());
        String reviewServiceAddress = reviews.stream().findFirst().map(review -> review.getServiceAddress()).orElse("");
        String recommendationServiceAddress = recommendations.stream().findFirst()
                .map(recommendation -> recommendation.getServiceAddress()).orElse("");

        ServiceAddresses serviceAddresses = new ServiceAddresses(serviceUtil.getServiceAddress(),
                product.getServiceAddress(), reviewServiceAddress, recommendationServiceAddress);
        return new ProductAggregate(product.getProductId(), product.getName(), product.getWeight(),
                recommendationSummaries, reviewSummaries, serviceAddresses);
    }

    @Override
    public Mono<ProductAggregate> createProduct(ProductAggregate productAggregate) {
        Product product = new Product(productAggregate.getProductId(), productAggregate.getName(),
                productAggregate.getWeight(), null);
        integration.createProduct(product);
        productAggregate.getRecommendations().stream().forEach(info -> {
            Recommendation recommendation = new Recommendation(info.getProductId(), info.getRecommendationId(),
                    info.getAuthor(), info.getRate(), info.getContent(), null);
            integration.createRecommendation(recommendation);
        });
        productAggregate.getReviews().stream().forEach(info -> {
            Review review = new Review(info.getProductId(), info.getReviewId(), info.getAuthor(), info.getSubject(),
                    info.getContent(), null);
            integration.createReview(review);
        });
        return Mono.just(productAggregate);
    }

    @Override
    public void deleteProduct(int productId) {
        integration.deleteProduct(productId);
        integration.deleteRecommendations(productId);
        integration.deleteReviews(productId);
    }

}
