FROM openjdk:latest
LABEL authors="orestklymko"
ADD target/user-service-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar","app.jar"]