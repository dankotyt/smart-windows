pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/snapshot") }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        mavenCentral()
        maven { url = uri("https://repo.spring.io/milestone") }
        maven { url = uri("https://repo.spring.io/snapshot") }
    }
}

rootProject.name = "smart-windows"
include(
    "microservice-weather",
    "microservice-user-management",
    "microservice-analytics",
    "microservice-notifications",
    "microservice-presets",
    "microservice-visual-content",
    "microservice-window-management"
)