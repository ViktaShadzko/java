# Task 4: Containerize Application with Docker

## Objective

Run application in Docker with health monitoring capabilities.

## Tasks

1. Add Spring Boot Actuator dependency to the project
2. Configure actuator endpoints in `application.yml`
3. Create a multi-stage `Dockerfile` for the application
4. Build and run the application in a Docker container
5. Verify the application is accessible from the container, including main functionality and actuator health endpoint

## Deliverables

- `Dockerfile` with multi-stage build
- Actuator configuration exposing health endpoints
- Application runs successfully in Docker container

## Acceptance Criteria

- Docker image builds successfully
- Application starts inside the container without errors
- Health endpoint (`/actuator/health`) returns status `UP`
- Application functionality is accessible from outside the container

## Extra Tasks

- Configure and run multi-container setup (application + database) using Docker Compose
- Add health checks to Docker Compose configuration

---

## Grading (10 points)

| Criteria | Points |
|----------|--------|
| Actuator dependency and configuration | 2 |
| Health endpoint works | 2 |
| Multi-stage Dockerfile | 2 |
| Docker image builds successfully | 2 |
| Application accessible from container | 2 |
| Extra tasks (bonus) | +2 |
