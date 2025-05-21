FROM amazoncorretto:21 AS builder

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle ./gradle
COPY microservice-user-management/build.gradle.kts ./microservice-user-management/
COPY microservice-user-management/src ./microservice-user-management/src

RUN ./gradlew :microservice-user-management:bootJar --no-daemon --info

FROM amazoncorretto:21

WORKDIR /app

COPY --from=builder /app/microservice-user-management/build/libs/*.jar app.jar

RUN yum install -y shadow-utils && \
    groupadd --system spring && \
    useradd --system --gid spring spring && \
    yum clean all

USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
