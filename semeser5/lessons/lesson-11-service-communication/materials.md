# Lesson 11: Service Communication *(Optional)*

## Prerequisites

- **Completed:** Lessons 1-10
- **Required knowledge:** HTTP clients, async programming concepts
- **Tools:** RabbitMQ (via Docker), external API access

---

## Learning Objectives

Upon completion of this lesson, students will be familiar with:
- HTTP communication between services using RestClient
- Using RabbitMQ for asynchronous messaging
- Understand observability basics with OpenTelemetry

## Topics Covered

### 1. REST Client for Service Communication
- `RestClient` for HTTP calls between services
- Basic error handling
- Service-to-service communication patterns

### 2. Messaging with RabbitMQ
- Message queues and exchanges
- Producers and consumers
- Spring AMQP integration

### 3. Observability Basics
- Logging, metrics and tracing concepts
- Introduction to OpenTelemetry

## Materials

1. [Spring RestClient Reference](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-restclient)
2. [Baeldung: Spring RestClient](https://www.baeldung.com/spring-boot-restclient)
3. [RabbitMQ Official Tutorials](https://www.rabbitmq.com/tutorials)
4. [CloudAMQP: RabbitMQ for Beginners](https://www.cloudamqp.com/blog/part1-rabbitmq-for-beginners-what-is-rabbitmq.html)
5. [Spring AMQP Reference](https://docs.spring.io/spring-amqp/reference/)

---

## Common Mistakes to Avoid

- **Not handling REST client errors** — external services can fail, always handle exceptions
- **Message not acknowledged** — unacked messages stay in queue, causing duplicates
- **RabbitMQ connection refused** — check if RabbitMQ is running and port is correct
- **Blocking in async context** — don't block message consumers with long operations

---

## Self-Check Questions

1. When would you use synchronous REST calls vs asynchronous messaging?
2. What is a message queue and why is it useful?
3. How does RabbitMQ routing work (exchanges, queues, bindings)?
4. What happens if a message consumer fails?
5. What are the benefits of observability in distributed systems?
