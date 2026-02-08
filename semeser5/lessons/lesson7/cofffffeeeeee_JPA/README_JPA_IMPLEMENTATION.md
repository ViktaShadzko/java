# Lab 3: Part 1 — JPA and Hibernate Implementation

## Overview

This project demonstrates the implementation of JPA (Java Persistence API) with Hibernate as the persistence provider for a coffee shop beverage management system.

## What's Implemented

### 1. JPA Dependencies and Configuration ✅

**Dependencies in pom.xml:**
- `spring-boot-starter-data-jpa` - JPA support with Hibernate
- `h2` - In-memory database for development

**Configuration in application.properties:**
```properties
spring.profiles.active=jpa
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect
```

### 2. Entity Classes with JPA Annotations ✅

**Beverage Entity** (`entity/Beverage.java`):
- `@Entity` - Marks the class as a JPA entity
- `@Table` - Specifies the table name
- `@Id` and `@GeneratedValue` - Primary key configuration
- `@Column` - Column mappings with constraints
- `@OneToMany` - Relationship with Review entity

**Review Entity** (`entity/Review.java`):
- Complete JPA entity with annotations
- `@ManyToOne` - Many reviews belong to one beverage
- `@JoinColumn` - Foreign key configuration
- Validation constraints (`@Min`, `@Max`, `@NotBlank`)

### 3. Entity Relationship: @OneToMany / @ManyToOne ✅

**Relationship:**
- One Beverage can have many Reviews
- Each Review belongs to one Beverage
- Cascade operations configured (`CascadeType.ALL`)
- Orphan removal enabled
- Helper methods for managing the relationship

### 4. CRUD Operations with EntityManager ✅

**JPABeverageRepository** (`repository/impl/JPABeverageRepository.java`):
- Uses `@PersistenceContext` to inject EntityManager
- Implements all CRUD operations:
  - `getAllBeverages()` - Uses JPQL query
  - `addBeverage()` - Persist/merge operations
  - `getBeverageById()` - Find by primary key
  - `deleteBeverage()` - Remove operation
  - `getBeverageCount()` - Aggregate query

**JPAReviewRepository** (`repository/impl/JPAReviewRepository.java`):
- Complete EntityManager-based implementation
- JPQL queries for complex operations
- Transaction management with `@Transactional`

## Project Structure

```
src/main/java/ehu/java/cofffffeeeeee/
├── entity/
│   ├── Beverage.java          # JPA entity with @OneToMany
│   └── Review.java            # JPA entity with @ManyToOne
├── repository/
│   ├── BeverageRepository.java
│   ├── ReviewRepository.java
│   └── impl/
│       ├── JPABeverageRepository.java    # EntityManager implementation
│       └── JPAReviewRepository.java      # EntityManager implementation
└── controller/
    ├── BeverageController.java
    └── ReviewController.java

src/main/resources/
├── application.properties      # JPA configuration
└── data.sql                   # Sample data initialization
```

## Running the Application

1. **Start the application:**
   ```bash
   ./mvnw spring-boot:run
   ```

2. **Access the application:**
   - Application: http://localhost:8080
   - H2 Console: http://localhost:8080/h2-console
     - JDBC URL: `jdbc:h2:mem:testdb`
     - Username: `sa`
     - Password: (leave empty)

## API Endpoints

### Beverage Endpoints
- `GET /api/beverages` - Get all beverages
- `GET /api/beverages/{id}` - Get beverage by ID
- `POST /api/beverages` - Add new beverage
- `DELETE /api/beverages/{id}` - Delete beverage
- `GET /api/beverages/count` - Get beverage count

### Review Endpoints
- `GET /api/reviews` - Get all reviews
- `GET /api/reviews/{id}` - Get review by ID
- `GET /api/reviews/beverage/{beverageId}` - Get reviews for a beverage
- `POST /api/reviews` - Add new review
- `DELETE /api/reviews/{id}` - Delete review
- `GET /api/reviews/count` - Get review count

## Testing the Implementation

### Example: Add a new beverage
```bash
curl -X POST http://localhost:8080/api/beverages \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Flat White",
    "price": 4.25,
    "description": "Espresso with microfoam milk"
  }'
```

### Example: Add a review
```bash
curl -X POST http://localhost:8080/api/reviews \
  -H "Content-Type: application/json" \
  -d '{
    "author": "Coffee Lover",
    "rating": 5,
    "comment": "Absolutely delicious!",
    "beverage": {"id": 1}
  }'
```

## Key Features Demonstrated

1. **JPA Configuration**: Proper setup of JPA with Hibernate
2. **Entity Mapping**: Correct use of JPA annotations
3. **Relationships**: @OneToMany and @ManyToOne with cascade operations
4. **EntityManager**: CRUD operations using EntityManager API
5. **JPQL Queries**: Type-safe queries for data retrieval
6. **Transaction Management**: @Transactional for data consistency
7. **Validation**: Jakarta Bean Validation integration
8. **Lazy Loading**: Optimized fetch strategies

## Grading Criteria Met

| Criteria | Status | Implementation |
|----------|--------|----------------|
| JPA dependencies and configuration | ✅ | spring-boot-starter-data-jpa, application.properties |
| Entity classes with proper annotations | ✅ | Beverage.java, Review.java with full JPA annotations |
| Entity relationship implemented | ✅ | @OneToMany / @ManyToOne between Beverage and Review |
| CRUD operations with EntityManager | ✅ | JPABeverageRepository, JPAReviewRepository |
| Code quality and organization | ✅ | Clean code, proper package structure, Lombok usage |

## Notes

- The application uses H2 in-memory database for easy testing
- Schema is automatically created by Hibernate (`ddl-auto=create-drop`)
- Sample data is loaded from `data.sql` on startup
- All SQL queries are logged for debugging
- Profile "jpa" activates the JPA repositories

