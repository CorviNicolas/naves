FROM maven:3-amazoncorretto-21-debian AS builder

COPY . /app/

WORKDIR /app

RUN mvn clean package

FROM openjdk:21-slim

WORKDIR /app

COPY --from=builder /app/target/naves-*.jar /app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]