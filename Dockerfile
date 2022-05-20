#
# Build stage
#
FROM maven:3.8.3-openjdk-16-slim AS build
COPY src /app/src
COPY pom.xml .env wait-for-it.sh /app/
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip

#
# Package stage
#
FROM openjdk:16-jdk-alpine
WORKDIR /app
RUN apk add --no-cache bash
COPY --from=build /app/target/*.jar app.jar
COPY --from=build /app/.env /app/wait-for-it.sh /app/
RUN chmod +x wait-for-it.sh
ENTRYPOINT ["java","-jar","app.jar"]