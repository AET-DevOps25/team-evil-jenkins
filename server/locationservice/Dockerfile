FROM openjdk:21-jdk-slim
COPY build/libs/*.jar locationservice.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/locationservice.jar"]