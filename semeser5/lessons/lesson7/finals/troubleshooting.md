# Troubleshooting: JPA and Hibernate

## LazyInitializationException

**Error:**
```
org.hibernate.LazyInitializationException: could not initialize proxy - no Session
```

**Cause:** Accessing a lazy-loaded relationship outside of a transaction/session.

**Solutions:**
1. Add `@Transactional` to the service method
2. Use `JOIN FETCH` in your query
3. Use `@EntityGraph` to eagerly load specific relationships
4. Initialize the collection before closing the session: `Hibernate.initialize(entity.getCollection())`

---

## Detached Entity Passed to Persist

**Error:**
```
org.hibernate.PersistentObjectException: detached entity passed to persist
```

**Cause:** Trying to persist an entity that already has an ID (was previously saved).

**Solutions:**
1. Use `merge()` instead of `persist()` for updates
2. Remove the ID before persisting a copy
3. Check if you're accidentally reusing an entity object

---

## No Default Constructor

**Error:**
```
org.hibernate.InstantiationException: No default constructor for entity
```

**Cause:** JPA entities require a no-argument constructor.

**Solution:**
```java
@Entity
public class Currency {
    public Currency() {} // Required!

    public Currency(String code) {
        this.code = code;
    }
}
```

---

## Table Not Created

**Problem:** Application starts but table doesn't exist in database.

**Check:**
1. `spring.jpa.hibernate.ddl-auto` property (use `create`, `update`, or `create-drop` for dev)
2. `@Entity` annotation is present
3. Entity is in a package scanned by Spring
4. Database connection is correct

**application.yml:**
```yaml
spring:
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```

---

## Duplicate Foreign Key

**Error:**
```
Duplicate column name 'category_id'
```

**Cause:** Bidirectional relationship mapped on both sides.

**Solution:** Use `mappedBy` on the inverse side:
```java
// Owner side (has the foreign key)
@ManyToOne
@JoinColumn(name = "category_id")
private Category category;

// Inverse side (no foreign key here)
@OneToMany(mappedBy = "category")  // <-- Use mappedBy!
private List<Currency> currencies;
```
