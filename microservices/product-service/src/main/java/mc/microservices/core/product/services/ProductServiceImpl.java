package mc.microservices.core.product.services;

import mc.microservices.api.core.product.Product;
import mc.microservices.api.core.product.ProductService;
import mc.microservices.api.exceptions.InvalidInputException;
import mc.microservices.api.exceptions.NotFoundException;
import mc.microservices.util.http.ServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductServiceImpl implements ProductService {

    private final ServiceUtil serviceUtil;

    @Autowired
    public ProductServiceImpl(ServiceUtil serviceUtil) {
        this.serviceUtil = serviceUtil;
    }

    @Override
    public Product getProduct(int productId) {
        if (productId < 1) throw new InvalidInputException("Invalid productId: " + productId);
        if (productId == 13) throw new NotFoundException("No product found for productId: " + productId);
        return new Product(productId, "name-" + productId, 123, serviceUtil.getServiceAddress());
    }
}
