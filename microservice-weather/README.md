# Микросервис пользователей

## 1. Назначение
**microservice-weather** — Spring Boot-микросервис, позволяющий получать актуальные данные о погоде по Yandex Weather API.
Основные функции:
- Получение локации нужного умного окна
- Обращение по API к Yandex Weather и обработка данных о погоде

**Технологический стек:**
- **JDK**: Amazon Corretto 21
- **Spring Boot**: 3.2.0
- **Тестирование**: JUnit 5, Mockito, Jupiter
- **Линтеры**: Checkstyle, PMD
- **Гитхуки**: pre-commit проверки (модульные тесты)

## 2. Архитектура и зависимости
### Основные технологии:
- Java 17 (Amazon Corretto 21)
- Spring Boot 3.2.0
- Gradle (сборка)
- Kafka (взаимодействие с другими микросервисами)
- Swagger/OpenAPI 3.0 (документация)

### Взаимодействие:
- **api-gateway**: 
  - Общая точка входа для микросервиса
- **microservice-window-management**: 
  - Получение актуальных данных о локации умного окна

**Дизайн API:** Разработано в соответствии с корпоративным API Design Guide

## 3. Способы запуска
### Docker:
cd microservice-weather/
docker build -t weather-service .
docker-compose up microservice-weather либо docker run -d --name weather -p 8088:8080 weathert-service

### Локально:
./gradlew :microservice-weather:run 

## 4. API документация:

При запущенном MicroserviceWeatherApplication: http://localhost:8088/swagger-ui.html

## 5. Тестирование

Запуск тестов: ./gradlew :microservice-weather:check 

### Используемый стек:
- JUnit 5
- Mockito
- Jupiter

## 6. Контакты

Автор микросервиса в Telegram: @eximun
