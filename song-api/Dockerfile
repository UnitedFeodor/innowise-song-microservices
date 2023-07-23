FROM openjdk:17
VOLUME /tmp
COPY target/*.jar song-api.jar
ENTRYPOINT ["java","-jar","/song-api.jar"]