# Лекция 4: Enum и Singleton

---

## 🗂️ План лекции

1. Enum — перечисления в Java
2. Поля, методы и конструкторы в Enum
3. Абстрактные методы в Enum
4. EnumSet и EnumMap
5. Паттерн Singleton — один объект на всю программу
6. Способы реализации Singleton
7. Singleton через Enum (лучший способ)
8. Антипаттерны и подводные камни
9. Итоги и домашнее задание

---

## 📌 Слайд 1: Зачем нужен Enum?

### Проблема без Enum:
```java
// ❌ Константы как int — никакой типобезопасности
public class OrderStatus {
    public static final int PENDING   = 0;
    public static final int CONFIRMED = 1;
    public static final int SHIPPED   = 2;
    public static final int DELIVERED = 3;
}

// Можно передать любое число — компилятор не проверит!
void processOrder(int status) { ... }
processOrder(42); // ❌ нет такого статуса, но компилируется!
```

### Решение — Enum:
```java
// ✅ Enum — типобезопасные константы
public enum OrderStatus {
    PENDING,
    CONFIRMED,
    SHIPPED,
    DELIVERED
}

// Компилятор знает допустимые значения
void processOrder(OrderStatus status) { ... }
processOrder(OrderStatus.SHIPPED);     // ✅
// processOrder(42);                   // ❌ compile error!
```

---

## 📌 Слайд 2: Основы Enum

```java
public enum DayOfWeek {
    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY;
}

// ── Базовые операции ──────────────────────────────────────
DayOfWeek day = DayOfWeek.WEDNESDAY;

System.out.println(day);            // WEDNESDAY
System.out.println(day.name());     // WEDNESDAY
System.out.println(day.ordinal());  // 2  (индекс с 0)

// Сравнение
if (day == DayOfWeek.WEDNESDAY) {
    System.out.println("Среда!");
}

// switch
switch (day) {
    case MONDAY:
    case TUESDAY:
    case WEDNESDAY:
    case THURSDAY:
    case FRIDAY:
        System.out.println("Рабочий день");
        break;
    case SATURDAY:
    case SUNDAY:
        System.out.println("Выходной!");
        break;
}

// Все значения
for (DayOfWeek d : DayOfWeek.values()) {
    System.out.println(d.ordinal() + ": " + d);
}

// Из строки
DayOfWeek friday = DayOfWeek.valueOf("FRIDAY"); // ← регистр важен!
```

---

## 📌 Слайд 3: Поля и методы в Enum

```java
public enum Planet {
    MERCURY(3.303e+23, 2.4397e6),
    VENUS  (4.869e+24, 6.0518e6),
    EARTH  (5.976e+24, 6.37814e6),
    MARS   (6.421e+23, 3.3972e6);

    private final double mass;    // кг
    private final double radius;  // м
    private static final double G = 6.67300E-11;

    // Конструктор enum — всегда private!
    Planet(double mass, double radius) {
        this.mass   = mass;
        this.radius = radius;
    }

    // Метод в enum
    public double surfaceGravity() {
        return G * mass / (radius * radius);
    }

    public double surfaceWeight(double otherMass) {
        return otherMass * surfaceGravity();
    }
}

// ── Использование ─────────────────────────────────────────
double earthWeight = 75.0;
double mass = earthWeight / Planet.EARTH.surfaceGravity();

for (Planet p : Planet.values()) {
    System.out.printf("Вес на %-10s: %6.2f%n", p, p.surfaceWeight(mass));
}
// Вес на MERCURY   :  28.33
// Вес на VENUS     :  67.88
// Вес на EARTH     :  75.00
// Вес на MARS      :  28.46
```

---

## 📌 Слайд 4: Абстрактные методы в Enum

```java
public enum Operation {
    ADD("+") {
        @Override
        public double apply(double x, double y) { return x + y; }
    },
    SUBTRACT("-") {
        @Override
        public double apply(double x, double y) { return x - y; }
    },
    MULTIPLY("*") {
        @Override
        public double apply(double x, double y) { return x * y; }
    },
    DIVIDE("/") {
        @Override
        public double apply(double x, double y) {
            if (y == 0) throw new ArithmeticException("Деление на ноль!");
            return x / y;
        }
    };

    private final String symbol;

    Operation(String symbol) { this.symbol = symbol; }

    // Абстрактный метод — каждая константа обязана реализовать
    public abstract double apply(double x, double y);

    @Override public String toString() { return symbol; }
}

// ── Использование ─────────────────────────────────────────
double x = 10, y = 3;
for (Operation op : Operation.values()) {
    System.out.printf("%.1f %s %.1f = %.2f%n", x, op, y, op.apply(x, y));
}
// 10.0 + 3.0 = 13.00
// 10.0 - 3.0 = 7.00
// 10.0 * 3.0 = 30.00
// 10.0 / 3.0 = 3.33
```

