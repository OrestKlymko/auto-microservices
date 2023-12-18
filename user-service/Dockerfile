FROM openjdk:latest
LABEL authors="orestklymko"
ADD target/user-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar","app.jar"]