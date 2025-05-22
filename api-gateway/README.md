# Микросервис пользователей

## 1. Назначение
**api-gateway** — Spring Boot-микросервис, являющийся общей точкой входа во все приложение Smart Windows.
Основные функции:
- Авторизация пользователей и переадресация на необходимый микросервис

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
- Swagger/OpenAPI 3.0 (документация)

### Взаимодействие:
- **microservice-user-management**: 
  - Обращение к auth/validate для проверки актуальности токенов

**Дизайн API:** Разработано в соответствии с корпоративным API Design Guide

## 3. Способы запуска
### Docker:
cd api-gateway/
docker build -t api-gateway-service .
docker-compose up api-gateway либо docker run -d --name gateway -p 8080:8080 api-gateway-service

### Локально:
./gradlew :api-gateway:run 

## 4. API документация:

При запущенном ApiGatewayApplication: http://localhost:8080/swagger-ui.html

## 5. Тестирование

Запуск тестов: ./gradlew :api-gateway:check 

### Используемый стек:
- JUnit 5
- Mockito
- Jupiter

## 6. Контакты

Автор микросервиса в Telegram: @eximun
