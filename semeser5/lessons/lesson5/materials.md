# Lesson 5: Spring Boot Fundamentals

## Prerequisites

- **Completed:** Lessons 1-4
- **Required knowledge:** Maven/Gradle basics, YAML syntax
- **Tools:** IDE with Spring Boot support

---

## Learning Objectives

Upon completion of this lesson, students will be able to:
- Understand Spring Boot's auto-configuration mechanism
- Configure applications using external properties and YAML files
- Work with Spring profiles for environment-specific configurations

## Topics Covered

### 1. Spring Boot Introduction
- Spring Boot starters and auto-configuration
- `@SpringBootApplication` annotation

### 2. Externalized Configuration
- `application.yml` / `application.properties` syntax and structure
- Property injection with `@Value`
- Type-safe configuration with `@ConfigurationProperties`

### 3. Profiles
- Profile-specific configuration files
- Activating profiles via CLI, environment variables, or configuration

## Materials

1. [Spring Boot Reference - Features](https://docs.spring.io/spring-boot/reference/features/index.html)
2. [Marco Behler: How Spring Boot Auto-Configuration Works](https://www.marcobehler.com/guides/spring-boot)
3. [Baeldung: @ConfigurationProperties Guide](https://www.baeldung.com/configuration-properties-in-spring-boot)
4. [Baeldung: @Value Annotation](https://www.baeldung.com/spring-value-annotation)
5. [Spring Boot Profiles](https://docs.spring.io/spring-boot/reference/features/profiles.html)

---

## Common Mistakes to Avoid

- **Wrong YAML indentation** — YAML is sensitive to spaces, use consistent indentation
- **Missing @EnableConfigurationProperties** — required when using @ConfigurationProperties
- **Profile not activated** — check `spring.profiles.active` in config or CLI
- **@Value with missing property** — application fails to start if property doesn't exist (use default values)

---

## Self-Check Questions

1. What does `@SpringBootApplication` combine?
2. What is the difference between `@Value` and `@ConfigurationProperties`?
3. How do you activate a specific profile?
4. What is auto-configuration and how does it work?
5. How do you override a property for a specific profile?
