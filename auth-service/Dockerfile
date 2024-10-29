FROM amazoncorretto:17.0.12

WORKDIR /app

COPY build/libs/auth-service-0.0.1-SNAPSHOT.jar /app/auth-service.jar

EXPOSE 8085

ENTRYPOINT ["java", "-jar", "auth-service.jar"]
