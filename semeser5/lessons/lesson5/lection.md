# Лекция 5: Введение в Spring Boot

## Содержание
1. [Что такое Spring Boot?](#что-такое-spring-boot)
2. [Автоконфигурация (Autoconfiguration)](#автоконфигурация-autoconfiguration)
3. [Spring Boot Starters](#spring-boot-starters)
4. [Что мы получаем в дефолтном Spring Boot веб приложении](#что-мы-получаем-в-дефолтном-spring-boot-веб-приложении)
5. [Как работают стартеры](#как-работают-стартеры)
6. [Практические примеры](#практические-примеры)

---

## Что такое Spring Boot?

**Spring Boot** — это фреймворк, построенный поверх Spring Framework, который значительно упрощает создание готовых к production приложений.

### Основные преимущества:
- ⚡ **Быстрый старт** — минимальная конфигурация
- 🔧 **Автоконфигурация** — автоматическая настройка компонентов
- 📦 **Встроенный сервер** — Tomcat, Jetty или Undertow
- 🎯 **Production-ready** — метрики, health checks из коробки
- 📚 **Стартеры** — готовые наборы зависимостей

### Философия Spring Boot:
```
Convention over Configuration (Конфигурация по соглашению)
```

Вместо того чтобы настраивать каждый компонент вручную, Spring Boot предоставляет разумные настройки по умолчанию.

---

## Автоконфигурация (Autoconfiguration)

### Как это работает?

Автоконфигурация — это "магия" Spring Boot, которая автоматически настраивает ваше приложение на основе:
1. **Classpath** — какие библиотеки присутствуют
2. **Properties/YAML** — ваши настройки
3. **Условия** — определённые условия выполнения

### Механизм автоконфигурации

```java
@SpringBootApplication
public class CoffeeApplication {
    public static void main(String[] args) {
        SpringApplication.run(CoffeeApplication.class, args);
    }
}
```

`@SpringBootApplication` — это комбинация трёх аннотаций:

```java
@SpringBootConfiguration  // Указывает, что это конфигурационный класс
@EnableAutoConfiguration  // Включает автоконфигурацию
@ComponentScan           // Сканирует компоненты в пакете и подпакетах
```

### @EnableAutoConfiguration — Ключ к автоконфигурации

Когда Spring Boot запускается:

1. **Сканирует classpath** — проверяет, какие библиотеки доступны
2. **Читает META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports**
3. **Загружает классы конфигурации** на основе условий

### Условные аннотации (@Conditional)

Spring Boot использует условные аннотации для включения/выключения конфигураций:

```java
@Configuration
@ConditionalOnClass(DataSource.class)  // Только если DataSource в classpath
public class DataSourceAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean  // Только если бин ещё не создан
    public DataSource dataSource() {
        return new HikariDataSource();
    }
}
```

### Основные условные аннотации:

| Аннотация | Описание |
|-----------|----------|
| `@ConditionalOnClass` | Активируется, если класс присутствует в classpath |
| `@ConditionalOnMissingClass` | Активируется, если класса НЕТ в classpath |
| `@ConditionalOnBean` | Активируется, если бин существует |
| `@ConditionalOnMissingBean` | Активируется, если бина НЕТ |
| `@ConditionalOnProperty` | Активируется, если свойство установлено |
| `@ConditionalOnWebApplication` | Активируется для веб-приложений |

### Пример автоконфигурации Jackson

Когда вы добавляете Jackson в classpath, Spring Boot автоматически:

```java
@Configuration
@ConditionalOnClass(ObjectMapper.class)
public class JacksonAutoConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Настройки по умолчанию
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        return mapper;
    }
}
```

### Как переопределить автоконфигурацию?

Просто создайте свой бин:

```java
@Configuration
public class MyConfig {
    
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        // Ваши настройки
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        return mapper;
    }
}
```

Spring Boot увидит ваш бин и НЕ создаст свой (благодаря `@ConditionalOnMissingBean`).

---

## Spring Boot Starters

### Что такое Starter?

**Starter** — это удобный дескриптор зависимостей. Вместо того чтобы добавлять десятки зависимостей вручную, вы добавляете один стартер.

### Пример: До и После

#### ❌ Без Spring Boot (много зависимостей):

```xml
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-web</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework</groupId>
    <artifactId>spring-webmvc</artifactId>
</dependency>
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
</dependency>
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
</dependency>
<dependency>
    <groupId>org.apache.tomcat.embed</groupId>
    <artifactId>tomcat-embed-core</artifactId>
</dependency>
<!-- ... ещё 10-15 зависимостей ... -->
```

#### ✅ С Spring Boot (один стартер):

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

### Основные стартеры

| Starter | Назначение | Что включает |
|---------|-----------|--------------|
| `spring-boot-starter-web` | Веб-приложения | Spring MVC, Tomcat, Jackson, Validation |
| `spring-boot-starter-data-jpa` | JPA/Hibernate | Spring Data JPA, Hibernate, JDBC |
| `spring-boot-starter-data-jdbc` | JDBC без ORM | Spring Data JDBC, HikariCP |
| `spring-boot-starter-security` | Безопасность | Spring Security |
| `spring-boot-starter-test` | Тестирование | JUnit, Mockito, AssertJ, Spring Test |
| `spring-boot-starter-actuator` | Мониторинг | Метрики, health checks, endpoints |
| `spring-boot-starter-validation` | Валидация | Hibernate Validator (JSR-380) |
| `spring-boot-starter-thymeleaf` | Шаблонизатор | Thymeleaf |
| `spring-boot-starter-logging` | Логирование | Logback, SLF4J (включен автоматически) |

### Транзитивные зависимости

Каждый стартер тянет за собой набор зависимостей:

```
spring-boot-starter-web
├── spring-boot-starter
│   ├── spring-boot
│   ├── spring-boot-autoconfigure
│   └── spring-boot-starter-logging
├── spring-boot-starter-json
│   └── jackson-databind
├── spring-boot-starter-tomcat
│   └── tomcat-embed-core
├── spring-web
└── spring-webmvc
```

---

## Что мы получаем в дефолтном Spring Boot веб приложении

### 1. Встроенный Tomcat Server

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

**Автоматически получаем:**
- ✅ Tomcat embedded сервер (порт 8080 по умолчанию)
- ✅ Автоматическая настройка сервлетов
- ✅ Возможность запуска как обычного Java приложения

```bash
java -jar myapp.jar
```

### 2. DispatcherServlet

**Автоматически настроен:**
- Обрабатывает все HTTP запросы
- Маршрутизирует запросы к контроллерам
- Настроен на паттерн `/*`

### 3. Jackson для JSON

**Автоматически включает:**
- Сериализация/десериализация объектов в/из JSON
- Настроен ObjectMapper с разумными дефолтами
- Автоматическое преобразование в `@RestController`

```java
@RestController
public class CoffeeController {
    
    @GetMapping("/api/coffee")
    public Coffee getCoffee() {
        return new Coffee("Latte", 3.50);  // Автоматически → JSON
    }
}
```

### 4. Validation (JSR-380)

При добавлении `spring-boot-starter-validation`:

```java
@RestController
public class BeverageController {
    
    @PostMapping("/api/beverages")
    public Beverage create(@Valid @RequestBody Beverage beverage) {
        // Автоматическая валидация
        return beverageService.save(beverage);
    }
}

public class Beverage {
    @NotBlank(message = "Name is required")
    private String name;
    
    @Positive(message = "Price must be positive")
    private double price;
}
```

### 5. Exception Handling

Автоматическая обработка ошибок:
- 404 для несуществующих endpoints
- 500 для внутренних ошибок
- 400 для ошибок валидации

Можно переопределить через `@ControllerAdvice`:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex) {
        // Ваша логика
        return ResponseEntity.badRequest().body(errorResponse);
    }
}
```

### 6. Static Resources

Автоматическое обслуживание статических файлов из:
- `/static`
- `/public`
- `/resources`
- `/META-INF/resources`

```
src/main/resources/
├── static/
│   ├── css/
│   ├── js/
│   └── images/
└── application.properties
```

### 7. Application Properties

Автоматическое чтение конфигурации:

```properties
# application.properties
server.port=9090
spring.application.name=coffee-app
logging.level.ehu.java=DEBUG
```

Или YAML:

```yaml
# application.yml
server:
  port: 9090
spring:
  application:
    name: coffee-app
logging:
  level:
    ehu.java: DEBUG
```

### 8. Actuator Endpoints

При добавлении `spring-boot-starter-actuator`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

**Получаем endpoints:**
- `/actuator/health` — статус приложения
- `/actuator/info` — информация о приложении
- `/actuator/metrics` — метрики
- `/actuator/env` — переменные окружения

### 9. DevTools (для разработки)

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
</dependency>
```

**Дает:**
- 🔄 Автоматический перезапуск при изменении кода
- 🚀 LiveReload для браузера
- ⚡ Кэширование отключено для разработки

### 10. Logging

**Из коробки настроено:**
- Logback как реализация
- SLF4J как фасад
- Цветной вывод в консоль
- Ротация логов

```java
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CoffeeService {
    
    public void makeCoffee() {
        log.info("Making coffee...");
        log.debug("Temperature: 92°C");
        log.error("Out of milk!");
    }
}
```

---

## Как работают стартеры

### Анатомия стартера

Стартер — это обычная Maven/Gradle зависимость, которая:

1. **Объявляет транзитивные зависимости**
2. **Не содержит код** (только `pom.xml` или `build.gradle`)
3. **Связан с autoconfigure модулем**

### Структура стартера

```
spring-boot-starter-web/
└── pom.xml  (только зависимости)

spring-boot-autoconfigure/
└── src/main/java/
    └── org/springframework/boot/autoconfigure/web/
        ├── WebMvcAutoConfiguration.java
        ├── HttpMessageConvertersAutoConfiguration.java
        └── ...
```

### Пример: spring-boot-starter-web

#### pom.xml стартера:

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-json</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-tomcat</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
    </dependency>
</dependencies>
```

### Процесс работы стартера

```
1. Добавляете starter в pom.xml
           ↓
2. Maven скачивает все транзитивные зависимости
           ↓
3. При запуске приложения @EnableAutoConfiguration 
   сканирует classpath
           ↓
4. Находит классы автоконфигурации
   (например, WebMvcAutoConfiguration)
           ↓
5. Проверяет условия (@ConditionalOnClass и т.д.)
           ↓
6. Если условия выполнены — создаёт бины
           ↓
7. Приложение готово к работе!
```

### Пример автоконфигурации для Web MVC

```java
@Configuration
@ConditionalOnWebApplication(type = Type.SERVLET)
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class })
@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
@AutoConfiguration
public class WebMvcAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public InternalResourceViewResolver defaultViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }
    
    @Bean
    public FormattingConversionService mvcConversionService() {
        DefaultFormattingConversionService conversionService = 
            new DefaultFormattingConversionService();
        addFormatters(conversionService);
        return conversionService;
    }
}
```

### Проверка автоконфигурации

Чтобы увидеть, что было автоматически настроено:

```bash
java -jar myapp.jar --debug
```

Или в `application.properties`:

```properties
debug=true
```

Вывод покажет:
- ✅ Positive matches (что было настроено)
- ❌ Negative matches (что НЕ было настроено и почему)

---

## Практические примеры

### Пример 1: Создание Coffee REST API

#### 1. Зависимости (pom.xml):

```xml
<dependencies>
    <!-- Всё для REST API -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- Lombok для удобства -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    
    <!-- Валидация -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>
