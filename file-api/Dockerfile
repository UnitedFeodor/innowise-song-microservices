FROM openjdk:17
VOLUME /tmp
COPY target/*.jar file-api.jar
ENTRYPOINT ["java","-jar","/file-api.jar"]