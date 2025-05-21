# Микросервис пресетов

## 1. Назначение
**microservice-presets** — Spring Boot-микросервис, управляющий распределением пресетов настроек умного окна.  
Основные функции:
- Выгрузка пресетов из базы данных PostgreSQL либо добавление новых пресетов
- Учёт количества загрузок каждого доступного пресета и их регистрация в другом микросервисе с помощью Kafka

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
- Redis (кэширование данных)
- Kafka (асинхронная коммуникация с другими микросервисами)
- Swagger/OpenAPI 3.0 (документация)

### Взаимодействие:
- **microservice-analytics**: 
  - Топик: `preset-downloads-topic`
- **api-gateway**: 
  - Топики: `preset-request-topic`, `preset-response-topic`

**Дизайн API:** Разработано в соответствии с корпоративным API Design Guide


## 3. Способы запуска
### Docker:
cd microservice-presets/
docker build -t presets-service .
docker-compose up microservice-presets либо docker run -d --name presets -p 8082:8080 presets-service

### Локально:
./gradlew :microservice-presets:run 

### Используемые переменные окружения:
DB_USER_LOGIN=your_db_login
DB_USER_PASSWORD=your_db_password


## 4. API документация:

При запущенном PresetsApplication: http://localhost:8080/swagger-ui.html

Основные конечные точки:


## 5. Тестирование

Запуск тестов: ./gradlew :microservice-presets:check 

### Используемый стек:
- JUnit 5
- Mockito
- Jupiter


## 6. Контакты

Автор микросервиса в Telegram: @Snowflakes17
