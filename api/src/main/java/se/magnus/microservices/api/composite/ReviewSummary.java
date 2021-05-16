package se.magnus.microservices.api.composite;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReviewSummary {
    private final int productId;
    private final int reviewId;
    private final String author;
    private final String subject;
    private final String content;

    @JsonCreator
    public ReviewSummary(@JsonProperty("productId") int productId, @JsonProperty("reviewId") int reviewId, @JsonProperty("author") String author,
            @JsonProperty("subject") String subject, @JsonProperty("content") String content) {
        this.productId = productId;
        this.reviewId = reviewId;
        this.author = author;
        this.subject = subject;
        this.content = content;
    }

    public int getProductId(){
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

    public String getContent(){
        return this.content;
    }

}
