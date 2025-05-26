FROM eclipse-temurin:17
VOLUME /tmp
COPY build/libs/*.jar userservice.jar
ENTRYPOINT ["java", "-jar", "/userservice.jar"]