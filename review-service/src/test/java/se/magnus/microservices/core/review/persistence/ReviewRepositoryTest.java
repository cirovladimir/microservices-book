package se.magnus.microservices.core.review.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ReviewRepositoryTest {

    private final Logger log = LoggerFactory.getLogger(ReviewRepositoryTest.class);
    
    @Autowired
    private ReviewRepository reviewRepository;

    private ReviewEntity savedReviewEntity;

    @BeforeEach
    public void setup(){
        reviewRepository.deleteAll();

        ReviewEntity review = new ReviewEntity(1, 1, "author", "subject", "content");
        savedReviewEntity = reviewRepository.save(review);

        assertEquals(1, reviewRepository.count());
    }

    @Test
    public void create(){
        ReviewEntity review = new ReviewEntity(2, 2, "author", "subject", "content");
        ReviewEntity savedReview = reviewRepository.save(review);

        ReviewEntity foundReview = reviewRepository.findById(savedReview.getId()).get();

        assertEquals(savedReview, foundReview);
        assertEquals(2, reviewRepository.count());
    }
    
    @Test
    public void update(){
        ReviewEntity savedReview = reviewRepository.findById(savedReviewEntity.getId()).get();
        savedReview.setAuthor("author-updated");
        reviewRepository.save(savedReview);

        ReviewEntity foundReview = reviewRepository.findById(savedReview.getId()).get();

        assertEquals("author-updated", foundReview.getAuthor());
        assertEquals(1, reviewRepository.count());
    }

}
