FROM eclipse-temurin:17
COPY build/libs/*.jar locationservice.jar
ENTRYPOINT ["java", "-jar", "/locationservice.jar"]

# FROM gradle:8.14-jdk21 AS build

# WORKDIR /app

# COPY gradle ./gradle
# COPY gradlew ./gradlew
# COPY gradlew.bat ./gradlew.bat
# COPY settings.gradle ./settings.gradle
# COPY model ./model
# COPY locationservice/ ./locationservice

# WORKDIR /app/locationservice
# RUN gradle bootJar --no-daemon

# FROM gcr.io/distroless/java21-debian12

# COPY --from=build /app/locationservice/build/libs/*.jar locationservice.jar
# EXPOSE 8080
# CMD ["locationservice.jar"]