FROM eclipse-temurin:17
ARG JAR_FILE=build/libs/spring-sample-truck-rest-0.0.2-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]