FROM eclipse-temurin:17
COPY build/libs/*.jar locationservice.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/locationservice.jar"]
