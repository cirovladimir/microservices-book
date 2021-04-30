package se.magnus.microservices.api.composite;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RecommendationSummary {

    private final int recommendationId;
    private final String author;
    private final int rate;

    @JsonCreator
    public RecommendationSummary(@JsonProperty("recommendationId") int recommendationId,
            @JsonProperty("author") String author, @JsonProperty("rate") int rate) {
        this.recommendationId = recommendationId;
        this.author = author;
        this.rate = rate;
    }

    public int getRecommnedationId() {
        return this.recommendationId;
    }

    public String getAuthor() {
        return this.author;
    }

    public int getRate() {
        return this.rate;
    }

}
