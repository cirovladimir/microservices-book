package se.magnus.microservices.core.recommendation.services;

import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
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
    public Flux<Recommendation> getRecommendations(int productId) {
        return recommendationRepository.findByProductId(productId)
        .log()
        .map(e->recommendationMapper.toApi(e))
        .map(e->{
            e.setServiceAddress(serviceUtil.getServiceAddress());
            return e;
        });
    }

    @Override
    public Recommendation createRecommendation(Recommendation body) {
        RecommendationEntity entity = recommendationMapper.toEntity(body);
        return recommendationRepository.save(entity)
        .log()
        .map(e->recommendationMapper.toApi(e))
        .toProcessor()
        .block();
    }

    @Override
    public void deleteRecommendations(int productId) {
        recommendationRepository.deleteAll(recommendationRepository.findByProductId(productId)).toProcessor().block();
    }

}