</dependencies>
```

#### 2. Entity:

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Beverage {
    private Long id;
    
    @NotBlank(message = "Name is required")
    private String name;
    
    @Positive(message = "Price must be positive")
    private double price;
    
    private String description;
}
```

#### 3. Service:

```java
@Slf4j
@Service
public class CoffeeService {
    private final BeverageRepository repository;
    
    public CoffeeService(BeverageRepository repository) {
        this.repository = repository;
    }
    
    public List<Beverage> getAllBeverages() {
        log.info("Fetching all beverages");
        return repository.getAllBeverages();
    }
    
    public Beverage getBeverageById(long id) {
        log.info("Fetching beverage with ID: {}", id);
        return repository.getBeverageById(id);
    }
}
```

#### 4. Controller:

```java
@Slf4j
@RestController
@RequestMapping("/api/beverages")
public class BeverageController {
    
    private final CoffeeService coffeeService;
    
    @GetMapping
    public ResponseEntity<List<Beverage>> getAllBeverages() {
        log.info("GET /api/beverages");
        return ResponseEntity.ok(coffeeService.getAllBeverages());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Beverage> getBeverageById(@PathVariable long id) {
        log.info("GET /api/beverages/{}", id);
        Beverage beverage = coffeeService.getBeverageById(id);
        
        return beverage != null 
            ? ResponseEntity.ok(beverage)
            : ResponseEntity.notFound().build();
    }
    
    @PostMapping
    public ResponseEntity<Beverage> createBeverage(
            @Valid @RequestBody Beverage beverage) {
        log.info("POST /api/beverages: {}", beverage);
        // Автоматическая валидация и JSON парсинг!
        return ResponseEntity.status(HttpStatus.CREATED).body(beverage);
    }
}
```

