package se.magnus.microservices.core.recommendation.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import se.magnus.microservices.api.recommendation.Recommendation;
import se.magnus.microservices.api.recommendation.RecommendationService;
import se.magnus.microservices.util.http.ServiceUtil;

@RestController
public class RecommendationServiceImpl implements RecommendationService {

    private final ServiceUtil serviceUtil;
    
    public RecommendationServiceImpl(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public List<Recommendation> getRecommendations(int productId) {
        return Arrays.asList(new Recommendation(productId, productId+10000, "author", 5, "content", serviceUtil.getServiceAddress()));
    }
    
}