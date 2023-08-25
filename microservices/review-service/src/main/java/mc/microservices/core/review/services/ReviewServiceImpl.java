package mc.microservices.core.review.services;

import mc.microservices.api.core.review.Review;
import mc.microservices.api.core.review.ReviewService;
import mc.microservices.api.exceptions.InvalidInputException;
import mc.microservices.core.review.persistence.ReviewEntity;
import mc.microservices.core.review.persistence.ReviewRepository;
import mc.microservices.util.http.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
    public Flux<Review> getReviews(int productId) {
        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }
        return reviewRepository.findByProductId(productId)
                                    .map(mapper::entityToApi)
                                    .map(this::setServiceAddress);
    }

    @Override
    public Mono<Review> createReview(Review body) {
        ReviewEntity reviewEntity = mapper.apiToEntity(body);
        return reviewRepository.save(reviewEntity).map(mapper::entityToApi);
    }

    @Override
    public Mono<Void> deleteReviews(int productId) {
        Flux<ReviewEntity> reviewEntities = reviewRepository.findByProductId(productId);
        return reviewRepository.deleteAll(reviewEntities);
    }

    Review setServiceAddress(Review review){
        review.setServiceAddress(serviceUtil.getServiceAddress());
        return review;
    }
}
