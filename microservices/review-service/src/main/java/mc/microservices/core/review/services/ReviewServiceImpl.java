package mc.microservices.core.review.services;

import mc.microservices.api.core.review.Review;
import mc.microservices.api.core.review.ReviewService;
import mc.microservices.api.exceptions.InvalidInputException;
import mc.microservices.core.review.persistence.ReviewEntity;
import mc.microservices.core.review.persistence.ReviewRepository;
import mc.microservices.util.http.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReviewServiceImpl implements ReviewService {

    private final ServiceUtil serviceUtil;
    private final ReviewRepository reviewRepository;

    private final ReviewMapper mapper;
    @Autowired
    public ReviewServiceImpl(ServiceUtil serviceUtil, ReviewRepository reviewRepository, ReviewMapper mapper) {
        this.serviceUtil = serviceUtil;
        this.reviewRepository = reviewRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Review> getReviews(int productId) {
        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }
        List<ReviewEntity> entities = reviewRepository.findByProductId(productId);
        List<Review> reviews = mapper.entityListToApiList(entities);

        reviews.forEach(r -> {
            r.setServiceAddress(serviceUtil.getServiceAddress());
        });
        return reviews;
    }

    @Override
    public Review createReview(Review body) {
        ReviewEntity reviewEntity = mapper.apiToEntity(body);
        ReviewEntity newReview = reviewRepository.save(reviewEntity);
        return mapper.entityToApi(newReview);
    }

    @Override
    public void deleteReviews(int productId) {
        List<ReviewEntity> reviewEntities = reviewRepository.findByProductId(productId);
        reviewRepository.deleteAll(reviewEntities);
    }
}
