# FROM gradle:8.14-jdk21 AS build

# WORKDIR /app

# COPY gradle ./gradle
# COPY gradlew ./gradlew
# COPY gradlew.bat ./gradlew.bat
# COPY settings.gradle ./settings.gradle
# COPY model ./model
# COPY messagingservice/ ./messagingservice

# WORKDIR /app/messagingservice
# RUN gradle bootJar --no-daemon

# FROM gcr.io/distroless/java21-debian12

# COPY --from=build /app/messagingservice/build/libs/*.jar messagingservice.jar
# EXPOSE 8080
# CMD ["messagingservice.jar"]

FROM openjdk:21-jdk-slim
COPY build/libs/*.jar messagingservice.jar
ENTRYPOINT ["java", "-jar", "/messagingservice.jar"]
