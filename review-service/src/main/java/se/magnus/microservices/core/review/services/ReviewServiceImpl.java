package se.magnus.microservices.core.review.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

import se.magnus.microservices.api.review.Review;
import se.magnus.microservices.api.review.ReviewService;
import se.magnus.microservices.util.http.ServiceUtil;


@RestController
public class ReviewServiceImpl implements ReviewService {

    private final ServiceUtil serviceUtil;

    public ReviewServiceImpl(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public List<Review> getReviews(int productId) {
        return Arrays.asList(new Review(productId, productId+20000, "author", "subject", "content", serviceUtil.getServiceAddress()));
    }
    
}
