# Lab 2: Part 2 — Database Configuration

## Objective

Configure database connectivity and implement the data access layer with multiple implementation strategies using Spring profiles.

## Tasks

1. Configure the application to use a relational database (e.g., PostgreSQL, H2)
2. Create a DAO interface with standard CRUD operations
3. Implement the DAO using JDBC from Java standard library
4. Implement the DAO using Spring's `JdbcTemplate`
5. Create two Spring profiles (`jdbc` and `jdbctemplate`) to switch between implementations

## Deliverables

- DAO interface and two implementations (JDBC and JdbcTemplate)
- Profile-specific configuration for database and DAO selection

## Acceptance Criteria

- Application connects to the database successfully
- CRUD operations work correctly with both DAO implementations
- Switching profiles changes the active DAO implementation without code changes

## Extra Tasks

- Configure HikariCP connection pool with custom settings
- Add a third `JdbcClient` implementation of DAO with a corresponding `jdbcclient` profile
- Connect to 2 databeses at the same time

---

## Grading (10 points)

| Criteria | Points |
|----------|--------|
| Database connection configured | 2 |
| DAO interface defined | 1 |
| Raw JDBC implementation works | 2 |
| JdbcTemplate implementation works | 2 |
| Profiles switch implementations | 2 |
| Code quality | 1 |
| Extra tasks (bonus) | +2 |
