plugins {
    java
    id("org.springframework.boot") version "3.4.3"
    id("io.spring.dependency-management") version "1.1.7"
    id("checkstyle")
    id("pmd")
}

group = "ru.pin36bik"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.kafka:spring-kafka")
    implementation("org.postgresql:postgresql")
    implementation("org.projectlombok:lombok")
    implementation("org.modelmapper:modelmapper:2.4.4")
    implementation("io.github.cdimascio:dotenv-java:3.0.0")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
}

checkstyle{
    toolVersion = "10.12.4"
    configFile = file("${rootProject.projectDir}/config/checkstyle/checkstyle.xml")
    isShowViolations = true
    isIgnoreFailures = false
}

tasks.checkstyleMain{
    reports {
        xml.required = false
        html.required = true
    }
}

pmd{
    toolVersion = "6.55.0"
    ruleSetFiles = files("${rootProject.projectDir}/config/pmd/pmd-ruleset.xml")
    isIgnoreFailures = false
}

tasks.pmdMain{
    reports {
        xml.required = false
        html.required = true
    }
}

tasks.test {
    useJUnitPlatform()
}