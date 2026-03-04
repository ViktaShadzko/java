# Лекция 5: Исключения и обработка ошибок

---

## 🗂️ План лекции

1. Что такое исключение и зачем оно нужно
2. Иерархия исключений в Java
3. Checked vs Unchecked исключения
4. try-catch-finally
5. throws и throw
6. Создание собственных исключений
7. Multi-catch и try-with-resources
8. Антипаттерны обработки исключений
9. Итоги и домашнее задание

---

## 📌 Слайд 1: Что такое исключение?

### Без обработки ошибок:
```java
// ❌ Без исключений — программа падает некрасиво
int[] arr = {1, 2, 3};
System.out.println(arr[10]); // ArrayIndexOutOfBoundsException: Index 10 out of bounds for length 3

String s = null;
System.out.println(s.length()); // NullPointerException
```

> **Исключение** — это объект, который сигнализирует о том, что во время выполнения программы что-то пошло **не так**.

### Механизм:
```
1. Возникает исключительная ситуация
2. JVM создаёт объект-исключение
3. Ищет обработчик (catch блок) вверх по стеку вызовов
4. Если не найден → программа завершается с ошибкой
```

---

## 📌 Слайд 2: Иерархия исключений

```
Throwable
├── Error                          ← JVM-ошибки, не обрабатываем
│   ├── OutOfMemoryError
│   ├── StackOverflowError
│   └── VirtualMachineError
└── Exception
    ├── RuntimeException           ← Unchecked (не обязательно обрабатывать)
    │   ├── NullPointerException
    │   ├── ArrayIndexOutOfBoundsException
    │   ├── ClassCastException
    │   ├── ArithmeticException
    │   ├── IllegalArgumentException
    │   └── IllegalStateException
    └── IOException                ← Checked (ОБЯЗАТЕЛЬНО обрабатывать)
        ├── FileNotFoundException
        └── SQLException (и другие Checked)
```

---

## 📌 Слайд 3: Checked vs Unchecked

| Тип | Наследуется от | Обработка | Примеры |
|---|---|---|---|
| **Checked** | `Exception` | Обязательна | `IOException`, `SQLException` |
| **Unchecked** | `RuntimeException` | Необязательна | `NPE`, `IndexOutOfBounds` |
| **Error** | `Error` | Не обрабатываем | `OutOfMemoryError` |

```java
// ── Checked — компилятор заставляет обработать ────────────
// FileReader может бросить IOException — checked
FileReader fr = new FileReader("file.txt"); // ❌ compile error без обработки!

// Вариант 1: try-catch
try {
    FileReader fr = new FileReader("file.txt");
} catch (FileNotFoundException e) {
    System.out.println("Файл не найден: " + e.getMessage());
}

// Вариант 2: throws — пробрасываем выше
public void readFile() throws IOException {
    FileReader fr = new FileReader("file.txt");
}

// ── Unchecked — компилятор не заставляет обрабатывать ─────
int[] arr = new int[3];
arr[10] = 5; // ArrayIndexOutOfBoundsException — можно не писать try-catch
```

---

## 📌 Слайд 4: try-catch-finally

```java
public class FileProcessor {

    public static String readFirstLine(String path) {
        BufferedReader reader = null;
        try {
            // ── try — код, который может бросить исключение ────
            reader = new BufferedReader(new FileReader(path));
            return reader.readLine();           // может бросить IOException

        } catch (FileNotFoundException e) {
            // ── catch — обработка конкретного типа исключения ──
            System.err.println("Файл не найден: " + path);
            return null;

        } catch (IOException e) {
            // Более широкий catch — перехватит остальные IO-ошибки
            System.err.println("Ошибка чтения: " + e.getMessage());
            return null;

        } finally {
            // ── finally — выполняется ВСЕГДА (и при успехе, и при ошибке) ──
            if (reader != null) {
                try {
                    reader.close(); // закрываем ресурс
                } catch (IOException e) {
                    System.err.println("Ошибка закрытия файла");
                }
            }
        }
    }
}
```

### Порядок catch важен:
```java
try {
    // ...
} catch (FileNotFoundException e) { // сначала — конкретное
    // ...
} catch (IOException e) {           // потом — более общее
    // ...
} catch (Exception e) {             // в конце — самое общее
    // ...
}
// ❌ Нельзя: более общее перед конкретным — compile error!
```

---

## 📌 Слайд 5: throw и throws

