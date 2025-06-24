FROM openjdk:21-jdk-slim
COPY build/libs/*.jar userservice.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/userservice.jar"]
