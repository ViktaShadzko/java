# Troubleshooting: Docker and Actuator

## Docker Build Fails

**Error:**
```
COPY failed: file not found in build context
```

**Check:**
1. File path is relative to Dockerfile location
2. File is not in `.dockerignore`
3. Build context includes the file: `docker build -t myapp .`

---

## Port Already in Use

**Error:**
```
Bind for 0.0.0.0:8080 failed: port is already allocated
```

**Solutions:**
1. Stop the other container/process using the port
2. Use a different port: `docker run -p 8081:8080 myapp`
3. Find what's using the port: `lsof -i :8080` (Mac/Linux)

---

## Container Can't Connect to Database

**Problem:** Application in container can't reach database on host.

**Solutions:**

1. **For host database:**
```yaml
# application.yml
spring:
  datasource:
    url: jdbc:postgresql://host.docker.internal:5432/mydb
```

2. **For database in another container:**
Use Docker network:
```bash
docker network create mynetwork
docker run --network mynetwork --name db postgres
docker run --network mynetwork -e DB_HOST=db myapp
```

3. **With Docker Compose:**
```yaml
services:
  app:
    depends_on:
      - db
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/mydb
  db:
    image: postgres
```

---

## Application Crashes on Startup

**Debug steps:**

1. Check logs:
```bash
docker logs <container_id>
docker logs -f <container_id>  # Follow logs
```

2. Run interactively:
```bash
docker run -it myapp /bin/sh
```

3. Check environment variables:
```bash
docker exec <container_id> env
```

---

## Actuator Endpoints Not Accessible

**Problem:** `/actuator/health` returns 404.

**Check application.yml:**
```yaml
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics  # Or use '*' for all
  endpoint:
    health:
      show-details: always
```

**Check dependency:**
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

---

## Large Docker Image

**Problem:** Image is several hundred MB or more.

**Solution:** Use multi-stage build:
```dockerfile
# Build stage
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

---

## Health Check Failing

**Problem:** Container marked unhealthy in Docker Compose.

**Check:**
1. Application has time to start (use `start_period`)
2. Health endpoint is accessible
3. Correct port is used

```yaml
services:
  app:
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 40s  # Wait for app to start
```