```java
// throws — объявляем, что метод МОЖЕТ бросить исключение
public class BankAccount {
    private double balance;

    public BankAccount(double balance) {
        if (balance < 0) {
            // throw — фактически БРОСАЕМ исключение
            throw new IllegalArgumentException(
                "Начальный баланс не может быть отрицательным: " + balance);
        }
        this.balance = balance;
    }

    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount <= 0) {
            throw new IllegalArgumentException("Сумма должна быть положительной");
        }
        if (amount > balance) {
            throw new InsufficientFundsException(
                "Запрошено: " + amount + ", доступно: " + balance);
        }
        balance -= amount;
    }
}

// ── Использование ─────────────────────────────────────────
try {
    BankAccount acc = new BankAccount(1000);
    acc.withdraw(1500);
} catch (InsufficientFundsException e) {
    System.out.println("Ошибка: " + e.getMessage());
} catch (IllegalArgumentException e) {
    System.out.println("Некорректный аргумент: " + e.getMessage());
}
```

---

## 📌 Слайд 6: Создание собственных исключений

```java
// ── Checked исключение ────────────────────────────────────
public class InsufficientFundsException extends Exception {
    private final double requested;
    private final double available;

    public InsufficientFundsException(double requested, double available) {
        super(String.format(
            "Недостаточно средств. Запрошено: %.2f, доступно: %.2f",
            requested, available));
        this.requested = requested;
        this.available = available;
    }

    public double getRequested() { return requested; }
    public double getAvailable() { return available; }
    public double getShortfall() { return requested - available; }
}

// ── Unchecked исключение ──────────────────────────────────
public class UserNotFoundException extends RuntimeException {
    private final String userId;

    public UserNotFoundException(String userId) {
        super("Пользователь не найден: " + userId);
        this.userId = userId;
    }

    public UserNotFoundException(String userId, Throwable cause) {
        super("Пользователь не найден: " + userId, cause);
        this.userId = userId;
    }

    public String getUserId() { return userId; }
}

// ── Использование ─────────────────────────────────────────
try {
    BankAccount acc = new BankAccount(500);
    acc.withdraw(1000);
} catch (InsufficientFundsException e) {
    System.out.println(e.getMessage());
    System.out.printf("Не хватает: %.2f руб.%n", e.getShortfall());
}
```

---

## 📌 Слайд 7: Multi-catch и try-with-resources

### Multi-catch (Java 7+):
```java
// ❌ До Java 7 — дублирование кода
try {
    processFile("data.csv");
} catch (FileNotFoundException e) {
    logger.error("Файл не найден", e);
    throw new ServiceException("Файл не найден", e);
} catch (ParseException e) {
    logger.error("Ошибка парсинга", e);
    throw new ServiceException("Ошибка парсинга", e);
}

// ✅ Java 7+ — multi-catch
try {
    processFile("data.csv");
} catch (FileNotFoundException | ParseException e) {
    logger.error("Ошибка обработки файла", e);
    throw new ServiceException("Ошибка обработки файла", e);
}
```

### try-with-resources (Java 7+):
```java
// ❌ Старый способ — громоздкий finally
Connection conn = null;
PreparedStatement ps = null;
try {
    conn = dataSource.getConnection();
    ps   = conn.prepareStatement("SELECT * FROM users");
    ResultSet rs = ps.executeQuery();
    // ...
} finally {
    if (ps   != null) try { ps.close();   } catch (SQLException e) { }
    if (conn != null) try { conn.close(); } catch (SQLException e) { }
}

// ✅ try-with-resources — автоматически закрывает AutoCloseable
try (Connection conn = dataSource.getConnection();
     PreparedStatement ps = conn.prepareStatement("SELECT * FROM users")) {

    ResultSet rs = ps.executeQuery();
    while (rs.next()) {
        System.out.println(rs.getString("name"));
    }
} catch (SQLException e) {
    System.err.println("Ошибка БД: " + e.getMessage());
}
// conn и ps закрываются АВТОМАТИЧЕСКИ в обратном порядке!
```

### Свой AutoCloseable:
```java
public class ManagedResource implements AutoCloseable {
    private final String name;

    public ManagedResource(String name) {
        this.name = name;
        System.out.println("Открыт: " + name);
    }

    public void doWork() { System.out.println("Работаю: " + name); }

    @Override
    public void close() { System.out.println("Закрыт: " + name); }
}

try (ManagedResource r1 = new ManagedResource("R1");
     ManagedResource r2 = new ManagedResource("R2")) {
    r1.doWork();
    r2.doWork();
}
// Открыт: R1 → Открыт: R2 → Работаю: R1 → Работаю: R2 → Закрыт: R2 → Закрыт: R1
```

