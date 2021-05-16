package se.magnus.microservices.api.composite;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RecommendationSummary {

    private final int productId;
    private final int recommendationId;
    private final String author;
    private final int rate;
    private String content;

    @JsonCreator
    public RecommendationSummary(@JsonProperty("productId") int productId,
            @JsonProperty("recommendationId") int recommendationId, @JsonProperty("author") String author,
            @JsonProperty("rate") int rate, @JsonProperty("content") String content) {
        this.productId = productId;
        this.recommendationId = recommendationId;
        this.author = author;
        this.rate = rate;
        this.content = content;
    }

    public int getProductId(){
        return this.productId;
    }

    public int getRecommendationId() {
        return this.recommendationId;
    }

    public String getAuthor() {
        return this.author;
    }

    public int getRate() {
        return this.rate;
    }

    public String getContent(){
        return this.content;
    }
}
