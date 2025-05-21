FROM amazoncorretto:21 AS builder

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle ./gradle
COPY microservice-presets/build.gradle.kts ./microservice-presets/
COPY microservice-presets/src ./microservice-presets/src

RUN ./gradlew :microservice-presets:bootJar --no-daemon --info

FROM amazoncorretto:21

WORKDIR /app

COPY --from=builder /app/microservice-presets/build/libs/*.jar app.jar

RUN yum install -y shadow-utils && \
    groupadd --system spring && \
    useradd --system --gid spring spring && \
    yum clean all

USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
