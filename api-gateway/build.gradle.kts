plugins {
    id("java")
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "ru.pin36bik"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    implementation ("io.github.cdimascio:dotenv-java:3.0.0")
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation("io.jsonwebtoken:jjwt-api:0.12.3")
    implementation("io.github.openfeign:feign-jackson:12.5")
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    //implementation(project(":microservice-user-management"))

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.3")

    compileOnly("org.projectlombok:lombok")

    annotationProcessor("org.projectlombok:lombok")
}

tasks.test {
    useJUnitPlatform()
}