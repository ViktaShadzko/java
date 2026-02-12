# Экзаменационные задания - Spring Boot Web Applications

## Вариант 1: Система управления кинотеатром

### Описание

Создайте Spring Web приложение для управления залами и сеансами в кинотеатре.

### Сущности

1. **CinemaHall** (Кинозал)
    - id (Long)
    - name (String) - название зала
    - hallType (Enum: STANDARD, IMAX, VIP, 3D)
    - totalSeats (Integer)
    - occupiedSeats (Integer)
    - pricePerTicket (BigDecimal)
    - isActive (Boolean)

2. **MovieSession** (Сеанс)
    - id (Long)
    - hallId (Long)
    - movieTitle (String)
    - startTime (LocalDateTime)
    - duration (Integer) - в минутах

### CRUD операции

- Полный CRUD для CinemaHall и MovieSession

### Специальные операции

- POST /api/halls/{id}/book - забронировать места
    - Request: { "seatsCount": Integer }
- POST /api/halls/{id}/cancel - отменить бронь
    - Request: { "seatsCount": Integer }
- POST /api/sessions/{id}/watch - "посмотреть" фильм (симуляция)
    - Response: { "Seans Wathers": Integer, "HallName": String }

---

## Вариант 2: Система управления боулинг-клубом

### Описание

Создайте Spring Web приложение для управления дорожками в боулинге.

### Сущности

1. **BowlingLane** (Дорожка)
    - id (Long)
    - laneNumber (Integer)
    - laneType (Enum: STANDARD, PROFESSIONAL, KIDS, VIP)
    - pricePerHour (BigDecimal)
    - isOccupied (Boolean)
    - maxPlayers (Integer)
    - hasAutomaticScoring (Boolean)

### CRUD операции

- Полный CRUD для BowlingLane

### Специальные операции

- POST /api/lanes/{id}/play-game - сыграть игру
    - Request: { "playerName": String }
    - Response: { "score": Integer, "strikes": Integer, "spares": Integer }

---

## Вариант 3: Система управления теннисными кортами

### Описание

Создайте Spring Web приложение для управления теннисными кортами.

### Сущности

1. **TennisCourt** (Теннисный корт)
    - id (Long)
    - courtNumber (Integer)
    - surface (Enum: CLAY, GRASS, HARD, CARPET)
    - isIndoor (Boolean)
    - hasLighting (Boolean)
    - pricePerHour (BigDecimal)
    - isOccupied (Boolean)
    - courtType (Enum: SINGLES, DOUBLES)

### CRUD операции

- Полный CRUD для TennisCourt

### Специальные операции

- POST /api/courts/{id}/reserve - зарезервировать корт
    - Request: { "playerName": String, "opponentName": String }
- POST /api/courts/{id}/release - освободить корт
- POST /api/courts/{id}/play-match - сыграть матч
    - Response: { "winner": String }

---

## Вариант 4: Система управления коворкингом

### Описание

Создайте Spring Web приложение для управления рабочими местами в коворкинге.

### Сущности

1. **Workspace** (Рабочее место)
    - id (Long)
    - workspaceNumber (Integer)
    - workspaceType (Enum: HOT_DESK, DEDICATED_DESK, PRIVATE_OFFICE, MEETING_ROOM)
    - capacity (Integer)
    - hasMonitor (Boolean)
    - hasWhiteboard (Boolean)
    - isOccupied (Boolean)

### CRUD операции

- Полный CRUD для Workspace

### Специальные операции

- POST /api/workspaces/{id}/checkin - зачекиниться на рабочее место
  Request: { "userId": Long}
- POST /api/workspaces/{id}/checkout - освободить место
    - GET /api/workspaces/{id}/stats - симуляция рабочей сессии
      Response: {
      "wasOccupiedBy":[userId1, userId2, ...]
      "currentOccupant": userId
      }

---

## Вариант 5: Система управления аквапарком

### Описание

Создайте Spring Web приложение для управления водными аттракционами в аквапарке.

### Сущности

1. **WaterAttraction** (Водный аттракцион)
    - id (Long)
    - name (String) - название аттракциона
    - attractionType (Enum: SLIDE, WAVE_POOL, LAZY_RIVER, KIDS_AREA, EXTREME_SLIDE)
    - minHeight (Integer) - минимальный рост в см
    - maxCapacity (Integer) - максимальная вместимость
    - currentVisitors (Integer) - текущее количество посетителей
    - safetyRating (Integer) - рейтинг безопасности от 1 до 5
    - isOperational (Boolean) - работает ли аттракцион
    - waterTemperature (Double) - температура воды в градусах

