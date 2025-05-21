FROM amazoncorretto:21 AS builder

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle ./gradle
COPY api-gateway/build.gradle.kts ./api-gateway/
COPY api-gateway/src ./api-gateway/src

RUN ./gradlew :api-gateway:bootJar --no-daemon --info

FROM amazoncorretto:21

WORKDIR /app

COPY --from=builder /app/api-gateway/build/libs/*.jar app.jar

RUN yum install -y shadow-utils && \
    groupadd --system spring && \
    useradd --system --gid spring spring && \
    yum clean all

USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
