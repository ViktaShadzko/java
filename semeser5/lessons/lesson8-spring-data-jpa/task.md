# Lab 3: Part 2 — Spring Data JPA Repositories

## Objective

Replace manual DAO implementations with Spring Data JPA repositories and implement proper transaction management.

## Tasks

1. Create Spring Data repository interfaces extending `JpaRepository` for all entities
2. Refactor service layer to use repository interfaces instead of DAO
3. Apply `@Transactional` annotations to service methods where appropriate

## Deliverables

- Repository interfaces for all entities
- Service layer refactored to use repositories instead of DAOs
- Proper transaction boundaries defined with `@Transactional`

## Acceptance Criteria

- All CRUD operations work through Spring Data repositories
- Service methods are properly annotated with `@Transactional`
- No direct EntityManager usage

## Extra Tasks

- Implement pagination and sorting functionality
- Create custom query methods using `@Query` annotation for complex queries

---

## Grading (10 points)

| Criteria | Points |
|----------|--------|
| Repository interfaces extend JpaRepository | 2 |
| Service layer uses repositories | 2 |
| @Transactional properly applied | 2 |
| No direct EntityManager usage | 2 |
| Code quality and organization | 2 |
| Extra tasks (bonus) | +2 |
