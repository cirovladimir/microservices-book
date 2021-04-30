package se.magnus.microservices.api.composite;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ServiceAddresses {

    private final String compositeAddress;
    private final String productAddress;
    private final String reviewAddress;
    private final String reccommendationAddress;

    @JsonCreator
    public ServiceAddresses(@JsonProperty("compositeAddress") String compositeAddress,
            @JsonProperty("productAddress") String productAddress, @JsonProperty("reviewAddress") String reviewAddress,
            @JsonProperty("recommendationAddress") String reccommendationAddress) {
        this.compositeAddress = compositeAddress;
        this.productAddress = productAddress;
        this.reviewAddress = reviewAddress;
        this.reccommendationAddress = reccommendationAddress;
    }

    public String getCompositeAddress() {
        return this.compositeAddress;
    }

    public String getProductAddress() {
        return this.productAddress;
    }

    public String getReviewAddress() {
        return this.reviewAddress;
    }

    public String getReccommendationAddress() {
        return this.reccommendationAddress;
    }

}
