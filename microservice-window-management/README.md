# Микросервис пользователей

## 1. Назначение
**microservice-window-management** — Spring Boot-микросервис, позволяющий взаимодействовать с умными окнами через приложение Smart Windows.
Основные функции:
- CRUD-операции с умными окнами
- Взаимодействие с умными окнами через приложение
- Получение данных об окнах от роли администратора

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
- **microservice-user-management**: 
  - Получение актуальных данных о пользователе и владельце умного окна

**Дизайн API:** Разработано в соответствии с корпоративным API Design Guide

## 3. Способы запуска
### Docker:
cd microservice-window-management/
docker build -t window-management-service .
docker-compose up microservice-window-management либо docker run -d --name window -p 8082:8080 window-management-service

### Локально:
./gradlew :microservice-window-management:run 

## 4. API документация:

При запущенном MicroserviceWindowApplication: http://localhost:8083/swagger-ui.html

## 5. Тестирование

Запуск тестов: ./gradlew :microservice-window-management:check 

### Используемый стек:
- JUnit 5
- Mockito
- Jupiter

## 6. Контакты

Автор микросервиса в Telegram: @eximun
