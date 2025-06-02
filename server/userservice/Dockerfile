FROM gradle:8.14-jdk21 AS build

WORKDIR /app

COPY gradle ./gradle
COPY gradlew ./gradlew
COPY gradlew.bat ./gradlew.bat
COPY settings.gradle ./settings.gradle
COPY model ./model
COPY userservice/ ./userservice

WORKDIR /app/userservice
RUN gradle bootJar --no-daemon

FROM gcr.io/distroless/java21-debian12

COPY --from=build /app/userservice/build/libs/*.jar userservice.jar
EXPOSE 8080
CMD ["userservice.jar"]