#### 5. Global Exception Handler:

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {
        
        String errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(FieldError::getDefaultMessage)
            .collect(Collectors.joining(", "));
        
        ErrorResponse response = new ErrorResponse(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Validation Error",
            errors,
            request.getRequestURI()
        );
        
        return ResponseEntity.badRequest().body(response);
    }
}
```

#### 6. Configuration:

```properties
# application.properties
server.port=8080
spring.application.name=coffee-app

# Logging
logging.level.ehu.java=DEBUG
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} - %msg%n

# Actuator
management.endpoints.web.exposure.include=health,info
```

### Что Spring Boot сделал автоматически?

✅ **Настроил Tomcat** на порту 8080  
✅ **Создал DispatcherServlet**  
✅ **Настроил Jackson** для JSON  
✅ **Включил валидацию** с Hibernate Validator  
✅ **Настроил обработку исключений**  
✅ **Создал ObjectMapper** для сериализации  
✅ **Настроил логирование** с Logback  
✅ **Включил actuator endpoints**  

### Пример 2: Переопределение автоконфигурации

Если нужно изменить настройки Jackson:

```java
@Configuration
public class JacksonConfig {
    
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        
        // Игнорировать null поля
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        
        // Даты в ISO-8601
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        
        // Красивое форматирование
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        
        return mapper;
    }
}
```

Spring Boot увидит ваш `ObjectMapper` и не создаст свой!

---

## Итоги

### Spring Boot даёт нам:

1. **Автоконфигурацию** — умная настройка на основе classpath
2. **Стартеры** — удобные наборы зависимостей
3. **Встроенный сервер** — не нужен внешний Tomcat
4. **Production-ready** функции — метрики, health checks
5. **Минимум кода** — больше бизнес-логики, меньше boilerplate

### Принципы работы:

- 🎯 **Convention over Configuration** — разумные дефолты
- 🔧 **Условная конфигурация** — только то, что нужно
- 📦 **Модульность** — используй только нужные стартеры
- 🔄 **Переопределяемость** — легко изменить любую настройку

### Для веб-приложения нужен всего один стартер:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

И вы получаете: Tomcat + Spring MVC + Jackson + Validation + Logging + многое другое!

---

## Полезные ссылки

- [Spring Boot Reference Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Boot Starters List](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.build-systems.starters)
- [Spring Boot Autoconfiguration Classes](https://docs.spring.io/spring-boot/docs/current/reference/html/auto-configuration-classes.html)
- [Common Application Properties](https://docs.spring.io/spring-boot/docs/current/reference/html/application-properties.html)

---

## Вопросы для самопроверки

1. Что такое автоконфигурация и как она работает?
2. Какие три аннотации включает `@SpringBootApplication`?
3. Что такое стартер и зачем он нужен?
4. Какие компоненты включает `spring-boot-starter-web`?
5. Как переопределить автоматическую конфигурацию?
6. Что делает аннотация `@ConditionalOnMissingBean`?
7. Какие endpoints предоставляет Spring Boot Actuator?
8. Как посмотреть, какая автоконфигурация была применена?

