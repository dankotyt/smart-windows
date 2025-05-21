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

**Дизайн API:** Разработано в соответствии с API Design Guide


## 3. Способы запуска
### Docker:
cd microservice-presets/
docker build -t presets-service .
docker-compose up microservice-presets либо docker run -d --name presets -p 8082:8080 presets-service

### Локально:
./gradlew :microservice-presets:run 

### Используемые переменные окружения:
BD_LOGIN=your_db_login
BD_PASSWORD=your_db_password


## 4. Тестирование

Запуск тестов: ./gradlew :microservice-presets:check 

### Используемый стек:
- JUnit 5
- Mockito
- Jupiter


## 5. Контакты

Автор микросервиса в Telegram: @Snowflakes17


## 6. API документация:

При запущенном PresetsApplication доступна по адресу: http://localhost:8086/swagger-ui.html

В остальных случаях:

```json
{
  "openapi": "3.0.1",
  "info": {
    "title": "API микросервиса пресетов",
    "description": "API для управления пресетами настроек окон в приложении Smart Windows",
    "version": "0.0"
  },
  "servers": [
    {
      "url": "http://localhost:8086",
      "description": "Generated server url"
    }
  ],
  "tags": [
    {
      "name": "Preset Controller",
      "description": "API для управления пресетами настроек окна"
    }
  ],
  "paths": {
    "/api/presets/v0/update-by-id/{id}": {
      "put": {
        "tags": [
          "Preset Controller"
        ],
        "summary": "Обновить пресет",
        "description": "Обновляет и возвращает существующий пресет",
        "operationId": "updatePresetById",
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "description": "ID пресета",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int64"
            },
            "example": 1
          }
        ],
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/PresetDTO"
              }
            }
          },
          "required": true
        },
        "responses": {
          "200": {
            "description": "Пресет успешно обновлен",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/PresetDTO"
                }
              }
            }
          },
          "404": {
            "description": "Пресет не найден",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/PresetDTO"
                }
              }
            }
          },
          "400": {
            "description": "Некорректные данные",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/PresetDTO"
                }
              }
            }
          }
        }
      }
    },
    "/api/presets/v0/create": {
      "post": {
        "tags": [
          "Preset Controller"
        ],
        "summary": "Создать новый пресет",
        "description": "Создает и возвращает новый пресет",
        "operationId": "createPreset",
        "requestBody": {
          "content": {
            "application/json": {
              "schema": {
                "$ref": "#/components/schemas/PresetDTO"
              }
            }
          },
          "required": true
        },
        "responses": {
          "201": {
            "description": "Пресет успешно создан",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/PresetDTO"
                }
              }
            }
          },
          "400": {
            "description": "Некорректные данные",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/PresetDTO"
                }
              }
            }
          }
        }
      }
    },
    "/api/presets/v0/get-by-id/{id}": {
      "get": {
        "tags": [
          "Preset Controller"
        ],
        "summary": "Найти пресет по ID",
        "description": "Возвращает пресет по указанному ID",
        "operationId": "getPresetById",
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "description": "ID пресета",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int64"
            },
            "example": 1
          }
        ],
        "responses": {
          "404": {
            "description": "Пресет не найден",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/PresetDTO"
                }
              }
            }
          },
          "200": {
            "description": "Пресет успешно получен",
            "content": {
              "application/json": {
                "schema": {
                  "$ref": "#/components/schemas/PresetDTO"
                }
              }
            }
          }
        }
      }
    },
    "/api/presets/v0/delete-by-id/{id}": {
      "delete": {
        "tags": [
          "Preset Controller"
        ],
        "summary": "Удалить пресет",
        "description": "Удаляет пресет по указанному ID",
        "operationId": "deletePresetById",
        "parameters": [
          {
            "name": "id",
            "in": "path",
            "description": "ID пресета",
            "required": true,
            "schema": {
              "type": "integer",
              "format": "int64"
            },
            "example": 1
          }
        ],
        "responses": {
          "204": {
            "description": "Пресет успешно удален"
          },
          "404": {
            "description": "Пресет не найден"
          }
        }
      }
    }
  },
  "components": {
    "schemas": {
      "PresetDTO": {
        "required": [
          "darkness_value",
          "frame_color",
          "preset_name",
          "ventilation_flag",
          "ventilation_timer"
        ],
        "type": "object",
        "properties": {
          "id": {
            "type": "integer",
            "description": "Уникальный идентификатор",
            "format": "int64",
            "readOnly": true,
            "example": 1
          },
          "preset_name": {
            "type": "string",
            "description": "Название пресета",
            "example": "Стандартные настройки"
          },
          "ventilation_flag": {
            "type": "boolean",
            "description": "Флаг проветривания",
            "example": true
          },
          "ventilation_timer": {
            "maximum": 86400,
            "minimum": 0,
            "type": "integer",
            "description": "Время проветривания (секунды)",
            "format": "int32",
            "example": 600
          },
          "darkness_value": {
            "maximum": 100,
            "minimum": 0,
            "type": "integer",
            "description": "Уровень затемнения (0-100)",
            "format": "int32",
            "example": 75
          },
          "frame_color": {
            "type": "integer",
            "description": "Цвет подсветки (RGB)",
            "format": "int32",
            "example": 16777215
          }
        },
        "description": "Данные пресета в формате JSON"
      }
    }
  }
}
```
