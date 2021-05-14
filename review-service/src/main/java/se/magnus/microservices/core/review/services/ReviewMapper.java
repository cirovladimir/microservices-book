package se.magnus.microservices.core.review.services;

import java.util.List;

import org.mapstruct.Mapper;

import se.magnus.microservices.api.review.Review;
import se.magnus.microservices.core.review.persistence.ReviewEntity;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    ReviewEntity toEntity(Review review);

    List<ReviewEntity> toEntity(List<Review> reviews);

    Review toApi(ReviewEntity reviewEntity);

    List<Review> toApi(List<ReviewEntity> reviewEntity);
}
