FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
COPY target/*.jar enricher-api.jar
ENTRYPOINT ["java","-jar","/enricher-api.jar"]