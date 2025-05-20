FROM amazoncorretto:21 as builder

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle ./gradle
COPY microservice-presets/build.gradle.kts ./microservice-presets/
COPY microservice-presets/src ./microservice-presets/src

RUN ./gradlew :microservice-presets:bootJar --no-daemon

FROM amazoncorretto:21-jre

WORKDIR /app

COPY --from=builder /app/microservice-presets/build/libs/*.jar app.jar

RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]