FROM openjdk:17
VOLUME /tmp
COPY target/*.jar config-service.jar
ENTRYPOINT ["java","-jar","/config-service.jar"]