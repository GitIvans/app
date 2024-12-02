FROM amazoncorretto:17.0.12

WORKDIR /app

COPY build/libs/user-service-0.0.1-SNAPSHOT.jar /app/user-service.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "user-service.jar"]
