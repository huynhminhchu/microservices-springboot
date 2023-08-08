FROM eclipse-temurin:20-jdk
EXPOSE 8080
WORKDIR workspace
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} product-service.jar
ENTRYPOINT ["java","-jar", "product-service.jar"]