repositories {
	gradlePluginPortal()
	mavenCentral()
}

plugins {
	java
	id("org.springframework.boot") version "3.2.0" //3.4.3
	id("io.spring.dependency-management") version "1.1.7"
}

group = "ru.pin36bik"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

extra["springCloudVersion"] = "2023.0.0"

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

// Применяем dependencyManagement для всех подпроектов
subprojects {
	apply(plugin = "io.spring.dependency-management")

	dependencyManagement {
		imports {
			mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
		}
	}
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.modelmapper:modelmapper:2.4.4")
	implementation ("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")
	implementation("io.swagger.parser.v3:swagger-parser:2.1.22")
	implementation("javax.xml.bind:jaxb-api:2.3.1")

}

tasks.withType<Test> {
	useJUnitPlatform()
}