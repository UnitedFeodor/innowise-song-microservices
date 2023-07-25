FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
COPY target/*.jar song-api.jar
ENTRYPOINT ["java","-jar","/song-api.jar"]