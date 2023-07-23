FROM openjdk:17
VOLUME /tmp
COPY target/*.jar enricher-api.jar
ENTRYPOINT ["java","-jar","/enricher-api.jar"]