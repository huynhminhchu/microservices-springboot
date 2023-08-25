package mc.microservices.core.product.services;


import mc.microservices.api.core.product.Product;
import mc.microservices.api.core.product.ProductService;
import mc.microservices.api.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class MessageProcessorConfig {

    private static final Logger LOG = LoggerFactory.getLogger(MessageProcessorConfig.class);
    private final ProductService productService;

    @Autowired
    public MessageProcessorConfig(ProductService productService) {
        this.productService = productService;
    }

    @Bean
    public Consumer<Event<Integer, Product>> messageProcessor(){
        return event -> {
            switch (event.getEventType()){
                case CREATE:
                    Product product = event.getData();
                    productService.createProduct(product).block();
                    break;
                case DELETE:
                    Integer productId = event.getKey();
                    productService.deleteProduct(productId).block();
                    break;
                default:
                    String errorMessage = "Incorrect event type: " + event.getEventType();
                    System.out.println(errorMessage);
            }
            LOG.info("Message processing done!");

        };
    }
}