---

## 📌 Слайд 8: Exception chaining — цепочки исключений

```java
// Сохраняем оригинальную причину через cause
public class UserService {

    public User findUser(String id) {
        try {
            return userRepository.findById(id); // может бросить SQLException
        } catch (SQLException e) {
            // Оборачиваем в бизнес-исключение, сохраняя причину
            throw new UserNotFoundException(id, e); // ← передаём cause!
        }
    }
}

// При логировании видно всю цепочку:
try {
    userService.findUser("unknown-id");
} catch (UserNotFoundException e) {
    e.printStackTrace();
    // UserNotFoundException: Пользователь не найден: unknown-id
    //   Caused by: SQLException: Connection refused
    //     at ...
}
```

---

## 📌 Слайд 9: Антипаттерны

### ❌ Глотать исключение молча:
```java
try {
    riskyOperation();
} catch (Exception e) {
    // ❌ Пустой catch — исключение "проглочено", никто не знает что пошло не так!
}
```

### ❌ Ловить Exception слишком широко:
```java
try {
    connectToDatabase();
    processData();
    saveResults();
} catch (Exception e) {            // ❌ Ловим ВСЁ подряд
    System.out.println("Ошибка!"); // Не знаем что именно
}
```

### ❌ Использовать исключения для управления потоком:
```java
// ❌ Очень медленно — создание объекта исключения дорого
try {
    while (true) {
        list.get(index++); // ждём IndexOutOfBoundsException как сигнала выхода
    }
} catch (IndexOutOfBoundsException e) {
    // конец списка
}

// ✅ Правильно
for (int i = 0; i < list.size(); i++) {
    process(list.get(i));
}
```

### ✅ Лучшие практики:
```java
// 1. Конкретный тип исключения
catch (FileNotFoundException e) { ... }

// 2. Логировать с контекстом
catch (SQLException e) {
    log.error("Ошибка при запросе пользователя id={}", userId, e);
}

// 3. Либо обработать, либо пробросить — не делай оба сразу
catch (IOException e) {
    log.error("...", e);
    throw new ServiceException("...", e); // ок — оборачиваем
}
```

---

## 📌 Слайд 10: Итоги

### Что нужно запомнить:

✅ **Checked** исключения → компилятор заставляет обработать или объявить через `throws`.

✅ **Unchecked** (RuntimeException) → программная ошибка, не обязательно обрабатывать.

✅ **finally** → всегда выполняется, используй для освобождения ресурсов.

✅ **try-with-resources** → лучший способ работы с Closeable-ресурсами.

✅ **Свои исключения** → наследуй от `Exception` (checked) или `RuntimeException` (unchecked).

✅ **Exception chaining** → передавай `cause`, чтобы не терять оригинальный стек.

✅ **Никогда не глотай исключения молча!**

---

## 📌 Слайд 11: Домашнее задание

### Задание: Валидатор и парсер данных

```java
// 1. Создать иерархию исключений:
class AppException extends RuntimeException { ... }
class ValidationException extends AppException { ... }
class ParseException extends AppException { ... }

// 2. Создать класс UserValidator:
class UserValidator {
    // validateName(String) — бросает ValidationException если имя пустое или < 2 символов
    // validateAge(int)     — бросает ValidationException если возраст < 0 или > 150
    // validateEmail(String) — бросает ValidationException если нет @
}

// 3. Создать класс CsvParser:
// - читает файл CSV
// - использует try-with-resources
// - на каждой строке вызывает UserValidator
// - собирает список ошибок (не прерывается на первой!)
// - после обработки всего файла бросает одно итоговое исключение со списком ошибок
```

---

## ❓ Вопросы для самопроверки

1. В чём разница между `Error` и `Exception`?
2. Когда использовать Checked, а когда Unchecked исключения?
3. Гарантировано ли выполнение `finally` если JVM завершается через `System.exit()`?
4. Что такое exception chaining и зачем он нужен?
5. Что делает `try-with-resources`? Какой интерфейс должен реализовывать ресурс?
6. Что плохого в пустом `catch` блоке?

---

## 📚 Дополнительные материалы

| Ресурс | Описание |
|---|---|
| [Oracle — Exceptions](https://docs.oracle.com/javase/tutorial/essential/exceptions/) | Официальная документация |
| «Effective Java» Item 69-77 | Глава об исключениях |
| [Baeldung — Exception Handling](https://www.baeldung.com/java-exceptions) | Подробная статья |

---

*Лекция 5 из 19 | Курс: Введение в ООП на Java | Семестр 2*

