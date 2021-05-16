package se.magnus.microservices.core.review.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.RestController;

import se.magnus.microservices.api.review.Review;
import se.magnus.microservices.api.review.ReviewService;
import se.magnus.microservices.core.review.persistence.ReviewEntity;
import se.magnus.microservices.core.review.persistence.ReviewRepository;
import se.magnus.microservices.util.http.ServiceUtil;


@RestController
public class ReviewServiceImpl implements ReviewService {

    private final ServiceUtil serviceUtil;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    public ReviewServiceImpl(ServiceUtil serviceUtil, ReviewRepository reviewRepository, ReviewMapper reviewMapper) {
        this.serviceUtil = serviceUtil;
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
    }

    @Override
    public List<Review> getReviews(int productId) {
        List<ReviewEntity> foundReviews = reviewRepository.findByProductId(productId);

        return reviewMapper.toApi(foundReviews).stream().map(review -> {
            review.setServiceAddress(serviceUtil.getServiceAddress());
            return review;
        }).collect(Collectors.toList());
    }

    @Override
    public Review createReview(Review body) {
        ReviewEntity reviewEntity = reviewMapper.toEntity(body);
        ReviewEntity savedEntity = reviewRepository.save(reviewEntity);
        return reviewMapper.toApi(savedEntity);
    }

    @Override
    public void deleteReviews(int productId) {
        reviewRepository.findByProductId(productId).forEach(review -> reviewRepository.delete(review));
    }
    
}
