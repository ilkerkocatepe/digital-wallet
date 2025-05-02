# Build stage
FROM maven:3.9-eclipse-temurin-21-alpine AS build
WORKDIR /home/app
CMD mv .dockerize/Dockerfile ./
COPY ./ /home/app/

RUN mvn clean package

# Package stage
FROM eclipse-temurin:21-jdk-alpine AS runtime
WORKDIR /app
COPY --from=build /home/app/target/digital-wallet-*.jar /app/digital-wallet.jar
RUN apk update && \
    apk upgrade && \
    apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/Europe/Istanbul /etc/localtime && \
    echo "Europe/Istanbul" > /etc/timezone
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "-Xmx4G", "/app/digital-wallet.jar"]
HEALTHCHECK --interval=30s --timeout=5s --start-period=60s CMD curl --fail http://localhost:8080/healthcheck || exit 1