FROM amazoncorretto:21 AS builder

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle ./gradle
COPY microservice-analytics/build.gradle.kts ./microservice-analytics/
COPY microservice-analytics/src ./microservice-analytics/src

RUN ./gradlew :microservice-analytics:bootJar --no-daemon --info

FROM amazoncorretto:21

WORKDIR /app

COPY --from=builder /app/microservice-analytics/build/libs/*.jar app.jar

RUN yum install -y shadow-utils && \
    groupadd --system spring && \
    useradd --system --gid spring spring && \
    yum clean all

USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
