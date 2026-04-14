FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -B clean package -DskipTests dependency:copy-dependencies

FROM eclipse-temurin:17-jdk
WORKDIR /app

COPY --from=build /app/target/FuelConsumption-1.0-SNAPSHOT.jar app.jar
COPY --from=build /app/target/dependency /app/dependency

ENV DISPLAY=host.docker.internal:0
ENV DB_URL=jdbc:mariadb://host.docker.internal:3317/fuel_calculator_localization
ENV DB_USER=fuel_user
ENV DB_PASSWORD=fuel_password

CMD ["java", "--module-path", "/app/dependency", "--add-modules", "javafx.controls,javafx.fxml", "-jar", "app.jar"]