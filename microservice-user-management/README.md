# Микросервис пользователей

## 1. Назначение
**microservice-user-management** — Spring Boot-микросервис, сохраняющий данные пользователей, позволяющий зарегистрироваться и аутентифицироваться в приложении Smart Windows.  
Основные функции:
- Регистрация и аутентификация пользователей с дальнейшим сохранением данных в PostgreSQL
- Изменение данных о пользователе
- Возможности администратора: поиск пользователя по id, email, получение всех пользователей

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
- **api-gateway**: 
  - Общая точка входа, перенаправляющая на необходимый микросервис (**microservice-user-management**)

**Дизайн API:** Разработано в соответствии с корпоративным API Design Guide


## 3. Способы запуска
### Docker:
cd microservice-user-management/
docker build -t user-management-service .
docker-compose up microservice-user-management либо docker run -d --name user -p 8082:8080 user-management-service

### Локально:
./gradlew :microservice-user-management:run 

### Используемые переменные окружения:
USER_DB_LOGIN=your_db_login
USER_DB_PASSWORD=your_db_password


## 4. API документация:

При запущенном MicroserviceUsersApplication: http://localhost:8082/swagger-ui.html

Основные конечные точки:


## 5. Тестирование

Запуск тестов: ./gradlew :microservice-user-management:check 

### Используемый стек:
- JUnit 5
- Mockito
- Jupiter


## 6. Контакты

Автор микросервиса в Telegram: @eximun
