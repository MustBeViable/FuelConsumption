FROM --platform=linux/amd64 maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn -B clean package -DskipTests

FROM --platform=linux/amd64 eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/FuelConsumption-1.0-SNAPSHOT.jar app.jar

CMD ["java", "-jar", "app.jar"]