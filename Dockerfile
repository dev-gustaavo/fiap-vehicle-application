# Etapa de build
FROM gradle:8.11.1-jdk17 AS build

WORKDIR /app

COPY build.gradle settings.gradle ./
COPY src ./src

RUN gradle build --no-daemon

# Etapa de runtime
FROM openjdk:17-slim

WORKDIR /app

ENV APP_NAME=vehicle-application
ENV JAR_FILE=${APP_NAME}-0.0.1-SNAPSHOT.jar

COPY --from=build /app/build/libs/${JAR_FILE} /app/${APP_NAME}.jar

# Baixar e configurar o wait-for-it.sh
RUN apt-get update && apt-get install -y curl \
    && curl -o /app/wait-for-it.sh https://raw.githubusercontent.com/vishnubob/wait-for-it/master/wait-for-it.sh \
    && chmod +x /app/wait-for-it.sh

ENTRYPOINT ["/app/wait-for-it.sh", "mysql-db:3306", "--", "java", "-jar", "/app/vehicle-application.jar"]

EXPOSE 8083
