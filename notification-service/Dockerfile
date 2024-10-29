FROM amazoncorretto:17.0.12

WORKDIR /app

COPY build/libs/notification-service-0.0.1-SNAPSHOT.jar /app/notification-service.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "notification-service.jar"]
