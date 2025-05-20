FROM eclipse-temurin:17
VOLUME /tmp
COPY build/libs/*.jar user-service.jar
ENTRYPOINT ["java", "-jar", "/user-service.jar"]