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
BD_LOGIN=your_db_login
BD_PASSWORD=your_db_password


## 4. Тестирование

Запуск тестов: ./gradlew :microservice-analytics:check 

### Используемый стек:
- JUnit 5
- Mockito
- Jupiter


## 5. Контакты

Автор микросервиса в Telegram: @Snowflakes17


## 6. API документация:

При запущенном AnalyticsApplication: http://localhost:8084/swagger-ui.html

В остальных случаях:

```json
{
  "openapi": "3.0.1",
  "info": {
    "title": "API микросервиса аналитики",
    "description": "API для управления сбором и анализом данных об использовании окон в приложении Smart Windows",
    "version": "0.0"
  },
  "servers": [
    {
      "url": "http://localhost:8080",
      "description": "Generated server url"
    }
  ],
  "tags": [
    {
      "name": "Window Analytics API",
      "description": "API для работы с данными об использовании умных окон"
    }
  ],
  "paths": {
    "/api/v0/analytics/users": {
      "post": {
        "tags": [
          "Window Analytics API"
        ],
        "summary": "Создать нового пользователя",
        "operationId": "createUser",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/UserAnalyticsDTO"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "Пресет успешно создан",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/PresetAnalyticsDTO"
                }
              }
            }
          },
          "400": {
            "description": "Некорректные данные пресета",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/UserAnalyticsDTO"
                }
              }
            }
          }
        }
      }
    },
    "/api/v0/analytics/users/logins": {
      "post": {
        "tags": [
          "Window Analytics API"
        ],
        "summary": "Зарегистрировать вход пользователя с заданным ID в систему",
        "operationId": "recordUserLogin",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "type": "integer",
                "description": "ID пользователя в формате JSON",
                "format": "int64"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "Вход зарегистрирован",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/UserAnalyticsDTO"
                }
              }
            }
          },
          "400": {
            "description": "Некорректные данные!",
            "content": {
              "application/json": {
                "schema": {
                  "type": "string",
                  "format": "date-time"
                }
              }
            }
          }
        }
      }
    },
    "/api/v0/analytics/presets": {
      "post": {
        "tags": [
          "Window Analytics API"
        ],
        "summary": "Создать новый пресет",
        "operationId": "createPreset",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/PresetAnalyticsDTO"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "Пресет успешно создан",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/PresetAnalyticsDTO"
                }
              }
            }
          },
          "400": {
            "description": "Некорректные данные пресета",
            "content": {
              "application/json": {
                "schema": {
                  "type": "object"
                }
              }
            }
          }
        }
      }
    },
    "/api/v0/analytics/presets/downloads": {
      "post": {
        "tags": [
          "Window Analytics API"
        ],
        "summary": "Зарегистрировать загрузку пресета с заданным названием",
        "operationId": "recordPresetDownload",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "type": "string",
                "description": "Название пресета в формате JSON"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "Загрузка зарегистрирована",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/PresetAnalyticsDTO"
                }
              }
            }
          },
          "400": {
            "description": "Некорректные данные!",
            "content": {
              "application/json": {
                "schema": {
                  "type": "string",
                  "format": "date-time"
                }
              }
            }
          }
        }
      }
    },
    "/api/v0/analytics/users/get-user-by-id/{user_id}": {
      "get": {
        "tags": [
          "Window Analytics API"
        ],
        "summary": "Найти пользователя по его ID",
        "description": "Возвращает запись о пользователе по указанному ID",
        "operationId": "getUserById",
        "parameters": [
          {
            "name": "user_id",
            "in": "path",
            "description": "Идентификатор пользователя",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int64"
            },
            "example": 12345
          }
        ],
        "responses": {
          "404": {
            "description": "Пользователь не найден",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/UserAnalyticsDTO"
                }
              }
            }
          },
          "200": {
            "description": "Пользователь успешно получен",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/UserAnalyticsDTO"
                }
              }
            }
          }
        }
      }
    },
    "/api/v0/analytics/users/earliest-login": {
      "get": {
        "tags": [
          "Window Analytics API"
        ],
        "summary": "Получить пользователя с самым старым логином",
        "operationId": "getUserWithEarliestLogin",
        "responses": {
          "404": {
            "description": "Пользователи не найдены",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/UserAnalyticsDTO"
                }
              }
            }
          },
          "200": {
            "description": "Пользователь найден",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/UserAnalyticsDTO"
                }
              }
            }
          }
        }
      }
    },
    "/api/v0/analytics/presets/most-downloaded": {
      "get": {
        "tags": [
          "Window Analytics API"
        ],
        "summary": "Получить самый популярный пресет (по количеству скачиваний)",
        "operationId": "getMostDownloadedPreset",
        "responses": {
          "200": {
            "description": "Пресет найден",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/PresetAnalyticsDTO"
                }
              }
            }
          },
          "404": {
            "description": "Пресеты не найдены",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/PresetAnalyticsDTO"
                }
              }
            }
          }
        }
      }
    },
    "/api/v0/analytics/presets/get-preset-by-name/{preset_name}": {
      "get": {
        "tags": [
          "Window Analytics API"
        ],
        "summary": "Найти пресет по его названию",
        "description": "Возвращает пресет по указанному названию",
        "operationId": "getPresetByName",
        "parameters": [
          {
            "name": "preset_name",
            "in": "path",
            "description": "Название пресета",
            "required": true,
            "schema": {
              "type": "string"
            },
            "example": "Test_Preset"
          }
        ],
        "responses": {
          "404": {
            "description": "Пресет не найден",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/PresetAnalyticsDTO"
                }
              }
            }
          },
          "200": {
            "description": "Пресет успешно получен",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/PresetAnalyticsDTO"
                }
              }
            }
          }
        }
      }
    }
  },
  "components": {
    "schemas": {
      "UserAnalyticsDTO": {
        "type": "object",
        "properties": {
          "user_id": {
            "type": "integer",
            "description": "Уникальный идентификатор пользователя",
            "format": "int64",
            "example": 456
          },
          "login_count": {
            "type": "integer",
            "description": "Количество входов в систему",
            "format": "int32",
            "example": 5
          },
          "last_login": {
            "type": "string",
            "description": "Время последнего входа",
            "format": "date-time"
          },
          "timestamp": {
            "type": "string",
            "description": "Временная метка данных",
            "format": "date-time"
          }
        },
        "description": "Данные о пользователе"
      },
      "PresetAnalyticsDTO": {
        "type": "object",
        "properties": {
          "id": {
            "type": "integer",
            "description": "ID пресета",
            "format": "int64",
            "example": 12345
          },
          "downloads_number": {
            "type": "integer",
            "description": "Количество загрузок данного пресета",
            "format": "int64",
            "example": 456000
          },
          "preset_name": {
            "type": "string",
            "description": "Название пресета",
            "example": "Утренний режим"
          },
          "created_at": {
            "type": "string",
            "description": "Время создания пресета",
            "format": "date-time"
          }
        },
        "description": "Данные о пресете настроек окна, доступном для загрузки"
      }
    }
  }
}
```
