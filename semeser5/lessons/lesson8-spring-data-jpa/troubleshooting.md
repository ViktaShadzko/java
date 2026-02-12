# Troubleshooting: Spring Data JPA

## N+1 Query Problem

**Symptom:** Many SQL queries when loading a list of entities with relationships.

**Detection:** Enable SQL logging:
```yaml
spring:
  jpa:
    show-sql: true
    properties:
      hibernate:
        format_sql: true
```

**Solutions:**

1. **JOIN FETCH in @Query:**
```java
@Query("SELECT c FROM Currency c JOIN FETCH c.category")
List<Currency> findAllWithCategory();
```

2. **@EntityGraph:**
```java
@EntityGraph(attributePaths = {"category", "history"})
List<Currency> findAll();
```

3. **Batch fetching:**
```yaml
spring:
  jpa:
    properties:
      hibernate:
        default_batch_fetch_size: 20
```

---

## Query Method Not Working

**Problem:** Custom query method returns empty or wrong results.

**Check:**
1. Method name follows Spring Data naming conventions
2. Property names match entity fields exactly (case-sensitive)
3. Use `@Query` for complex conditions

**Examples:**
```java
// Correct
List<Currency> findByCodeIgnoreCase(String code);

// Wrong - 'Name' doesn't match 'name' in some cases
List<Currency> findByName(String Name);
```

---

## Transaction Not Working

**Problem:** Changes not saved to database.

**Check:**
1. `@Transactional` is on public method
2. `@Transactional` is from `org.springframework.transaction.annotation`
3. Method is called from outside the class (not self-invocation)
4. `@EnableTransactionManagement` is present (auto-configured in Spring Boot)

**Wrong (self-invocation):**
```java
public void doSomething() {
    this.saveData();  // @Transactional won't work!
}

@Transactional
public void saveData() { ... }
```

---

## Repository Not Found

**Error:**
```
No qualifying bean of type 'CurrencyRepository'
```

**Check:**
1. Interface extends `JpaRepository` or `CrudRepository`
2. Entity class has `@Entity` annotation
3. Repository is in a package scanned by Spring Boot
4. `@EnableJpaRepositories` if using custom package

---

## Pagination Returns Wrong Count

**Problem:** `Page.getTotalElements()` returns wrong number.

**Solution:** Use `@Query` with countQuery:
```java
@Query(
    value = "SELECT c FROM Currency c WHERE c.active = true",
    countQuery = "SELECT COUNT(c) FROM Currency c WHERE c.active = true"
)
Page<Currency> findActiveCurrencies(Pageable pageable);
```
