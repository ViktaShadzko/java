# Lab 4: Service Communication with REST and RabbitMQ *(Extra)*

## Objective

Implement inter-service communication using REST clients for synchronous calls and RabbitMQ for asynchronous messaging.

## Tasks

1. Configure the application to connect to RabbitMQ
2. Implement a REST client to call an external API and process the response
3. Create a message producer that sends the processed response to a RabbitMQ queue
4. Implement a message consumer to read messages from the RabbitMQ queue and handle them appropriately

## Deliverables

- Configured RabbitMQ (can be run locally or via Docker)
- RabbitMQ configuration in the application
- REST client implementation for external API calls
- Message producer and consumer components

## Acceptance Criteria

- REST client successfully calls external API and processes response
- Messages are published to RabbitMQ queue
- Consumer receives and processes messages from the RabbitMQ queue

---

## Grading (10 points)

| Criteria | Points |
|----------|--------|
| RabbitMQ configured and running | 2 |
| REST client calls external API | 3 |
| Message producer works | 2 |
| Message consumer works | 3 |
