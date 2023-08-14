package mc.microservices.core.recommendation.services;

import mc.microservices.api.core.recomendation.Recommendation;
import mc.microservices.api.core.recomendation.RecommendationService;
import mc.microservices.api.exceptions.InvalidInputException;
import mc.microservices.core.recommendation.persistence.RecommendationEntity;
import mc.microservices.core.recommendation.persistence.RecommendationRepository;
import mc.microservices.util.http.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RecommendationServiceImpl implements RecommendationService {

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
    public List<Recommendation> getRecommendations(int productId) {
        if (productId < 1) {
            throw new InvalidInputException("Invalid productId: " + productId);
        }
        List<RecommendationEntity> entities = recommendationRepository.findByProductId(productId);
        List<Recommendation> recommendations = mapper.entityListToApiList(entities);

        recommendations.forEach(r -> {
            r.setServiceAddress(serviceUtil.getServiceAddress());
        });
        return recommendations;
    }

    @Override
    public Recommendation createRecommendation(Recommendation body) {
        RecommendationEntity recommendationEntity = mapper.apiToEntity(body);
        RecommendationEntity newRecommendation = recommendationRepository.save(recommendationEntity);
        return mapper.entityToApi(newRecommendation);
    }

    @Override
    public void deleteRecommendations(int productId) {
        List<RecommendationEntity> recommendationEntities = recommendationRepository.findByProductId(productId);
        recommendationRepository.deleteAll(recommendationEntities);
    }
}
