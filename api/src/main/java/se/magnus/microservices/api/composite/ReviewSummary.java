package se.magnus.microservices.api.composite;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ReviewSummary {
    private final int reviewId;
    private final String author;
    private final String subject;

    @JsonCreator
    public ReviewSummary(@JsonProperty("reviewId") int reviewId, @JsonProperty("author") String author,
            @JsonProperty("subject") String subject) {
        this.reviewId = reviewId;
        this.author = author;
        this.subject = subject;
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

}
