# Lesson 7: JPA and Hibernate Fundamentals

## Prerequisites

- **Completed:** Lessons 1-6
- **Required knowledge:** SQL, database concepts, JDBC basics
- **Tools:** Database running (PostgreSQL/H2), IDE with JPA support

---

## Learning Objectives

Upon completion of this lesson, students will be able to:
- Understand Object-Relational Mapping (ORM) concepts and JPA architecture
- Map Java entities to database tables using JPA annotations
- Implement entity relationships with proper fetch strategies

## Topics Covered

### 1. JPA and EntityManager Basics
- ORM concept and benefits
- JPA vs Hibernate
- Core operations: `persist()`, `find()`, `merge()`, `remove()`
- Persistence context

### 2. Entity Mapping
- `@Entity`, `@Table`, `@Column` annotations
- `@Id` and `@GeneratedValue` strategies (IDENTITY, SEQUENCE)
- Basic type mapping

### 3. Entity Relationships and Fetch Strategies
- `@OneToMany` / `@ManyToOne` bidirectional relationship
- `@JoinColumn` and `mappedBy`
- Fetch strategies (EAGER vs LAZY)
- Cascade types

## Materials

1. [Thorben Janssen: JPA Entity Mapping](https://thorben-janssen.com/key-jpa-hibernate-annotations/)
2. [Vlad Mihalcea: Entity Identifier Strategies](https://vladmihalcea.com/hibernate-and-uuid-identifiers/)
3. [Thorben Janssen: Entity Relationships](https://thorben-janssen.com/entity-mappings-introduction-jpa-fetchtypes/)
4. [Vlad Mihalcea: ManyToOne Best Practices](https://vladmihalcea.com/the-best-way-to-map-a-onetomany-association-with-jpa-and-hibernate/)
5. [Vlad Mihalcea: EAGER vs LAZY Fetching](https://vladmihalcea.com/hibernate-facts-the-importance-of-fetch-strategy/)
6. [Vlad Mihalcea: Cascade Types Guide](https://vladmihalcea.com/jpa-cascade-types/)

---

## Common Mistakes to Avoid

- **Missing @Entity annotation** — class won't be recognized as entity
- **No default constructor** — JPA requires a no-arg constructor
- **Using EAGER fetch everywhere** — causes performance issues, prefer LAZY
- **Bidirectional relationship without mappedBy** — duplicate foreign keys
- **Wrong cascade type** — CascadeType.ALL can delete more than intended

---

## Self-Check Questions

1. What is the difference between JPA and Hibernate?
2. When would you use EAGER vs LAZY fetching?
3. What is the persistence context?
4. How do you define a bidirectional OneToMany relationship?
5. What are the differences between `persist()`, `merge()`, and `save()`?