---

## 📌 Слайд 5: EnumSet и EnumMap

```java
import java.util.EnumSet;
import java.util.EnumMap;

public enum Permission {
    READ, WRITE, DELETE, ADMIN
}

// ── EnumSet — множество констант Enum ─────────────────────
EnumSet<Permission> userPerms  = EnumSet.of(Permission.READ, Permission.WRITE);
EnumSet<Permission> adminPerms = EnumSet.allOf(Permission.class);
EnumSet<Permission> guestPerms = EnumSet.of(Permission.READ);

System.out.println(userPerms.contains(Permission.DELETE)); // false
System.out.println(adminPerms);  // [READ, WRITE, DELETE, ADMIN]

// ── EnumMap — карта с ключами Enum ────────────────────────
EnumMap<Permission, String> descriptions = new EnumMap<>(Permission.class);
descriptions.put(Permission.READ,   "Просмотр файлов");
descriptions.put(Permission.WRITE,  "Редактирование файлов");
descriptions.put(Permission.DELETE, "Удаление файлов");
descriptions.put(Permission.ADMIN,  "Полный доступ");

for (Permission p : userPerms) {
    System.out.println(p + ": " + descriptions.get(p));
}
// READ: Просмотр файлов
// WRITE: Редактирование файлов
```

---

## 📌 Слайд 6: Паттерн Singleton

### Суть:
> **Singleton** — паттерн проектирования, гарантирующий, что класс имеет **только один экземпляр** и предоставляет глобальную точку доступа к нему.

### Когда нужен:
- Конфигурация приложения (один конфиг на всё)
- Пул соединений с БД
- Логгер
- Кэш

---

## 📌 Слайд 7: Способы реализации Singleton

### Вариант 1 — Ленивая инициализация (Lazy):
```java
public class ConfigSingleton {
    private static ConfigSingleton instance; // null до первого вызова

    private String dbUrl;
    private int    port;

    // Приватный конструктор — нельзя создать снаружи
    private ConfigSingleton() {
        dbUrl = "jdbc:postgresql://localhost:5432/mydb";
        port  = 8080;
        System.out.println("Config создан!");
    }

    // Единственный способ получить объект
    public static ConfigSingleton getInstance() {
        if (instance == null) {            // ⚠️ не потокобезопасно!
            instance = new ConfigSingleton();
        }
        return instance;
    }

    public String getDbUrl() { return dbUrl; }
    public int    getPort()  { return port;  }
}
```

### Вариант 2 — Потокобезопасный (Double-Checked Locking):
```java
public class ThreadSafeConfig {
    // volatile — гарантирует видимость между потоками
    private static volatile ThreadSafeConfig instance;

    private ThreadSafeConfig() { }

    public static ThreadSafeConfig getInstance() {
        if (instance == null) {                    // 1-я проверка без lock
            synchronized (ThreadSafeConfig.class) {
                if (instance == null) {            // 2-я проверка с lock
                    instance = new ThreadSafeConfig();
                }
            }
        }
        return instance;
    }
}
```

### Вариант 3 — Через static holder (лучший классический):
```java
public class AppConfig {
    private AppConfig() { }

    // Внутренний класс загружается только при первом вызове getInstance()
    private static class Holder {
        private static final AppConfig INSTANCE = new AppConfig();
    }

    public static AppConfig getInstance() {
        return Holder.INSTANCE; // потокобезопасно, ленивая инициализация
    }
}
```

---

## 📌 Слайд 8: Singleton через Enum — лучший способ

