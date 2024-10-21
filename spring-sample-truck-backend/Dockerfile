FROM eclipse-temurin:17
ARG JAR_FILE=build/libs/spring-sample-truck-backend-0.0.3-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]