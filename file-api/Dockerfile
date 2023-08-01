FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
COPY target/*.jar file-api.jar
ENTRYPOINT ["java","-jar","/file-api.jar"]