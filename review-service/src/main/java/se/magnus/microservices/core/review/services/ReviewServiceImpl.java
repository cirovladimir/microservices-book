package se.magnus.microservices.core.review.services;

import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
import reactor.core.scheduler.Scheduler;
import se.magnus.microservices.api.review.Review;
import se.magnus.microservices.api.review.ReviewService;
import se.magnus.microservices.core.review.persistence.ReviewEntity;
import se.magnus.microservices.core.review.persistence.ReviewRepository;
import se.magnus.microservices.util.http.ServiceUtil;


@RestController
public class ReviewServiceImpl implements ReviewService {
    private final Logger log = LoggerFactory.getLogger(ReviewServiceImpl.class);

    private final ServiceUtil serviceUtil;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final Scheduler scheduler;

    public ReviewServiceImpl(ServiceUtil serviceUtil, ReviewRepository reviewRepository, ReviewMapper reviewMapper, Scheduler scheduler) {
        this.serviceUtil = serviceUtil;
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.scheduler = scheduler;
    }

    @Override
    public Flux<Review> getReviews(int productId) {
        log.debug("will get reviews for product id: {}", productId);
        return Flux.defer(()->Flux.fromIterable(
            reviewRepository.findByProductId(productId).stream()
            .map(r->reviewMapper.toApi(r))
            .map(r->{
                r.setServiceAddress(serviceUtil.getServiceAddress());
                return r;
            }).collect(Collectors.toList())
            )).subscribeOn(scheduler).log(null, Level.FINE);
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
