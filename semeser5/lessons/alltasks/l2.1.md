# Lab 2: Part 1 — Spring Boot Migration

## Objective

Migrate an existing Spring Core application to Spring Boot, leveraging auto-configuration and simplified dependency management.

## Tasks

1. Convert the existing Spring application to use Spring Boot:
   - Add Spring Boot parent POM and starter dependencies
   - Create a main class annotated with `@SpringBootApplication`
2. Replace XML or Java-based configuration with Spring Boot auto-configuration where applicable
3. Create an `application.yml` (or `application.properties`) file for externalized configuration
4. Verify the application starts correctly using the embedded server

## Deliverables

- A working Spring Boot application with `@SpringBootApplication` entry point
- Externalized configuration in `application.yml` or `application.properties`

## Acceptance Criteria

- Application migrated to Spring Boot
- All existing functionality remains intact after migration
- Application starts without errors on the default (8080) or on a redefined port

---

## Grading (8 points)

| Criteria | Points |
|----------|--------|
| @SpringBootApplication main class | 2 |
| Spring Boot starter dependencies | 2 |
| application.yml/properties configuration | 2 |
| Auto-configuration replaces manual config | 2 |

## Bonus Points
Use @ConfigurationProperties anatation 
Use value anotation with SpEL expression more complicated then just a property
Create your starter 
Use actuator at least 2 endpoints  