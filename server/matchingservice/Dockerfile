FROM gradle:8.14-jdk21 AS build

WORKDIR /app

COPY gradle ./gradle
COPY gradlew ./gradlew
COPY gradlew.bat ./gradlew.bat
COPY settings.gradle ./settings.gradle
COPY model ./model
COPY matchingservice/ ./matchingservice

WORKDIR /app/matchingservice
RUN gradle bootJar --no-daemon

FROM gcr.io/distroless/java21-debian12
COPY --from=build /app/matchingservice/build/libs/*.jar matchingservice.jar
EXPOSE 8080
CMD ["matchingservice.jar"]
