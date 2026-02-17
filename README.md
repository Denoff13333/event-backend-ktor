# Event Backend (Ktor + Kotlin + Exposed + Koin + JWT)

- JDK 17+
- Gradle
- Docker 

2) (Опционально) поменять `jwt.secret` в `src/main/resources/application.yaml`.

3) Запустить сервер:
```bash
gradle run
```

Сервер стартует на `http://localhost:8080`.

## Запуск тестов (используется H2 in-memory)
```bash
gradle test
```

## Примеры запросов

### Регистрация
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","password":"secret123"}'
```

### Логин (получить JWT)
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","password":"secret123"}'
```

Ответ: `{"token":"..."}`

### Создать событие (нужен токен)
```bash
TOKEN="PASTE_TOKEN_HERE"

curl -X POST http://localhost:8080/events \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"title":"Meetup","description":"Ktor + Kotlin"}'
```

### Список событий (публичный)
```bash
curl http://localhost:8080/events
```

### Удалить событие (только владелец)
```bash
TOKEN="PASTE_TOKEN_HERE"
EVENT_ID=1

curl -X DELETE http://localhost:8080/events/$EVENT_ID \
  -H "Authorization: Bearer $TOKEN" -i
```

## Кастомный плагин
Каждый ответ содержит header `X-Response-Time-Ms`.



## Результаты 
<img width="1280" height="473" alt="image" src="https://github.com/user-attachments/assets/d9a2014d-3149-4a7d-be89-7fd46e206f79" />
<img width="946" height="301" alt="image" src="https://github.com/user-attachments/assets/6a37fb0a-47c0-42e6-87ec-088f2a428b96" />


