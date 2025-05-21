FROM amazoncorretto:21 AS builder

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle ./gradle
COPY microservice-weather/build.gradle.kts ./microservice-weather/
COPY microservice-notifications/src ./microservice-weather/src

RUN ./gradlew :microservice-weather:bootJar --no-daemon

FROM amazoncorretto:21

WORKDIR /app

COPY --from=builder /app/microservice-weather/build/libs/*.jar app.jar

RUN yum install -y shadow-utils && \
    groupadd --system spring && \
    useradd --system --gid spring spring && \
    yum clean all

USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
