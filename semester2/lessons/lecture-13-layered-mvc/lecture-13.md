# Лекция 13: Слоистая архитектура и MVC

---

## 🗂️ План лекции

1. Зачем нужна архитектура?
2. Layered Architecture (Слоистая архитектура)
3. Паттерн MVC — Model-View-Controller
4. Зависимости между слоями
5. Принцип SOLID — кратко
6. Dependency Injection (DI)
7. Пример приложения с MVC
8. Итоги и домашнее задание

---

## 📌 Слайд 1: Зачем нужна архитектура?

### Без архитектуры — "Большой комок грязи":
```java
// ❌ Всё в одном месте — God Class
public class App {
    public static void main(String[] args) {
        // Чтение ввода
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();

        // Валидация
        if (name == null || name.isBlank()) {
            System.out.println("Ошибка!");
            return;
        }

        // Запрос к БД (напрямую в main!)
        Connection conn = DriverManager.getConnection("jdbc:...");
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM users WHERE name=?");
        ps.setString(1, name);
        ResultSet rs = ps.executeQuery();

        // Бизнес-логика
        while (rs.next()) {
            double balance = rs.getDouble("balance");
            if (balance > 10000) balance *= 1.05; // начислить бонус

            // Форматирование вывода
            System.out.printf("Пользователь: %s, баланс: %.2f%n", name, balance);
        }
    }
}
// ❌ Невозможно тестировать, изменить или переиспользовать
```

---

## 📌 Слайд 2: Layered Architecture

```
┌─────────────────────────────────┐
│        Presentation Layer       │  ← Controller / View
│   (Пользовательский интерфейс)  │
├─────────────────────────────────┤
│         Business Layer          │  ← Service
│         (Бизнес-логика)         │
├─────────────────────────────────┤
│        Persistence Layer        │  ← Repository / DAO
│        (Доступ к данным)        │
├─────────────────────────────────┤
│          Database Layer         │  ← DB, файлы, API
│            (Хранилище)          │
└─────────────────────────────────┘

Правило: каждый слой зависит только от слоя НИЖЕ!
```

### Что в каждом слое:

| Слой | Ответственность | Пакет |
|---|---|---|
| **Controller** | Принять запрос, вернуть ответ | `controller` |
| **Service** | Бизнес-логика, оркестрация | `service` |
| **Repository** | CRUD операции с хранилищем | `repository` |
| **Model/Entity** | Данные предметной области | `model` / `entity` |

---

## 📌 Слайд 3: Модель (Model)

```java
package com.example.library.model;

// ── Сущность (Entity) — данные предметной области ─────────
public class Book {
    private Long   id;
    private String title;
    private String author;
    private int    year;
    private double price;
    private boolean available;

    // Конструктор для создания нового (без id — БД назначит)
    public Book(String title, String author, int year, double price) {
        this.title     = title;
        this.author    = author;
        this.year      = year;
        this.price     = price;
        this.available = true;
    }

    // Геттеры и сеттеры...

    @Override
    public String toString() {
        return String.format("Book{id=%d, title='%s', author='%s', price=%.2f, available=%b}",
            id, title, author, price, available);
    }
}

// ── DTO (Data Transfer Object) — для передачи между слоями ─
public class BookDto {
    private Long   id;
    private String title;
    private String author;
    private double price;

    // Только геттеры — DTO часто иммутабельны
    // Используется когда не нужны ВСЕ поля сущности
}
```

---

## 📌 Слайд 4: Repository (Persistence Layer)

