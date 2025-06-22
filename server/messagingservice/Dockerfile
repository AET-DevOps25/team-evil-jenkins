FROM eclipse-temurin:17
VOLUME /tmp
COPY build/libs/*.jar /messagingservice.jar
ENTRYPOINT ["java", "-jar", "/messagingservice.jar"]
