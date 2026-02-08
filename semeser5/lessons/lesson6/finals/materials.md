# Lesson 6: Database Access with JDBC

## Prerequisites

- **Completed:** Lessons 1-5
- **Required knowledge:** SQL basics, JDBC fundamentals (review)
- **Tools:** PostgreSQL/H2 database, DBeaver or similar DB client

> **Note:** Students have prior JDBC knowledge. This lesson focuses on Spring abstractions and connection pooling.

---

## Learning Objectives

Upon completion of this lesson, students will be able to:
- Understand JDBC fundamentals and connection management (review)
- Implement CRUD operations using raw JDBC and Spring abstractions
- Configure connection pooling for database access

## Topics Covered

### 1. JDBC Fundamentals (Review)
- Connection, Statement, ResultSet
- PreparedStatement and SQL injection prevention

### 2. Connection Pooling (HikariCP)
- Benefits of connection pooling
- HikariCP configuration options

### 3. CRUD Operations with Raw JDBC
- Basic INSERT, SELECT, UPDATE, DELETE operations

### 4. Spring JDBC Abstractions
- **JdbcTemplate**: Simplified data access with automatic resource management
- **JdbcClient**: Modern fluent API for JDBC operations

## Materials

1. [Oracle JDBC Tutorial](https://docs.oracle.com/javase/tutorial/jdbc/basics/index.html)
2. [Baeldung: Introduction to HikariCP](https://www.baeldung.com/hikaricp)
3. [Baeldung: Spring JdbcTemplate Guide](https://www.baeldung.com/spring-jdbc-jdbctemplate)
4. [Spring JdbcClient Reference](https://docs.spring.io/spring-framework/reference/data-access/jdbc/core.html#jdbc-JdbcClient)
5. [Baeldung: JdbcClient Guide](https://www.baeldung.com/spring-6-jdbcclient-api)

---

## Common Mistakes to Avoid

- **Not closing connections** — always use try-with-resources or let Spring manage connections
- **SQL injection** — never concatenate user input into SQL, use PreparedStatement
- **Wrong DataSource configuration** — check URL, username, password, driver class
- **Connection pool exhaustion** — set appropriate pool size, release connections properly

---

## Self-Check Questions

1. Why is connection pooling important?
2. What is the difference between Statement and PreparedStatement?
3. How does JdbcTemplate simplify JDBC code?
4. What happens if you don't close a database connection?
5. How do you configure HikariCP pool size?
