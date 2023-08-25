package mc.microservices.api.core.recommendation;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RecommendationService {

    @GetMapping(value = "/recommendation", produces = "application/json")
    Flux<Recommendation> getRecommendations(@RequestParam(value = "productId", required = true) int productId );
    // cur localhost:8080/recommendation?productId= 1

    @PostMapping(value = "/recommendation", consumes = "application/json", produces = "application/json")
    Mono<Recommendation> createRecommendation(@RequestBody Recommendation body);

    @DeleteMapping(value = "/recommendation")
    Mono<Void> deleteRecommendations(@RequestParam(value = "productId", required = true) int productId);
}
