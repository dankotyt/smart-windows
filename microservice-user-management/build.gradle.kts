plugins {
    java
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "ru.pin36bik"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation ("org.mockito:mockito-core:4.0.0")
    testImplementation ("org.springframework.boot:spring-boot-starter-test:2.7.0")
    testImplementation ("org.hamcrest:hamcrest-library:2.2")
    testImplementation("org.springframework.security:spring-security-test")
    //testImplementation("com.h2database:h2:2.2.220")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.postgresql:postgresql")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    //implementation ("org.flywaydb:flyway-core")
    implementation ("io.github.cdimascio:dotenv-java:3.0.0")
    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
    implementation("org.springframework.kafka:spring-kafka")
    implementation(project(":microservice-presets"))

    compileOnly ("org.springframework.boot:spring-boot-starter-security")
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    runtimeOnly ("org.postgresql:postgresql")
}

tasks.test {
    useJUnitPlatform()
}