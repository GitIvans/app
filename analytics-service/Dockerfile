FROM amazoncorretto:17.0.12

WORKDIR /app

COPY build/libs/analytics-service-0.0.1-SNAPSHOT.jar /app/analytics-service.jar

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "analytics-service.jar"]
