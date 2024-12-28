FROM maven:3.9.9-amazoncorretto-21-al2023
COPY target/hotel_booking-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]