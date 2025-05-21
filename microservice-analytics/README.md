# Микросервис аналитики

## 1. Назначение
**microservice-analytics** — Spring Boot-микросервис, управляющий сбором аналитических данных по использованию возможностей умных окон.  
Основные функции:
- Регистрация и подсчёт количества загрузок каждого пресета настроек умного окна, информация поступает из microservice-presets по Kafka
- Регистрация и подсчёт количества авторизаций и даты последней авторизации каждого пользователя сервиса, информация поступает из microservice-user-management по Kafka

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
- PostgreSQL (хранение данных)
- Kafka (асинхронная коммуникация с другими микросервисами)
- Swagger/OpenAPI 3.0 (документация)

### Взаимодействие:
- **microservice-presets**: 
  - Топик: `preset-downloads-topic`
- **microservice-user-management**: 
  - Топики: `user-login-topic`

**Дизайн API:** Разработано в соответствии с API Design Guide


## 3. Способы запуска
### Docker:
cd microservice-analytics/
docker build -t analytics-service .
docker-compose up microservice-analytics либо docker run -d --name presets -p 8084:8080 analytics-service

### Локально:
./gradlew :microservice-analytics:run 

### Используемые переменные окружения:
DB_USER_LOGIN=your_db_login
DB_USER_PASSWORD=your_db_password


## 4. API документация:

При запущенном AnalyticsApplication: http://localhost:8083/swagger-ui.html

Основные конечные точки:


## 5. Тестирование

Запуск тестов: ./gradlew :microservice-analytics:check 

### Используемый стек:
- JUnit 5
- Mockito
- Jupiter


## 6. Контакты

Автор микросервиса в Telegram: @Snowflakes17
