package se.magnus.microservices.core.recommendation.services;

import java.util.List;

import org.mapstruct.Mapper;

import se.magnus.microservices.api.recommendation.Recommendation;
import se.magnus.microservices.core.recommendation.persistence.RecommendationEntity;

@Mapper(componentModel = "spring")
public interface RecommendationMapper {

    RecommendationEntity toEntity(Recommendation recommendation);

    List<RecommendationEntity> toEntity(List<Recommendation> recommendation);

    Recommendation toApi(RecommendationEntity recommendationEntity);

    List<Recommendation> toApi(List<RecommendationEntity> recommendationEntity);

}
