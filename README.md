![img.png](img.png)

- 3 cores microservices: the Product, Review and Recommendation Service
- 1 composite microservices: the Product Composite service.


## Product service: manages product information
- Product ID
- Name
- Weight

## Review service: manages product reviews
- Product ID
- Review ID
- Author
- Subject
- Content

## Recomendation service: manage product recommendations
- Product ID
- Recommendation ID
- Author
- Rate
- Content

# Init the spring projects
- Init 4 empty spring projects in Intellji.
![img_2.png](img_2.png)
- Dependency: WebFlux and Actuator
- Result:
![img_3.png](initresult.png)

# Adding an API and a util project


## Get list of json objects with Spring RestTemplate
```java
String url = "http://example.com/api/objects";
RestTemplate restTemplate = new RestTemplate();
ResponseEntity<List<Object>> response = restTemplate.exchange(
    url,
    HttpMethod.GET,
    null,
    new ParameterizedTypeReference<List<Object>>(){});
List<Object> objects = response.getBody();
```

# Add exception handling

# Deploy microservices using Docker
## Setting max heap size
- By default, max heap size inside a jdk container = 1/4 host memory.
- Add -Xmx option to set the limit

docker pull eclipse-temurin:20-jdk
docker run -it --rm eclipse-temurin:20-jdk java -Xmx600m -XX:+PrintFlagsFinal | grep "size_t MaxHeapSize"

# Building maven multi module
```java
mvn archetype:generate -DgroupId=mc.microservices -DartifactId=microservices-springboot
```

# Dockerize
```dockerfile
FROM eclipse-temurin:20-jdk as builder
WORKDIR workspace
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract

FROM eclipse-temurin:20-jdk
WORKDIR workspace
COPY --from=builder workspace/dependencies/ ./
COPY --from=builder workspace/spring-boot-loader/ ./
COPY --from=builder workspace/snapshot-dependencies/ ./
COPY --from=builder workspace/application/ ./

EXPOSE 8080

ENTRYPOINT ["java", "org.springframework.boot.loader.JarLauncher"]
```

```bash
cd microservices-springboot
mvn clean install -DskipTests
docker build -t product-service:v1 microservices/product-service/
docker build -t review-service:v1 microservices/review-service/
docker build -t recommendation-service:v1 microservices/recommendation-service/ 
```

Test
```
docker run --rm -p 9080:8080 -e "SPRING_PROFILES_ACTIVE=docker" product-service:v1    
```