```java
package com.example.library.repository;

// ── Интерфейс — контракт репозитория ─────────────────────
public interface BookRepository {
    Book save(Book book);
    Optional<Book> findById(Long id);
    List<Book> findAll();
    List<Book> findByAuthor(String author);
    List<Book> findAvailable();
    void delete(Long id);
    boolean exists(Long id);
}

// ── In-Memory реализация (для разработки и тестов) ────────
public class InMemoryBookRepository implements BookRepository {
    private final Map<Long, Book> storage = new HashMap<>();
    private long nextId = 1;

    @Override
    public Book save(Book book) {
        if (book.getId() == null) {
            book.setId(nextId++);       // автоинкремент
        }
        storage.put(book.getId(), book);
        return book;
    }

    @Override
    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Book> findByAuthor(String author) {
        return storage.values().stream()
            .filter(b -> b.getAuthor().equalsIgnoreCase(author))
            .collect(Collectors.toList());
    }

    @Override
    public List<Book> findAvailable() {
        return storage.values().stream()
            .filter(Book::isAvailable)
            .collect(Collectors.toList());
    }

    @Override
    public void delete(Long id) { storage.remove(id); }

    @Override
    public boolean exists(Long id) { return storage.containsKey(id); }
}
```

---

## 📌 Слайд 5: Service (Business Layer)

```java
package com.example.library.service;

// ── Сервис — бизнес-логика ────────────────────────────────
public class BookService {
    private final BookRepository repository; // зависимость через интерфейс!

    // ── Dependency Injection через конструктор ────────────
    public BookService(BookRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    public Book addBook(String title, String author, int year, double price) {
        // Валидация — это бизнес-правило, НЕ в Repository!
        if (title == null || title.isBlank())
            throw new ValidationException("Название книги обязательно");
        if (price < 0)
            throw new ValidationException("Цена не может быть отрицательной");

        Book book = new Book(title, author, year, price);
        return repository.save(book);
    }

    public Book getBook(Long id) {
        return repository.findById(id)
            .orElseThrow(() -> new BookNotFoundException(id));
    }

    public List<Book> searchByAuthor(String author) {
        if (author == null || author.isBlank())
            throw new ValidationException("Имя автора не может быть пустым");
        return repository.findByAuthor(author);
    }

    public void borrowBook(Long bookId, String readerName) {
        Book book = getBook(bookId);
        // Бизнес-правило: нельзя взять недоступную книгу
        if (!book.isAvailable())
            throw new BookNotAvailableException(bookId);
        book.setAvailable(false);
        book.setBorrowedBy(readerName);
        repository.save(book);
    }

    public void returnBook(Long bookId) {
        Book book = getBook(bookId);
        book.setAvailable(true);
        book.setBorrowedBy(null);
        repository.save(book);
    }

    public List<Book> getAllAvailable() {
        return repository.findAvailable();
    }

    // ── Аналитика — стримы внутри сервиса ────────────────
    public Map<String, Long> countByAuthor() {
        return repository.findAll().stream()
            .collect(Collectors.groupingBy(Book::getAuthor, Collectors.counting()));
    }

    public double averagePrice() {
        return repository.findAll().stream()
            .mapToDouble(Book::getPrice)
            .average()
            .orElse(0.0);
    }
}
```

---

## 📌 Слайд 6: Controller (Presentation Layer)

```java
package com.example.library.controller;

// ── Контроллер — принимает команды, возвращает ответы ─────
public class BookController {
    private final BookService bookService; // зависимость только от сервиса!

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    // Методы контроллера — тонкие: принять → делегировать → вернуть
    public void addBook(String title, String author, int year, double price) {
        try {
            Book book = bookService.addBook(title, author, year, price);
            System.out.println("✅ Добавлена: " + book);
        } catch (ValidationException e) {
            System.out.println("❌ Ошибка валидации: " + e.getMessage());
        }
    }

    public void showBook(Long id) {
        try {
            Book book = bookService.getBook(id);
            printBook(book);
        } catch (BookNotFoundException e) {
            System.out.println("❌ Книга не найдена: " + id);
        }
    }

    public void showAvailableBooks() {
        List<Book> books = bookService.getAllAvailable();
        if (books.isEmpty()) {
            System.out.println("Нет доступных книг");
            return;
        }
        System.out.println("📚 Доступные книги:");
        books.forEach(this::printBook);
    }

    public void borrowBook(Long bookId, String reader) {
        try {
            bookService.borrowBook(bookId, reader);
            System.out.println("✅ " + reader + " взял книгу #" + bookId);
        } catch (BookNotFoundException e) {
            System.out.println("❌ Книга не найдена");
        } catch (BookNotAvailableException e) {
            System.out.println("❌ Книга уже занята");
        }
    }

    private void printBook(Book book) {
        System.out.printf("  [%d] %-30s | %s | %.2f руб. | %s%n",
            book.getId(), book.getTitle(), book.getAuthor(),
            book.getPrice(), book.isAvailable() ? "✅ доступна" : "❌ занята");
    }
}
```

