FROM amazoncorretto:17.0.12

WORKDIR /app

COPY build/libs/subscription-service-0.0.1-SNAPSHOT.jar /app/subscription-service.jar

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "subscription-service.jar"]