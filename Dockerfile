FROM maven:3.9.9-amazoncorretto-23-al2023

# Sao chép file .jar từ builder sang runtime image
COPY target/hotel_booking-0.0.1-SNAPSHOT.jar app.jar

# Mở port ứng dụng
EXPOSE 8080

# Chạy ứng dụng
ENTRYPOINT ["java", "-jar", "app.jar"]
