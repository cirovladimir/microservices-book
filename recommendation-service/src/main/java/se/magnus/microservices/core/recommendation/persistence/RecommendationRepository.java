package se.magnus.microservices.core.recommendation.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecommendationRepository extends CrudRepository<RecommendationEntity, String>{
    
}