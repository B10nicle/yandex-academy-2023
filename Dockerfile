# Build stage
FROM gradle:latest AS BUILD
WORKDIR /opt/app
COPY . .
RUN gradle build

# Package stage
FROM openjdk:17.0.1-jdk-slim

ENV JAR_NAME=yandex-lavka-0.0.1-SNAPSHOT.jar
ENV APP_HOME=/opt/app
ENV POSTGRES_SERVER=db
ENV POSTGRES_PORT=5432
ENV POSTGRES_DB=postgres
ENV POSTGRES_USER=postgres
ENV POSTGRES_PASSWORD=password

WORKDIR $APP_HOME
COPY --from=BUILD /opt/app/build/libs/$JAR_NAME $APP_HOME
EXPOSE 8080
ENTRYPOINT exec java -jar $JAR_NAME
