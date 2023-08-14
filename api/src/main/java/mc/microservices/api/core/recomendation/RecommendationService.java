package mc.microservices.api.core.recomendation;

import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface RecommendationService {

    @GetMapping(value = "/recommendation", produces = "application/json")
    List<Recommendation> getRecommendations(@RequestParam(value = "productId", required = true) int productId );
    // cur localhost:8080/recommendation?productId= 1

    @PostMapping(value = "/recommendation", consumes = "application/json", produces = "application/json")
    Recommendation createRecommendation(@RequestBody Recommendation body);

    @DeleteMapping(value = "/recommendation")
    void deleteRecommendations(@RequestParam(value = "productId", required = true) int productId);
}
