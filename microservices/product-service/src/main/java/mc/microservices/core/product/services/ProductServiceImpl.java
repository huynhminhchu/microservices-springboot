package mc.microservices.core.product.services;

import mc.microservices.api.core.product.Product;
import mc.microservices.api.core.product.ProductService;
import mc.microservices.api.exceptions.InvalidInputException;
import mc.microservices.api.exceptions.NotFoundException;
import mc.microservices.core.product.persistence.ProductEntity;
import mc.microservices.core.product.persistence.ProductRepository;
import mc.microservices.util.http.ServiceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.logging.Level;

import static java.util.logging.Level.FINE;

@RestController
public class ProductServiceImpl implements ProductService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;
    private final ServiceUtil serviceUtil;

    private final ProductMapper productMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ServiceUtil serviceUtil, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.serviceUtil = serviceUtil;
        this.productMapper = productMapper;
    }

    @Override
    public Mono<Product> getProduct(int productId) {
        if (productId < 1) throw new InvalidInputException("Invalid productId: " + productId);
        LOG.info("Ok la");
        return productRepository.findByProductId(productId)
                .switchIfEmpty(Mono.error(new NotFoundException("No product found for productId: " + productId)))
                .log(LOG.getName(), FINE)
                .map(productMapper::entityToApi)
                .map(this::setServiceAddress);
    }

    @Override
    public Mono<Product> createProduct(Product body) {
        if (body.getProductId() < 1) {
            throw new InvalidInputException("Invalid productId: " + body.getProductId());
        }

        ProductEntity entity = productMapper.apiToEntity(body);
        Mono<Product> newEntity = productRepository.save(entity)
                .log(LOG.getName(), FINE)
                .onErrorMap(
                        DuplicateKeyException.class,
                        ex -> new InvalidInputException("Duplicate key, Product Id: " + body.getProductId()))
                .map(productMapper::entityToApi);

        return newEntity;
    }

    @Override
    public Mono<Void> deleteProduct(int productId) {
//        productRepository.findByProductId(productId).ifPresent(productRepository::delete);
        return productRepository.findByProductId(productId).
                log(LOG.getName(), FINE).map(productRepository::delete)
                .flatMap(e -> e)
                ;
        // .map(productRepository::delete) return Mono<Mono<Void>>

    }

    Product setServiceAddress(Product product){
        product.setServiceAddress(serviceUtil.getServiceAddress());
        return product;
    }
}