```java
// ✅ Enum Singleton — рекомендует Joshua Bloch ("Effective Java")
public enum DatabaseConnection {
    INSTANCE; // единственная константа = единственный объект

    private final String url;
    private int connectionCount = 0;

    // Конструктор enum
    DatabaseConnection() {
        this.url = "jdbc:postgresql://localhost:5432/app";
        System.out.println("Подключение к БД создано!");
    }

    public String getUrl() { return url; }

    public void connect() {
        connectionCount++;
        System.out.println("Подключение #" + connectionCount + " к " + url);
    }

    public int getConnectionCount() { return connectionCount; }
}

// ── Использование ─────────────────────────────────────────
DatabaseConnection db1 = DatabaseConnection.INSTANCE;
DatabaseConnection db2 = DatabaseConnection.INSTANCE;
DatabaseConnection db3 = DatabaseConnection.INSTANCE;

System.out.println(db1 == db2); // true — один и тот же объект!
System.out.println(db2 == db3); // true

db1.connect(); // Подключение #1
db2.connect(); // Подключение #2
db3.connect(); // Подключение #3

System.out.println(db1.getConnectionCount()); // 3 — все три вызова через один объект
```

### Почему Enum Singleton лучше:
| Критерий | Обычный Singleton | Enum Singleton |
|---|---|---|
| Потокобезопасность | Нужна дополнительная работа | ✅ Гарантирована JVM |
| Сериализация | Нужна дополнительная работа | ✅ Гарантирована |
| Reflection-атаки | Уязвим | ✅ Защищён |
| Количество кода | Много | Минимум |

---

## 📌 Слайд 9: Антипаттерны

### ❌ Singleton как замена глобальным переменным:
```java
// ❌ Неправильное использование — Singleton для хранения состояния UI
public enum UIState {
    INSTANCE;
    public String currentUser;
    public String currentPage;
    public boolean isDarkMode;
    // Всё это должно быть в отдельных классах!
}
```

### ❌ Злоупотребление Singleton затрудняет тестирование:
```java
// ❌ Трудно тестировать — зависимость скрыта внутри метода
public class OrderService {
    public void processOrder(Order order) {
        DatabaseConnection.INSTANCE.connect(); // скрытая зависимость!
        // ...
    }
}

// ✅ Лучше — Dependency Injection (принцип Spring)
public class OrderService {
    private final DataSource dataSource; // явная зависимость

    public OrderService(DataSource dataSource) {
        this.dataSource = dataSource; // внедряется снаружи → легко мокировать
    }
}
```

---

## 📌 Слайд 10: Итоги

### Что нужно запомнить:

✅ **Enum** — типобезопасные константы. Могут иметь поля, методы и абстрактные методы.

✅ **Enum** нельзя унаследовать, нельзя создать через `new`.

✅ **EnumSet / EnumMap** — высокопроизводительные коллекции для работы с Enum.

✅ **Singleton** — один объект на всю JVM. Используй `enum` для простоты.

✅ **Не злоупотребляй Singleton** — затрудняет тестирование. Spring предлагает DI вместо Singleton.

---

## 📌 Слайд 11: Домашнее задание

### Задание: Система ролей и разрешений

```java
// 1. Создать Enum Role с полями и методами:
enum Role {
    GUEST(0),
    USER(1),
    MODERATOR(2),
    ADMIN(3);
    // поле: int level
    // метод: boolean canAccess(Role required)
    // метод: boolean isHigherThan(Role other)
}

// 2. Создать Enum Permission с абстрактным методом:
enum Permission {
    READ { @Override public boolean check(Role role) { return role.getLevel() >= 0; } },
    WRITE { @Override public boolean check(Role role) { return role.getLevel() >= 1; } },
    DELETE { @Override public boolean check(Role role) { return role.getLevel() >= 2; } };

    public abstract boolean check(Role role);
}

// 3. Singleton ApplicationContext — хранит текущего пользователя
enum ApplicationContext {
    INSTANCE;
    // currentUser, currentRole
    // методы: login, logout, hasPermission
}
```

---

## ❓ Вопросы для самопроверки

1. Чем `enum` лучше констант `public static final int`?
2. Может ли `enum` реализовывать интерфейс?
3. Может ли `enum` наследоваться от класса?
4. В чём проблема потокобезопасности у «ленивого» Singleton?
5. Почему Singleton через `enum` защищён от атак через Reflection?
6. Назови три реальных примера использования Singleton.

---

## 📚 Дополнительные материалы

| Ресурс | Описание |
|---|---|
| [Oracle — Enum Types](https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html) | Официальная документация |
| «Effective Java» Item 3 — Joshua Bloch | Singleton через Enum |
| «Effective Java» Item 34 | Использование Enum вместо int констант |
| [Baeldung — Singleton](https://www.baeldung.com/java-singleton) | Все способы реализации |

---

*Лекция 4 из 19 | Курс: Введение в ООП на Java | Семестр 2*

