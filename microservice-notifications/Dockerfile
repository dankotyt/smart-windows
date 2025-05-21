FROM amazoncorretto:21 AS builder

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle ./gradle
COPY microservice-notifications/build.gradle.kts ./microservice-notifications/
COPY microservice-notifications/src ./microservice-notifications/src

RUN ./gradlew :microservice-notifications:bootJar --no-daemon --info

FROM amazoncorretto:21

WORKDIR /app

COPY --from=builder /app/microservice-notifications/build/libs/*.jar app.jar

RUN yum install -y shadow-utils && \
    groupadd --system spring && \
    useradd --system --gid spring spring && \
    yum clean all

USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
