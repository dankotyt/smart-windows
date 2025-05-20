FROM amazoncorretto:21 as builder

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle ./gradle
COPY microservice-window-management/build.gradle.kts ./microservice-window-management/
COPY microservice-window-management/src ./microservice-window-management/src

RUN ./gradlew :microservice-window-management:bootJar --no-daemon

FROM amazoncorretto:21-jre

WORKDIR /app

COPY --from=builder /app/microservice-window-management/build/libs/*.jar app.jar

RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]