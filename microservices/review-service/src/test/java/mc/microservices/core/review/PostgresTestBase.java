package mc.microservices.core.review;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public abstract class PostgresTestBase {

    private static PostgreSQLContainer postgresqlContainer = new PostgreSQLContainer("postgres:15.4")
            .withDatabaseName("review-test")
            .withUsername("postgres")
            .withPassword("postgres");

    static {
        postgresqlContainer.start();
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry){
        registry.add("spring.datasource.url", postgresqlContainer ::getJdbcUrl);
        registry.add("spring.datasource.username", postgresqlContainer::getUsername);
        registry.add("spring.datasource.password", postgresqlContainer::getPassword);
    }
}
