package mc.microservices.core.product.services;

import mc.microservices.api.core.product.Product;
import mc.microservices.api.core.product.ProductService;
import mc.microservices.api.exceptions.InvalidInputException;
import mc.microservices.api.exceptions.NotFoundException;
import mc.microservices.core.product.persistence.ProductEntity;
import mc.microservices.core.product.persistence.ProductRepository;
import mc.microservices.util.http.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductServiceImpl implements ProductService {

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
    public Product getProduct(int productId) {
        if (productId < 1) throw new InvalidInputException("Invalid productId: " + productId);
        ProductEntity entity = productRepository.findByProductId(productId)
                .orElseThrow(() -> new NotFoundException("No product found for productId:" + productId));
        Product response = productMapper.entityToApi(entity);
        response.setServiceAddress(serviceUtil.getServiceAddress());
        return response;
    }

    @Override
    public Product createProduct(Product body) {
        try {
            ProductEntity entity = productMapper.apiToEntity(body);
            ProductEntity newEntity = productRepository.save(entity);
            return productMapper.entityToApi(newEntity);
        } catch (DuplicateKeyException dke){
            throw new InvalidInputException("Duplicate Key, product id:" + body.getProductId());
        }

    }

    @Override
    public void deleteProduct(int productId) {
        productRepository.findByProductId(productId).ifPresent(productRepository::delete);
    }
}
