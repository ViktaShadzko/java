# Lab 3: Part 1 — Configure JPA and Hibernate

## Objective

Set up JPA with Hibernate as the persistence provider and create entity mappings for the application domain.

## Tasks

1. Add JPA and Hibernate dependencies to the project
2. Configure JPA properties in `application.yml`
3. Create entity classes with appropriate JPA annotations
4. Implement at least one entity relationship (`@OneToMany` / `@ManyToOne`)
5. Implement basic CRUD operations using `EntityManager`

## Deliverables

- JPA configuration in `application.yml`
- Entity classes with proper mappings and relationships
- Working persistence layer with `EntityManager` operations
- Basic CRUD functionality for all entities

## Acceptance Criteria

- Application starts without JPA/Hibernate errors
- CRUD operations persist and retrieve data correctly

---

## Grading (10 points)

| Criteria | Points |
|----------|--------|
| JPA dependencies and configuration | 2 |
| Entity classes with proper annotations | 2 |
| Entity relationship implemented | 2 |
| CRUD operations with EntityManager | 2 |
| Code quality and organization | 2 |
