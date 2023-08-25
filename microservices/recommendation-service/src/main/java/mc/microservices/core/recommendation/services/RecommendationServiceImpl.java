package mc.microservices.core.recommendation.services;

import mc.microservices.api.core.recommendation.Recommendation;
import mc.microservices.api.core.recommendation.RecommendationService;
import mc.microservices.api.exceptions.InvalidInputException;
import mc.microservices.core.recommendation.persistence.RecommendationEntity;
import mc.microservices.core.recommendation.persistence.RecommendationRepository;
import mc.microservices.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.logging.Level;

@RestController
public class RecommendationServiceImpl implements RecommendationService {

    private static final Logger LOG = LoggerFactory.getLogger(RecommendationServiceImpl.class);
    private final ServiceUtil serviceUtil;

    private final RecommendationRepository recommendationRepository;

    private final RecommendationMapper mapper;
    @Autowired
    public RecommendationServiceImpl(ServiceUtil serviceUtil, RecommendationRepository recommendationRepository, RecommendationMapper mapper) {
        this.serviceUtil = serviceUtil;
        this.recommendationRepository = recommendationRepository;
        this.mapper = mapper;
    }

    @Override
    public Flux<Recommendation> getRecommendations(int productId) {
        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }
        return recommendationRepository.findByProductId(productId).
                log(LOG.getName(), Level.FINE).
                map(mapper::entityToApi).
                map(this::setServiceAddress);

    }

    @Override
    public Mono<Recommendation> createRecommendation(Recommendation body) {

        RecommendationEntity recommendationEntity = mapper.apiToEntity(body);
        Mono<Recommendation> newRecommendation = recommendationRepository.save(recommendationEntity).map(mapper::entityToApi);
        return newRecommendation;

    }

    @Override
    public Mono<Void> deleteRecommendations(int productId) {
        Flux<RecommendationEntity> recommendationEntities = recommendationRepository.findByProductId(productId);
        return recommendationRepository.deleteAll(recommendationEntities);
    }

    Recommendation setServiceAddress(Recommendation recommendation){
        recommendation.setServiceAddress(serviceUtil.getServiceAddress());
        return recommendation;
    }
}
