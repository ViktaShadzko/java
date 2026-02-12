## Вариант 0: Система управления казино

### Описание
Создайте Spring Web приложение для управления столами в казино.

### Сущности
1. **GameTable** (Игровой стол)
    - id (Long)
    - name (String) - название стола
    - gameType (Enum: POKER, BLACKJACK, ROULETTE, BACCARAT)
    - minBet (BigDecimal) - минимальная ставка
    - maxBet (BigDecimal) - максимальная ставка
    - isOccupied (Boolean) - занят ли стол
    - currentPlayers (Integer) - текущее количество игроков
    - maxPlayers (Integer) - максимальное количество игроков

### CRUD операции (GameTable)
- GET /api/tables - получить все столы
- GET /api/tables/{id} - получить стол по ID
- POST /api/tables - создать новый стол
- PUT /api/tables/{id} - обновить стол
- DELETE /api/tables/{id} - удалить стол
- GET /api/tables/available - получить свободные столы
- GET /api/tables/by-type/{gameType} - получить столы по типу игры

### Специальные операции
- POST /api/tables/{id}/occupy - занять стол (join игрока)
- POST /api/tables/{id}/release - освободить стол (leave игрока)
- POST /api/tables/{id}/play - сыграть в игру стола
    - Request: { "playerId": Long, "betAmount": BigDecimal }
    - Response: { "result": String, "winAmount": BigDecimal }

---
