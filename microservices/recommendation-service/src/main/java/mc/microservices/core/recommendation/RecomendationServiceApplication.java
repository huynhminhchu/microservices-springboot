package mc.microservices.core.recommendation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "mc")
public class RecomendationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecomendationServiceApplication.class, args);
    }

}
