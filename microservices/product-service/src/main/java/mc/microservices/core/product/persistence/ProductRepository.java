package mc.microservices.core.product.persistence;

import mc.microservices.api.core.product.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

import java.util.Optional;

public interface ProductRepository extends ReactiveCrudRepository<ProductEntity, String>{

    Mono<ProductEntity> findByProductId(int productId);

}
