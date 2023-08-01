FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
COPY target/*.jar eureka-discovery-service.jar
ENTRYPOINT ["java","-jar","/eureka-discovery-service.jar"]