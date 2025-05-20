FROM amazoncorretto:21 as builder

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle ./gradle
COPY microservice-visual-content/build.gradle.kts ./microservice-visual-content/
COPY microservice-visual-content/src ./microservice-visual-content/src

RUN ./gradlew :microservice-visual-content:bootJar --no-daemon

FROM amazoncorretto:21-jre

WORKDIR /app

COPY --from=builder /app/microservice-visual-content/build/libs/*.jar app.jar

RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]