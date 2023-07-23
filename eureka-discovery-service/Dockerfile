FROM openjdk:17
VOLUME /tmp
COPY target/*.jar eureka-discovery-service.jar
ENTRYPOINT ["java","-jar","/eureka-discovery-service.jar"]