FROM amazoncorretto:21 as builder

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle ./gradle
COPY microservice-user-management/build.gradle.kts ./microservice-user-management/
COPY microservice-user-management/src ./microservice-user-management/src

RUN ./gradlew :microservice-user-management:bootJar --no-daemon

FROM amazoncorretto:21-jre

WORKDIR /app

COPY --from=builder /app/microservice-user-management/build/libs/*.jar app.jar

RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]