package mc.microservices.core.product;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;

public abstract class MongoTestBase {

    private static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0").withExposedPorts(27017);

    static {
        mongoDBContainer.start();
    }
    @DynamicPropertySource
    private static void setProperties(DynamicPropertyRegistry registry){
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);

    }
}
