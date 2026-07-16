FROM maven:3.9.12-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/stable-manager-0.0.1-SNAPSHOT.jar stable-manager.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","stable-manager.jar"]