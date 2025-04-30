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

tasks.withType<Test> {
	useJUnitPlatform()
}