package mc.microservices.composite.product.services;

import mc.microservices.api.core.product.Product;
import mc.microservices.api.core.product.ProductService;
import mc.microservices.api.core.recomendation.Recommendation;
import mc.microservices.api.core.recomendation.RecommendationService;
import mc.microservices.api.core.review.Review;
import mc.microservices.api.core.review.ReviewService;
import mc.microservices.api.exceptions.InvalidInputException;
import mc.microservices.api.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.HttpMethod.GET;

@Component
public class ProductCompositeIntegration implements ProductService, RecommendationService, ReviewService {

    private final RestTemplate rest;

    private final String productServiceUrl;
    private final String reviewServiceUrl;
    private final String recommendationServiceUrl;
    @Autowired
    public ProductCompositeIntegration(RestTemplate rest
            , @Value("${app.product-service.host}") String productServiceHost
            , @Value("${app.product-service.port}") String productServicePort
            , @Value("${app.review-service.host}") String reviewServiceHost
            , @Value("${app.review-service.port}") String reviewServicePort
            , @Value("${app.recommendation-service.host}") String recommendationServiceHost
            , @Value("${app.recommendation-service.port}") String recommendationServicePort){
        this.rest = rest;
        this.productServiceUrl = "http://" + productServiceHost + ":" + productServicePort + "/product/";
        this.reviewServiceUrl = "http://" + reviewServiceHost + ":" + reviewServicePort + "/review?productId=";
        this.recommendationServiceUrl = "http://" + recommendationServiceHost + ":" + recommendationServicePort + "/recommendation?productId=";

    }

    @Override
    public Product getProduct(int productId) {
        try {
            String url = productServiceUrl + productId;
            return rest.getForObject(url, Product.class);
        } catch (HttpClientErrorException ex){
            switch (ex.getStatusCode().value()) {
                case 404 -> throw new NotFoundException("Not found exception 2");
                case 422 -> throw new InvalidInputException("Invalid input exception 2");
                default -> throw ex;
            }

        }
    }

    @Override
    public List<Recommendation> getRecommendations(int productId) {
        String url = recommendationServiceUrl + productId;
        System.out.println("Rec:" + url);
        ResponseEntity<List<Recommendation>> response = rest.exchange(
                url,
                GET,
                null,
                new ParameterizedTypeReference<List<Recommendation>>() {
                });
        return response.getBody();
//        List<Recommendation> recommendations = rest
//                .exchange(url, GET, null, new ParameterizedTypeReference<List<Recommendation>>() {})
//                .getBody();
//        return recommendations;
    }

    @Override
    public List<Review> getReviews(int productId) {
        String url = reviewServiceUrl + productId;
        System.out.println("review:" + url);
        ResponseEntity<List<Review>> response = rest.exchange(
                url,
                GET,
                null,
                new ParameterizedTypeReference<List<Review>>() {
                });
        return response.getBody();
    }
}
