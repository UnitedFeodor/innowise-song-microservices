FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
COPY target/*.jar auth-api.jar
ENTRYPOINT ["java","-jar","/auth-api.jar"]