package se.magnus.microservices.api.review;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Review {
    private final int productId;
    private final int reviewId;
    private final String author;
    private final String subject;
    private final String content;
    private final String serviceAddress;

    @JsonCreator
    public Review(@JsonProperty("productId") int productId, @JsonProperty("reviewId") int reviewId,
            @JsonProperty("author") String author, @JsonProperty("subject") String subject,
            @JsonProperty("content") String content, @JsonProperty("serviceAddress") String serviceAddress) {
        this.productId = productId;
        this.reviewId = reviewId;
        this.author = author;
        this.subject = subject;
        this.content = content;
        this.serviceAddress = serviceAddress;
    }

    public int getProductId() {
        return this.productId;
    }

    public int getReviewId() {
        return this.reviewId;
    }

    public String getAuthor() {
        return this.author;
    }

    public String getSubject() {
        return this.subject;
    }

    public String getContent() {
        return this.content;
    }

    public String getServiceAddress() {
        return this.serviceAddress;
    }
}
