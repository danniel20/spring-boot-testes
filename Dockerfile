#
# Build stage
#
FROM maven:3.8.3-openjdk-16-slim AS build
COPY src /app/src
COPY pom.xml .env /app/
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip

#
# Package stage
#
FROM openjdk:16-jdk-alpine
COPY --from=build /app/target/*.jar /app/app.jar
COPY --from=build /app/.env /app/
ENTRYPOINT ["java","-jar","/app/app.jar"]