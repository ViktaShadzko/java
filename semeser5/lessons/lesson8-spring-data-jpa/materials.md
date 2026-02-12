# Lesson 8: Spring Data JPA and Advanced Hibernate

## Prerequisites

- **Completed:** Lessons 1-7
- **Required knowledge:** JPA basics, entity mapping
- **Tools:** Database with existing entities from Lesson 7

---

## Learning Objectives

Upon completion of this lesson, students will be able to:
- Create and use Spring Data JPA repositories
- Define custom query methods using method naming conventions and JPQL
- Implement pagination and sorting for large datasets
- Apply transaction management with `@Transactional`
- Understand and avoid the N+1 query problem

## Topics Covered

### 1. Spring Data JPA Introduction
- From EntityManager to Spring Data repositories
- `CrudRepository` and `JpaRepository`
- Built-in CRUD methods

### 2. N+1 Query Problem
- Understanding the N+1 problem
- Detection and diagnosis
- Solutions: JOIN FETCH, `@EntityGraph`, batch fetching

### 3. Query Methods
- Query derivation from method names (`findBy`, `countBy`)
- Common keywords: `And`, `Or`, `Like`, `Between`, `OrderBy`
- `@Query` annotation with JPQL

### 4. Pagination and Sorting
- `Pageable` and `Sort` parameters
- `Page` and `Slice` return types

### 5. Transactions
- `@Transactional` annotation
- Basic transaction management

## Materials

1. [Spring Data JPA - Query Methods](https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html)
2. [Thorben Janssen: Spring Data JPA Query Methods](https://thorben-janssen.com/spring-data-jpa-query-annotation/)
3. [Baeldung: Spring Data JPA @Query](https://www.baeldung.com/spring-data-jpa-query)
4. [Vlad Mihalcea: Spring @Transactional](https://vladmihalcea.com/read-write-read-only-transaction-routing-spring/)
5. [Baeldung: Spring Data JPA Pagination](https://www.baeldung.com/spring-data-jpa-pagination-sorting)
6. [Vlad Mihalcea: N+1 Query Problem](https://vladmihalcea.com/n-plus-1-query-problem/)

---

## Common Mistakes to Avoid

- **N+1 query problem** — fetching related entities one by one instead of joining
- **Missing @Transactional** — LazyInitializationException when accessing lazy collections
- **Wrong @Transactional placement** — put on service layer, not repository
- **Modifying entities outside transaction** — changes won't be persisted
- **Complex method names** — use @Query for complex queries instead

---

## Self-Check Questions

1. What is the N+1 query problem and how do you solve it?
2. What is the difference between `CrudRepository` and `JpaRepository`?
3. How does Spring Data derive queries from method names?
4. When should you use `@Query` instead of derived query methods?
5. What does `@Transactional(readOnly = true)` do?
