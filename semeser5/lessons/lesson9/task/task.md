# Lab 3

## Task
- добавть сервис хранящий user в паямти при помощи лист или мап.(Implement UserDetailsService)
- реализовать эндпоинты:
    - GET /login
    - POST /register
- при регистрации сохранять user в память
- при логине проверять есть ли такой user в памяти и возвращать 200 если есть JWT tocken
- использовать спринг секьюрити для защиты эндпоинта post curency от доступа без авторизации
## Дополнитьельные задания 
1. добавить эндпоинт refresh jwt token
2. добавить роль ADMIN и USER и защитить эндпоинт POST /curency и DELETE только для ADMIN

## Экстра задание
1. использовать AuthenticationEventPublisher
