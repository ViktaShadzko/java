# Lesson 9: HTTP Clients and Security

## Prerequisites

- **Completed:** Lessons 1-8
- **Required knowledge:** JWT basics, HTTP authentication headers
- **Tools:** Postman for testing secured endpoints

> **Note:** Students have basic JWT knowledge. This lesson extends it with Spring Security integration.

---

## Learning Objectives

Upon completion of this lesson, students will be able to:
- Make HTTP calls to external services using Spring's HTTP clients
- Implement JWT-based authentication
- Configure Spring Security for REST API protection
- Manage user authentication and authorization

## Topics Covered

### 1. HTTP Clients
- `RestTemplate` (legacy approach)
- `RestClient` (modern fluent API)
- Error handling and timeouts

### 2. Spring Security Fundamentals
- Security filter chain
- Path-based security configuration
- In-memory user management
- Password encoding

### 3. JWT Authentication
- JWT token structure (header, payload, signature)
- Token generation and validation
- Stateless authentication flow
- `UserDetailsService` implementation

### 4. Authorization
- Role-based access control
- Method-level security
- Securing REST endpoints

## Materials

1. [Spring Security Project](https://spring.io/projects/spring-security)
2. [Spring Security Getting Started Guide](https://spring.io/guides/gs/securing-web)
3. [Medium: Spring Boot Security Step by Step](https://medium.com/@ansgar.nell/spring-boot-security-step-by-step-21ea836499f8)
4. [GeeksForGeeks: Introduction to Spring Security](https://www.geeksforgeeks.org/advance-java/introduction-to-spring-security-and-its-features/)
5. [GeeksForGeeks: Securing REST APIs](https://www.geeksforgeeks.org/advance-java/securing-rest-apis-with-spring-security/)
6. [Spring RestClient Reference](https://docs.spring.io/spring-framework/reference/integration/rest-clients.html#rest-restclient)
7. [Baeldung: Spring RestClient](https://www.baeldung.com/spring-boot-restclient)

---

## Common Mistakes to Avoid

- **CORS errors** — configure CORS in SecurityFilterChain for frontend access
- **403 instead of 401** — check if authentication or authorization is failing
- **Password not encoded** — always use PasswordEncoder, never store plain text
- **JWT token expired** — handle token refresh or re-authentication
- **Disabling CSRF without understanding** — only safe for stateless APIs

---

## Self-Check Questions

1. What is the difference between authentication and authorization?
2. How does JWT authentication work in a stateless API?
3. What is the purpose of `UserDetailsService`?
4. When would you use `RestClient` vs `RestTemplate`?
5. How do you configure path-based security rules?
