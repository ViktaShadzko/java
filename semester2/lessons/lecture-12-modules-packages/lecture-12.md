# Лекция 12: Модули и пакеты

---

## 🗂️ План лекции

1. Пакеты — организация классов
2. import и import static
3. Модульная система Java (JPMS, Java 9+)
4. module-info.java
5. Видимость в модульной системе
6. Практика — структура проекта
7. Итоги и домашнее задание

---

## 📌 Слайд 1: Пакеты

> **Пакет** — пространство имён для группировки связанных классов.

### Зачем нужны пакеты:
- Избегать конфликтов имён (два класса `User` в разных пакетах)
- Логическая организация кода
- Управление доступом (`package-private` видимость)

```java
// ── Объявление пакета — первая строка файла ───────────────
package com.company.project.service;

// Имя файла: UserService.java
// Путь: src/com/company/project/service/UserService.java

public class UserService {
    // ...
}
```

### Соглашение об именовании:
```
com.company.project.layer
│    │       │       └── Слой: service, repository, controller, model
│    │       └────────── Название проекта
│    └────────────────── Домен компании
└─────────────────────── Обратный домен: com, org, ru, edu
```

```
Пример:
com.bank.payments.service.TransactionService
org.university.course.repository.StudentRepository
ru.company.app.controller.UserController
```

---

## 📌 Слайд 2: Import

```java
package com.example.app;

// ── Обычный import — конкретный класс ─────────────────────
import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

// ── Wildcard import — все классы пакета ───────────────────
import java.util.*;           // ← нормально для IDE, но явный лучше для читаемости

// ── Static import — статические члены ────────────────────
import static java.lang.Math.PI;
import static java.lang.Math.sqrt;
import static java.util.Collections.sort;
import static java.util.Collections.unmodifiableList;

public class Example {
    public void calculate() {
        double area = PI * 5 * 5;    // без Math.PI
        double root = sqrt(16);       // без Math.sqrt

        List<Integer> nums = new ArrayList<>();
        sort(nums);                   // без Collections.sort
    }
}
```

### Автоматически импортируется:
```java
// java.lang — всегда доступен без import
String    s = "hello";      // java.lang.String
System.out.println("...");  // java.lang.System
Integer   i = 42;           // java.lang.Integer
Math.PI;                    // java.lang.Math
```

---

## 📌 Слайд 3: Видимость и пакеты

```java
// ── package-private (по умолчанию) — виден только в пакете ──
class InternalHelper {      // без public!
    void doSomething() { }  // без public!
}

// ── Файл 1: com.example.service.OrderService ─────────────
package com.example.service;

public class OrderService {
    public void createOrder() { }       // виден всем

    void validateOrder() { }            // package-private — только service пакет

    private void calculateTax() { }    // только этот класс
}

// ── Файл 2: com.example.service.PaymentService ───────────
package com.example.service;

public class PaymentService {
    public void process(OrderService os) {
        os.createOrder();   // ✅ public
        os.validateOrder(); // ✅ тот же пакет!
    }
}

// ── Файл 3: com.example.controller.OrderController ───────
package com.example.controller;

import com.example.service.OrderService;

public class OrderController {
    public void handle(OrderService os) {
        os.createOrder();   // ✅ public
        // os.validateOrder(); // ❌ package-private — другой пакет!
    }
}
```

---

## 📌 Слайд 4: Модульная система JPMS (Java 9+)

> До Java 9 — пакеты. С Java 9 — **модули**: ещё один уровень изоляции.

### Проблемы до модулей:
- classpath — "jar hell", конфликты версий
- нет строгой изоляции между библиотеками
- `public` класс виден **всему** classpath

### Модуль:
```
module myapp {
    ├── packages (один или несколько)
    ├── явно объявляет ЧТО экспортирует
    └── явно объявляет ОТ КОГО зависит
}
```

---

## 📌 Слайд 5: module-info.java

