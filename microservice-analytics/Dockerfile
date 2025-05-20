FROM amazoncorretto:21 as builder

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle ./gradle
COPY microservice-analytics/build.gradle.kts ./microservice-analytics/
COPY microservice-analytics/src ./microservice-analytics/src

RUN ./gradlew :microservice-analytics:bootJar --no-daemon

FROM amazoncorretto:21-jre

WORKDIR /app

COPY --from=builder /app/microservice-analytics/build/libs/*.jar app.jar

RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]