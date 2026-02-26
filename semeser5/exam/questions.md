# Экзаменационные билеты - Spring Framework

## Билет 1

1. **Spring Core**: Что такое Spring Bean? Какие способы создания бинов вы знаете?

2. **Spring Boot**: Что такое Spring Boot и какие преимущества он предоставляет по сравнению с обычным Spring?

3. **JDBC**: Что такое JdbcTemplate и зачем он нужен? Приведите пример простого запроса.


---

## Билет 2

1. **Bean Scopes**: Какие scope бинов вы знаете? Объясните разницу между singleton и prototype.

2. **Spring Web**: Что такое @RestController и чем он отличается от @Controller?

3. **JPA**: Что такое Entity в JPA? Какие обязательные аннотации нужны для создания сущности?
---

## Билет 3

1. **Dependency Injection**: Что такое Dependency Injection (внедрение зависимостей)? Какие способы DI есть в Spring?

2. **Spring Boot**: Что делает аннотация @SpringBootApplication? Какие аннотации она в себя включает?

3. **HTTP**: Что такое HTTP протокол? Объясните разницу между GET и POST запросами.

---

## Билет 4

1. **Spring Data JPA**: Что такое Repository в Spring Data JPA? Какие базовые методы предоставляет JpaRepository?

2. **Servlet**: Что такое servlet? Объясните жизненный цикл servlet (init, service, destroy).

3. **Spring Web**: Какие HTTP методы вы знаете? Какие Spring аннотации используются для их маппинга?
---

## Билет 5

1. **Configuration**: Что такое @Configuration и @Bean аннотации? Для чего они используются?

2. **Spring Boot**: Что такое application.properties/application.yml? Приведите примеры настроек для базы данных.

3. **JDBC vs JPA**: В чем основное отличие между JDBC и JPA? Когда лучше использовать каждый из них?

---

## Билет 6

1. **Component Scan**: Что такое @Component, @Service, @Repository, @ComponentScan? В чем разница между этими аннотациями?

2. **JPA Relations**: Какие типы связей между сущностями вы знаете в JPA? Объясните @OneToMany и @ManyToOne.

3. **Spring Web**: Что такое @PathVariable и @RequestParam? В чем их отличие?

---

## Билет 7

1. **Spring Boot Auto-configuration**: Что такое автоконфигурация в Spring Boot? Как она работает?

2. **Hibernate**: Что такое lazy loading и eager loading? В чем разница?

3. **Transaction Management**: Что делает аннотация @Transactional? Зачем нужны транзакции?

---

## Билет 8

1. **@Autowired**: Что делает аннотация @Autowired? Где её можно применять (конструктор, поле, setter)?

2. **Spring Boot Starter**: Что такое Spring Boot Starter? Назовите несколько популярных стартеров.

3. **JDBC Template**: Какие методы JdbcTemplate вы знаете? Объясните разницу между query и update.

---

## Билет 9

1. **ApplicationContext**: Что такое ApplicationContext в Spring? Чем он отличается от BeanFactory?

2. **Exception Handling**: Как обрабатывать исключения в Spring REST контроллерах? Что такое @ExceptionHandler?

3. **JPA Entity States**: Какие состояния может иметь Entity в JPA (transient, managed, detached, removed)?

---

## Билет 10

1. **Spring Annotations**: Объясните назначение аннотаций @RequestBody и @ResponseBody. Когда они используются?

2. **JPA Repository**: Что такое CrudRepository? Назовите 5 основных методов, которые он предоставляет.

3. **HTTP Status Codes**: Какие HTTP коды ответа вы знаете? Что означают коды 200, 201, 400, 404, 500?

---

## Подсказки для подготовки:

### Spring Core & Bean:
- Изучите способы создания бинов: @Component, @Bean, XML
- Поймите разницу между singleton, prototype, request, session scopes
- Разберитесь с dependency injection через конструктор, setter, field

### Spring Boot:
- @SpringBootApplication = @Configuration + @EnableAutoConfiguration + @ComponentScan
- application.properties для конфигурации
- Spring Boot Starters (spring-boot-starter-web, spring-boot-starter-data-jpa)

### Spring Web:
- @RestController, @RequestMapping, @GetMapping, @PostMapping, @PutMapping, @DeleteMapping
- @PathVariable, @RequestParam, @RequestBody
- @ExceptionHandler, @ControllerAdvice

### JDBC & JPA:
- JdbcTemplate для работы с JDBC
- @Entity, @Id, @GeneratedValue для JPA
- @OneToMany, @ManyToOne, @ManyToMany для связей
- JpaRepository с методами save(), findById(), findAll(), delete()

### Hibernate:
- Session, SessionFactory
- Lazy loading vs Eager loading
- Entity states: transient, persistent, detached, removed

### Transaction:
- @Transactional для управления транзакциями
- ACID принципы
- Isolation levels

