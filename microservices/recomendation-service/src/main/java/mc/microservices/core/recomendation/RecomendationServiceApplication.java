package mc.microservices.core.recomendation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

@SpringBootApplication
@ComponentScan(basePackages = "mc")
public class RecomendationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecomendationServiceApplication.class, args);
    }

}
