package se.magnus.microservices.core.recommendation.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

@DataMongoTest
public class RecommendationRepositoryTest {
    
    @Autowired
    RecommendationRepository recommendationRepository;

    RecommendationEntity savedRecommendationEntity;

    @BeforeEach
    public void setup(){
        savedRecommendationEntity = new RecommendationEntity(1, 1, "author", 5, "content");
        recommendationRepository.save(savedRecommendationEntity).block();

        assertEquals(1, recommendationRepository.count().block());
    }

    @Test
    public void create(){
        RecommendationEntity newRecommendationEntity = new RecommendationEntity(2, 2, "author", 3, "content");
        recommendationRepository.save(newRecommendationEntity).block();

        RecommendationEntity foundRecommendationEntity = recommendationRepository.findById(newRecommendationEntity.getId()).block();

        assertEquals(newRecommendationEntity, foundRecommendationEntity);
        assertEquals(2, recommendationRepository.count().block());
    }
}