---

## 📌 Слайд 7: Сборка и DI

```java
// ── Main — сборка зависимостей (Composition Root) ────────
public class Main {
    public static void main(String[] args) {
        // 1. Создаём инфраструктуру (самый нижний слой)
        BookRepository repository = new InMemoryBookRepository();

        // 2. Внедряем в сервис
        BookService service = new BookService(repository);

        // 3. Внедряем сервис в контроллер
        BookController controller = new BookController(service);

        // 4. Используем приложение
        controller.addBook("Война и мир", "Толстой Л.Н.", 1869, 450.0);
        controller.addBook("Преступление и наказание", "Достоевский Ф.М.", 1866, 380.0);
        controller.addBook("Мастер и Маргарита", "Булгаков М.А.", 1967, 520.0);

        controller.showAvailableBooks();

        controller.borrowBook(1L, "Иван Иванов");
        controller.borrowBook(1L, "Мария Петрова"); // уже занята!

        controller.showAvailableBooks(); // теперь 2 книги

        controller.returnBook(1L);
        controller.showAvailableBooks(); // снова 3 книги
    }
}
```

---

## 📌 Слайд 8: SOLID — кратко

| Принцип | Расшифровка | Суть |
|---|---|---|
| **S** | Single Responsibility | Один класс — одна ответственность |
| **O** | Open/Closed | Открыт для расширения, закрыт для изменения |
| **L** | Liskov Substitution | Наследник заменяем родителем |
| **I** | Interface Segregation | Много малых интерфейсов лучше одного большого |
| **D** | Dependency Inversion | Зависеть от абстракций, не от реализаций |

```java
// D — Dependency Inversion в действии:
// ❌ Зависимость от реализации
public class BookService {
    private InMemoryBookRepository repository = new InMemoryBookRepository();
    // ← нельзя поменять на Database-реализацию без изменения класса!
}

// ✅ Зависимость от абстракции
public class BookService {
    private final BookRepository repository; // ← интерфейс!

    public BookService(BookRepository repository) {
        this.repository = repository; // внедряем снаружи
    }
    // теперь можно передать InMemory, Database, или Mock для теста!
}
```

---

## 📌 Слайд 9: Итоги

✅ **Layered Architecture** — каждый слой отвечает только за своё.

✅ **Controller** — тонкий: принять запрос, передать сервису, вернуть ответ.

✅ **Service** — вся бизнес-логика. Не знает ни об UI, ни о БД.

✅ **Repository** — только CRUD. Не знает о бизнес-логике.

✅ **DI** — передавать зависимости через конструктор, не создавать внутри.

✅ **SOLID** — принципы хорошего дизайна. Начни с S и D.

---

## 📌 Слайд 10: Домашнее задание

```
Реализовать консольное приложение "Студенческий журнал":

Модель: Student (id, name, faculty, List<Grade>)
        Grade   (subject, score, date)

Repository: StudentRepository — CRUD
Service:    StudentService    — бизнес-логика:
    - enroll(name, faculty)
    - addGrade(studentId, subject, score)
    - getAverage(studentId)
    - getTopStudents(int n)         — топ N студентов по среднему
    - getFacultyStats(faculty)      — статистика по факультету

Controller: StudentController — принимает команды, обрабатывает исключения

Main:       Сборка + демонстрация всех операций
```

---

## ❓ Вопросы для самопроверки

1. Почему Controller не должен напрямую обращаться к Repository?
2. Почему Service должен зависеть от интерфейса Repository, а не реализации?
3. Что такое Dependency Injection?
4. В чём принцип S из SOLID?
5. Чем DTO отличается от Entity?

---

*Лекция 13 из 19 | Курс: Введение в ООП на Java | Семестр 2*

