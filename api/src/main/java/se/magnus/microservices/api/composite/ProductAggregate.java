package se.magnus.microservices.api.composite;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ProductAggregate {
    private final int productId;
    private final String name;
    private final int weight;
    private final List<RecommendationSummary> recommendations;
    private final List<ReviewSummary> reviews;
    private final ServiceAddresses serviceAddresses;

    @JsonCreator
    public ProductAggregate(@JsonProperty("productId") int productId, @JsonProperty("name") String name,
            @JsonProperty("weight") int weight,
            @JsonProperty("recommnedations") List<RecommendationSummary> recommendations,
            @JsonProperty("reviews") List<ReviewSummary> reviews,
            @JsonProperty("serviceAddresses") ServiceAddresses serviceAddresses) {
        this.productId = productId;
        this.name = name;
        this.weight = weight;
        this.recommendations = recommendations;
        this.reviews = reviews;
        this.serviceAddresses = serviceAddresses;
    }

    public int getProductId() {
        return this.productId;
    }

    public String getName() {
        return this.name;
    }

    public int getWeight() {
        return this.weight;
    }

    public List<RecommendationSummary> getRecommendations() {
        return this.recommendations;
    }

    public List<ReviewSummary> getReviews() {
        return this.reviews;
    }

    public ServiceAddresses getServiceAddresses() {
        return this.serviceAddresses;
    }

}
