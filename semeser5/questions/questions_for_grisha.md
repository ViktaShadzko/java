# Вопросы для собеседования Junior → Middle

## Java

### Основы и ООП
- Раскажи проо принципы ООП
- Объясните разницу между `==` и `equals()`. Когда использовать каждый из них?
- Что такое контракт между `equals()` и `hashCode()`? Почему важно переопределять оба метода?+
- Расскажите про иммутабельность объектов. Как создать immutable класс?
- В чем разница между абстрактным классом и интерфейсом? Когда использовать каждый?
- Объясните принципы SOLID на примерах.
- Что такое полиморфизм? Приведите примеры.
- Раскажи про новые фичи джавы которые ты юзал в проектах

### Collections Framework
- Объясните внутреннее устройство `HashMap`. Как обрабатываются коллизии?
- Объясните иерархию коллекций в Java.
- Что такое fail-fast и fail-safe итераторы?
- В чем разница между `Comparable` и `Comparator`?
- Изменения колекцой с 8 джавы 
- 
### Stream API и Functional Programming
- Объясните разницу между intermediate и terminal операциями в Stream API.
- Что такое Optional? Зачем он нужен?
- В чем разница между `map()` и `flatMap()` и `forEach()`?
- Что такое функциональный интерфейс?

### Spring Framework
- Объясните жизненный цикл Spring Bean.
- В чем разница между `@Component`, `@Service`, `@Repository`, `@Controller`?
- Что такое Dependency Injection? Какие типы DI поддерживает Spring?
- Объясните разницу между `@Bean` и `@Component`.
- Что такое Spring Profiles? Для чего используются?
- Расскажите про транзакции в Spring (`@Transactional`).
- Объясните scope бинов: singleton, prototype, request, session.

### Spring Boot
- Что такое auto-configuration в Spring Boot?
- Что такое `@ConfigurationProperties`?

### REST API
- Объясните разницу между REST и SOAP.
- В чем разница между PUT и PATCH?
- Что такое идемпотентность? Какие методы идемпотентны?
- Что такое HATEOAS?
- Как обеспечить версионирование API?

### JPA/Hibernate
- Объясните разницу между Lazy и Eager loading.
- Что такое N+1 проблема? Как ее решить?
- Расскажите про уровни изоляции транзакций.

### Testing
- В чем разница между unit, integration и e2e тестами?
- Что такое Mockito? Для чего используется?
- Объясните разницу между `@Mock` и `@InjectMocks`.
- Что такое TDD?
- Как тестировать Spring Boot приложения?

## Cloud (AWS/Azure/GCP)

### Основы Cloud Computing
- Объясните разницу между IaaS, PaaS, SaaS.
- Что такое горизонтальное и вертикальное масштабирование?
- Расскажите про CAP теорему.
- Что такое eventual consistency?

### Контейнеризация
- Объясните разницу между Docker и виртуальной машиной.
- Что такое Docker Image и Docker Container?
- Для чего нужен `docker-compose`?
- Объясните multi-stage builds в Docker.

### Microservices
- Объясните архитектуру микросервисов. Плюсы и минусы.
- Что такое Service Discovery?
- Расскажите про паттерны: API Gateway, Circuit Breaker, Saga.
- Как организовать communication между микросервисами? (REST, gRPC, Message Queues)
- Что такое distributed tracing?
- Как обрабатывать distributed transactions?

### CI/CD
- Что такое CI/CD pipeline?
- Опишите этапы типичного pipeline (build, test, deploy).
- Какие инструменты CI/CD вы знаете? (Jenkins, GitLab CI, GitHub Actions)
- Как деплоить новую функциональность?
- Что такое Blue-Green deployment?
- Что такое Canary deployment?

### Cloud Services
- Что такое S3? Для чего используется?
- Объясните разницу между реляционными и NoSQL базами данных.
- Что такое Load Balancer? Типы балансировщиков.
- Расскажите про Message Queues (SQS, RabbitMQ, Kafka).
- Что такое CDN? Зачем нужен?
- Что такое Lambda functions (serverless)?
- Как дебажить лямбда функции?

### Monitoring & Logging
- Какие инструменты мониторинга вы знаете? (Prometheus, Grafana, ELK)
- Что такое distributed logging?
- Какие метрики важно отслеживать в production?

### Messages & Async Communication
- Объясните разницу между синхронной и асинхронной коммуникацией.
- Что такое Message Queue? Для чего используется?
- Расскажите про Kafka. Основные компоненты (Producer, Consumer, Topic, Partition).
- В чем разница между Kafka и RabbitMQ?
- Что такое Event-Driven Architecture?
- Объясните паттерн Publisher-Subscriber.
- Что такое Dead Letter Queue?
- Как обеспечить гарантию доставки сообщений (at-least-once, at-most-once, exactly-once)?
- Что такое idempotent consumer?
- Как обрабатывать failed messages?
- Расскажите про back pressure в системах с очередями.
- Что такое message ordering? Как его обеспечить?
- В чем разница между queue и topic?
- Что такое consumer group в Kafka?
- Как масштабировать обработку сообщений?

## Общие вопросы

### Архитектура и Design Patterns
- Расскажите про паттерны: Singleton, Factory, Builder, Observer, Strategy.
- Что такое SOLID принципы?
- Что такое DRY, KISS, YAGNI?
- Объясните разницу между монолитом и микросервисами.

### Базы данных
- Что такое индексы? Как они работают?
- Объясните нормализацию БД.
- Что такое транзакции ACID?
- В чем разница между LEFT JOIN и INNER JOIN?

### Git
- Объясните разницу между merge и rebase.
- Что такое Git flow?
- Как откатить изменения?
- Что такое cherry-pick?

### Каверзные вопросы
- Что такое memory leak в Java? Как его избежать?
- Объясните работу Garbage Collector в Java.
- Что есть JVM, JRE, JDK?
- Что будет если положить мапу в мапу


### Задачи на программирование
- Найти в строке каких букв больше всего