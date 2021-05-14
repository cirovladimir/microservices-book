package se.magnus.microservices.core.recommendation.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RestController;

import se.magnus.microservices.api.recommendation.Recommendation;
import se.magnus.microservices.api.recommendation.RecommendationService;
import se.magnus.microservices.core.recommendation.persistence.RecommendationEntity;
import se.magnus.microservices.core.recommendation.persistence.RecommendationRepository;
import se.magnus.microservices.util.http.ServiceUtil;

@RestController
public class RecommendationServiceImpl implements RecommendationService {

    private final ServiceUtil serviceUtil;
    private final RecommendationRepository recommendationRepository;
    private final RecommendationMapper recommendationMapper;

    public RecommendationServiceImpl(ServiceUtil serviceUtil, RecommendationRepository recommendationRepository,
            RecommendationMapper recommendationMapper) {
        this.serviceUtil = serviceUtil;
        this.recommendationRepository = recommendationRepository;
        this.recommendationMapper = recommendationMapper;
    }

    @Override
    public List<Recommendation> getRecommendations(int productId) {
        List<RecommendationEntity> foundRecommendations = recommendationRepository.findByProductId(productId);

        return recommendationMapper.toApi(foundRecommendations).stream().map(recommendation -> {
            recommendation.setServiceAddress(serviceUtil.getServiceAddress());
            return recommendation;
        }).collect(Collectors.toList());
    }

    @Override
    public Recommendation createRecommendation(Recommendation body) {
        RecommendationEntity entity = recommendationMapper.toEntity(body);
        RecommendationEntity savedEntity = recommendationRepository.save(entity);
        return recommendationMapper.toApi(savedEntity);
    }

    @Override
    public void deleteRecommendations(int productId) {
        recommendationRepository.deleteAll(recommendationRepository.findByProductId(productId));
    }

}