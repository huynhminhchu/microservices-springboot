package mc.microservices.core.product.persistence;

import mc.microservices.api.core.product.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface ProductRepository extends PagingAndSortingRepository<ProductEntity, String>, CrudRepository<ProductEntity, String> {

    Optional<ProductEntity> findByProductId(int productId);

}
