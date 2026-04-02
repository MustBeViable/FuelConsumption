FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -B clean package -DskipTests

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/FuelConsumption-1.0-SNAPSHOT.jar app.jar

ENV DB_URL=jdbc:mariadb://db:3306/fuel_calculator_localization
ENV DB_USER=fuel_user
ENV DB_PASSWORD=fuel_password

CMD ["java", "-jar", "app.jar"]