```java
// ── Файл: src/module-info.java ────────────────────────────
module com.example.app {

    // Зависимости — что нам нужно
    requires java.base;           // автоматически (всегда)
    requires java.sql;            // для JDBC
    requires com.google.gson;     // сторонняя библиотека
    requires transitive java.logging; // транзитивная — передаётся нашим пользователям

    // Экспорт — что мы предоставляем
    exports com.example.app.service;          // все могут использовать
    exports com.example.app.model;
    exports com.example.app.api to com.example.client; // только для конкретного модуля!

    // Для рефлексии (Hibernate, Jackson нужна)
    opens com.example.app.model;              // открыть для рефлексии всем
    opens com.example.app.internal to com.google.gson; // только gson может рефлектировать
}
```

```java
// ── Модуль библиотеки: src/module-info.java ───────────────
module com.example.library {
    exports com.example.library.api;      // публичный API
    // com.example.library.internal — не экспортируется, скрыто!
}
```

---

## 📌 Слайд 6: Структура проекта

### Рекомендуемая структура Maven/Gradle проекта:

```
my-project/
├── pom.xml (или build.gradle)
└── src/
    ├── main/
    │   ├── java/
    │   │   ├── module-info.java          ← модуль (Java 9+)
    │   │   └── com/
    │   │       └── example/
    │   │           └── app/
    │   │               ├── controller/
    │   │               │   └── UserController.java
    │   │               ├── service/
    │   │               │   └── UserService.java
    │   │               ├── repository/
    │   │               │   └── UserRepository.java
    │   │               ├── model/
    │   │               │   └── User.java
    │   │               └── Main.java
    │   └── resources/
    │       ├── application.properties
    │       └── logback.xml
    └── test/
        └── java/
            └── com/example/app/
                └── service/
                    └── UserServiceTest.java
```

---

## 📌 Слайд 7: Слоистая архитектура и пакеты

```java
// ── model — просто данные ─────────────────────────────────
package com.example.app.model;

public class User {
    private Long   id;
    private String name;
    private String email;
    // конструктор, геттеры, сеттеры
}

// ── repository — работа с данными ────────────────────────
package com.example.app.repository;

import com.example.app.model.User;

public interface UserRepository {
    Optional<User> findById(Long id);
    List<User> findAll();
    User save(User user);
    void delete(Long id);
}

// ── service — бизнес-логика ───────────────────────────────
package com.example.app.service;

import com.example.app.model.User;
import com.example.app.repository.UserRepository;

public class UserService {
    private final UserRepository repository;  // зависимость через интерфейс!

    public UserService(UserRepository repository) {
        this.repository = repository;
    }

    public User getUser(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(id));
    }
}

// ── controller — точка входа ──────────────────────────────
package com.example.app.controller;

import com.example.app.service.UserService;

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public void handleGetUser(Long id) {
        System.out.println(userService.getUser(id));
    }
}
```

---

## 📌 Слайд 8: Итоги

✅ **Пакет** — пространство имён + механизм доступа (`package-private`).

✅ Соглашение: обратный домен `com.company.project.layer`.

✅ `import static` — для статических методов и констант.

✅ **Модули (Java 9+)** — явные зависимости (`requires`) и экспорт (`exports`).

✅ `module-info.java` — описание модуля в корне исходников.

✅ Слоистая структура: `model → repository → service → controller`.

---

## 📌 Слайд 9: Домашнее задание

```
Создать структуру проекта "Библиотека":
com.library
├── model
│   ├── Book.java       — id, title, author, year, genre
│   └── Reader.java     — id, name, email
├── repository
│   ├── BookRepository.java  — интерфейс
│   └── InMemoryBookRepository.java — реализация
├── service
│   └── LibraryService.java  — бизнес-логика
└── Main.java

Методы:
- addBook(Book)
- findByAuthor(String) → List<Book>
- findByGenre(String)  → List<Book>
- borrowBook(Long bookId, Long readerId) → бросает если нет в наличии
- returnBook(Long bookId)
```

---

## ❓ Вопросы для самопроверки

1. Что такое `package-private` модификатор доступа?
2. Чем модуль отличается от пакета?
3. Что означает `requires transitive` в `module-info.java`?
4. Чем отличается `exports` от `opens`?
5. Почему `java.lang` не нужно импортировать?

---

*Лекция 12 из 19 | Курс: Введение в ООП на Java | Семестр 2*

