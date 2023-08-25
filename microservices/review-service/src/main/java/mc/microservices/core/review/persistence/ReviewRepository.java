package mc.microservices.core.review.persistence;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface ReviewRepository extends R2dbcRepository<ReviewEntity, Integer> {

    @Transactional(readOnly = true)
    Flux<ReviewEntity> findByProductId(int productId);
}