### CRUD операции

- Полный CRUD для WaterAttraction

### Специальные операции

- POST /api/attractions/{id}/enter - посетитель заходит на аттракцион
    - Request: { "visitorName": String, "visitorHeight": Integer, "hasSwimVest": Boolean }
    - Response: { "allowed": Boolean, "waitingTime": Integer, "queuePosition": Integer }
- POST /api/attractions/{id}/exit - посетитель покидает аттракцион
    - Request: { "visitorName": String }
    - Response: { "enjoymentRating": Integer, "timeSpent": Integer }
- PUT /api/attractions/{id}/adjust-temperature - изменить температуру воды
    - Request: { "newTemperature": Double }
- GET /api/attractions/popular - получить топ-3 самых популярных аттракционов
    - Response: List<{ "name": String, "visitorsToday": Integer }>
- POST /api/attractions/{id}/emergency-close - экстренное закрытие аттракциона
    - Response: { "evacuatedVisitors": Integer, "reason": String }

---

## Вариант 6: Система управления планетарием

### Описание

Создайте Spring Web приложение для управления сеансами и куполами в планетарии.

### Сущности

1. **PlanetariumDome** (Купол планетария)
    - id (Long)
    - domeName (String)
    - projectionTechnology (Enum: DIGITAL_4K, DIGITAL_8K, HYBRID, CLASSIC_OPTICAL)
    - seatingCapacity (Integer)
    - hasLaserShow (Boolean)
    - has3DCapability (Boolean)

2. **Show** (Шоу/Сеанс)
    - id (Long)
    - domeId (Long)
    - title (String)
    - theme (Enum: SOLAR_SYSTEM, DEEP_SPACE, CONSTELLATIONS, EDUCATIONAL, MUSIC_SHOW)
    - duration (Integer) - в минутах
    - ticketsSold (Integer)

### CRUD операции

- Полный CRUD для PlanetariumDome и Show

### Специальные операции

- POST /api/domes/{id}/start-show - начать показ
    - Request: { "showId": Long, "attendees": Integer }
    - Response: { "projectionQuality": String, "atmosphereLevel": Integer }
- GET /api/dome/{id}/shows - получить расписание всех шоу на день
    - Response: List<{ "time": String, "domeName": String, "title": String, "availableSeats": Integer }>

---

---

## Критерии оценки (для всех вариантов):

### Базовая часть (60 баллов):

- ✅ Правильная структура Spring Boot проекта (10 баллов)
- ✅ Корректная реализация всех CRUD операций (20 баллов)
- ✅ Правильное использование аннотаций (@RestController, @Service, @Repository) (10 баллов)
- ✅ Валидация входных данных (10 баллов)
- ✅ Обработка исключений (10 баллов)

### Продвинутая часть (40 баллов):

- ✅ Реализация всех специальных операций (20 баллов)
- ✅ Качество кода и соблюдение best practices (10 баллов)
- ✅ Логирование важных операций (5 баллов)
- ✅ Документация API (Swagger/OpenAPI) (5 баллов)

### Бонусы (до +10 баллов):

- ⭐ Дополнительная бизнес-логика (+3 балла)
- ⭐ Использование DTO и MapStruct/ModelMapper (+2 балла)

**Общее время на выполнение: 3-4 часа**

---

## Примеры эндпоинтов для тестирования (на примере Варианта 0 - Казино):

```bash
# Создать стол
POST http://localhost:8080/api/tables
{
  "name": "VIP Poker Table 1",
  "gameType": "POKER",
  "minBet": 100,
  "maxBet": 10000,
  "maxPlayers": 6
}

# Получить все столы
GET http://localhost:8080/api/tables

# Занять стол
POST http://localhost:8080/api/tables/1/occupy
{
    "playerId": 1
}

# Сыграть в игру
POST http://localhost:8080/api/tables/1/play
{
  "betAmount": 500
}
Response 200 OK
[{
  "winnerId": 1,
  "winningHand": "Flush",
  "payout": 1500
    
}]

# Освободить стол
POST http://localhost:8080/api/tables/1/release
{
    "playerId": 1
}
```

