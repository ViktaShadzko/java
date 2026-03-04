# Лекция 3: Интерфейсы и абстрактные классы. Полиморфизм, перегрузка и переопределение

---

## 🗂️ План лекции

1. Абстрактные классы — зачем нужны?
2. Интерфейсы — контракт без реализации
3. Абстрактный класс vs Интерфейс — когда что использовать
4. Полиморфизм — одно имя, разное поведение
5. Перегрузка методов (Overloading)
6. Переопределение методов (Overriding)
7. Перегрузка vs Переопределение — разница
8. Default-методы в интерфейсах (Java 8+)
9. Функциональные интерфейсы
10. Итоги и домашнее задание

---

## 📌 Слайд 1: Абстрактные классы

### Суть:
> **Абстрактный класс** — класс, который нельзя создать напрямую. Он задаёт **шаблон** для наследников.

### Когда нужен:
- Есть общая логика для нескольких классов
- Хочешь **принудить** наследников реализовать определённые методы
- Нужны и общие поля, и общие методы

```java
// ── Абстрактный класс ─────────────────────────────────────
public abstract class Shape {
    protected String color;
    protected String name;

    public Shape(String color, String name) {
        this.color = color;
        this.name  = name;
    }

    // Абстрактный метод — ОБЯЗАН быть переопределён в наследнике
    public abstract double area();
    public abstract double perimeter();

    // Обычный метод — общий для всех фигур
    public void printInfo() {
        System.out.printf("%s [%s] — площадь: %.2f, периметр: %.2f%n",
            name, color, area(), perimeter());
    }
}

// ── Конкретные наследники ─────────────────────────────────
public class Circle extends Shape {
    private double radius;

    public Circle(String color, double radius) {
        super(color, "Круг");
        this.radius = radius;
    }

    @Override public double area()      { return Math.PI * radius * radius; }
    @Override public double perimeter() { return 2 * Math.PI * radius; }
}

public class Rectangle extends Shape {
    private double width, height;

    public Rectangle(String color, double width, double height) {
        super(color, "Прямоугольник");
        this.width  = width;
        this.height = height;
    }

    @Override public double area()      { return width * height; }
    @Override public double perimeter() { return 2 * (width + height); }
}

// ── Полиморфизм через массив ──────────────────────────────
Shape[] shapes = {
    new Circle("красный", 5),
    new Rectangle("синий", 4, 6),
    new Circle("зелёный", 3)
};

for (Shape s : shapes) {
    s.printInfo(); // вызывается правильный area() для каждой фигуры!
}

// Shape s = new Shape("red", "test"); // ❌ нельзя создать абстрактный класс!
```

---

## 📌 Слайд 2: Интерфейсы

### Суть:
> **Интерфейс** — это **контракт**. Он описывает ЧТО умеет делать объект, но не КАК.

```java
// ── Интерфейс — только сигнатуры методов ─────────────────
public interface Drawable {
    void draw();                  // абстрактный по умолчанию
    void resize(double factor);
}

public interface Saveable {
    void save(String filename);
    void load(String filename);
}

// ── Класс может реализовывать НЕСКОЛЬКО интерфейсов ───────
public class Canvas implements Drawable, Saveable {

    private String content;

    @Override
    public void draw() {
        System.out.println("Рисую: " + content);
    }

    @Override
    public void resize(double factor) {
        System.out.println("Масштаб: " + factor);
    }

    @Override
    public void save(String filename) {
        System.out.println("Сохранено в: " + filename);
    }

    @Override
    public void load(String filename) {
        System.out.println("Загружено из: " + filename);
    }
}

// ── Использование через тип интерфейса ────────────────────
Drawable d = new Canvas();
d.draw();
d.resize(2.0);

Saveable s = new Canvas();
s.save("file.png");
```

### Поля в интерфейсе:
```java
public interface Constants {
    // Все поля в интерфейсе — автоматически public static final
    int MAX_SIZE    = 100;       // == public static final int MAX_SIZE = 100
    String VERSION  = "1.0.0";
}
```

---

## 📌 Слайд 3: Абстрактный класс vs Интерфейс

| Критерий | Абстрактный класс | Интерфейс |
|---|---|---|
| Создать объект | ❌ | ❌ |
| Наследование | Одиночное (`extends`) | Множественное (`implements`) |
| Поля | Любые | Только `public static final` |
| Конструктор | ✅ | ❌ |
| Реализация методов | Можно | `default` / `static` (Java 8+) |
| Когда использовать | Общее состояние + поведение | Контракт / способность |

### Правило выбора:
```
Вопрос: "Это иерархия ЧТО ЭТО ТАКОЕ?"
  → Да → Абстрактный класс (Animal → Dog)

Вопрос: "Это контракт ЧТО УМЕЕТ ДЕЛАТЬ?"
  → Да → Интерфейс (Flyable, Swimable, Drawable)
```

```java
// Абстрактный класс — иерархия
abstract class Vehicle { ... }
class Car extends Vehicle { ... }
class Truck extends Vehicle { ... }

// Интерфейс — способность
interface Electric { void charge(); }
interface Autonomous { void selfDrive(); }

// Tesla умеет И то, И то
class Tesla extends Car implements Electric, Autonomous {
    @Override public void charge()    { System.out.println("Заряжаюсь ⚡"); }
    @Override public void selfDrive() { System.out.println("Еду сам 🤖"); }
}
```

---

## 📌 Слайд 4: Полиморфизм

### Суть:
> **Полиморфизм** — способность объектов разных типов обрабатываться через единый интерфейс.

> 🔑 Ключевое: метод вызывается на основе **реального типа объекта**, а не типа переменной.

```java
public interface Animal {
    String speak();
    String getName();
}

public class Dog implements Animal {
    private String name;
    public Dog(String name) { this.name = name; }

    @Override public String speak()   { return "Гав!"; }
    @Override public String getName() { return name;   }
}

public class Cat implements Animal {
    private String name;
    public Cat(String name) { this.name = name; }

    @Override public String speak()   { return "Мяу!"; }
    @Override public String getName() { return name;   }
}

public class Parrot implements Animal {
    private String name;
    private String phrase;
    public Parrot(String name, String phrase) {
        this.name   = name;
        this.phrase = phrase;
    }

    @Override public String speak()   { return phrase + "! " + phrase + "!"; }
    @Override public String getName() { return name; }
}

// ── Полиморфизм в действии ────────────────────────────────
List<Animal> animals = Arrays.asList(
    new Dog("Рекс"),
    new Cat("Барсик"),
    new Parrot("Кеша", "Привет")
);

// Один и тот же код — разное поведение для каждого типа
for (Animal a : animals) {
    System.out.println(a.getName() + " говорит: " + a.speak());
}
// Рекс говорит: Гав!
// Барсик говорит: Мяу!
// Кеша говорит: Привет! Привет!
```

---

## 📌 Слайд 5: Перегрузка методов (Overloading)

### Суть:
> **Перегрузка** — несколько методов с **одним именем**, но разными параметрами. Решается на этапе **компиляции**.

```java
public class Calculator {

    // Перегрузка по типу параметра
    public int add(int a, int b) {
        System.out.println("int + int");
        return a + b;
    }

    public double add(double a, double b) {
        System.out.println("double + double");
        return a + b;
    }

    public String add(String a, String b) {
        System.out.println("String + String");
        return a + b;
    }

    // Перегрузка по количеству параметров
    public int add(int a, int b, int c) {
        return a + b + c;
    }

    // ❌ НЕ перегрузка — отличается только возвращаемый тип (compile error!)
    // public double add(int a, int b) { return (double)(a + b); }
}

Calculator calc = new Calculator();
calc.add(1, 2);          // int + int → 3
calc.add(1.5, 2.5);      // double + double → 4.0
calc.add("Hello", " World"); // String + String → "Hello World"
```

### Перегрузка конструкторов — уже знаем:
```java
public class User {
    String name;
    String email;
    int    age;

    public User(String name)                          { this(name, "", 0);   }
    public User(String name, String email)            { this(name, email, 0);}
    public User(String name, String email, int age)   {
        this.name  = name;
        this.email = email;
        this.age   = age;
    }
}
```

---

## 📌 Слайд 6: Переопределение методов (Overriding)

### Суть:
> **Переопределение** — дочерний класс заменяет реализацию метода родителя. Решается в **рантайме** (dynamic dispatch).

```java
public class Logger {
    public void log(String message) {
        System.out.println("[LOG] " + message);
    }
}

public class FileLogger extends Logger {
    private String filename;

    public FileLogger(String filename) {
        this.filename = filename;
    }

    @Override
    public void log(String message) {
        // Полностью заменяем реализацию родителя
        System.out.println("[FILE:" + filename + "] " + message);
    }
}

public class ColorLogger extends Logger {
    private String color;

    public ColorLogger(String color) {
        this.color = color;
    }

    @Override
    public void log(String message) {
        // Расширяем реализацию родителя
        super.log("[" + color + "] " + message);
    }
}

// ── Полиморфизм через переопределение ─────────────────────
Logger logger1 = new Logger();
Logger logger2 = new FileLogger("app.log");
Logger logger3 = new ColorLogger("RED");

logger1.log("Старт");       // [LOG] Старт
logger2.log("Старт");       // [FILE:app.log] Старт
logger3.log("Старт");       // [LOG] [RED] Старт
```

---

## 📌 Слайд 7: Overloading vs Overriding

| Критерий | Перегрузка (Overloading) | Переопределение (Overriding) |
|---|---|---|
| Где происходит | В одном классе | Родитель → Наследник |
| Имя метода | Одинаковое | Одинаковое |
| Параметры | **Разные** | **Одинаковые** |
| Тип возврата | Может быть разным | Одинаковый (или ковариантный) |
| Когда решается | **Compile time** | **Runtime** |
| `@Override` | Не используется | Рекомендуется |
| `static` методы | Можно перегрузить | Нельзя переопределить |

```java
class Parent {
    public void print(int x)    { System.out.println("Parent int: " + x);    } // 1
    public void print(String x) { System.out.println("Parent String: " + x); } // 2 — перегрузка
}

class Child extends Parent {
    @Override
    public void print(int x)    { System.out.println("Child int: " + x);     } // 3 — переопределение
    public void print(double x) { System.out.println("Child double: " + x);  } // 4 — перегрузка
}

Parent obj = new Child();
obj.print(42);       // Child int: 42     ← метод 3 (переопределение, runtime)
obj.print("hello");  // Parent String: hello ← метод 2 (перегрузка, compile time)
// obj.print(3.14); // ❌ compile error — Parent не знает о методе 4
```

---

## 📌 Слайд 8: Default-методы в интерфейсах (Java 8+)

> До Java 8 интерфейсы содержали **только абстрактные методы**. Java 8 добавила `default` и `static` методы.

```java
public interface Notification {

    // Абстрактный — обязателен для реализации
    void send(String message);

    // Default — реализация по умолчанию, можно переопределить
    default void sendWithTimestamp(String message) {
        String timestamp = LocalDateTime.now().toString();
        send("[" + timestamp + "] " + message);
    }

    // Static — утилитный метод интерфейса
    static boolean isValidMessage(String msg) {
        return msg != null && !msg.isBlank();
    }
}

// ── Реализации ────────────────────────────────────────────
public class EmailNotification implements Notification {
    private String email;

    public EmailNotification(String email) { this.email = email; }

    @Override
    public void send(String message) {
        System.out.println("Email → " + email + ": " + message);
    }
    // sendWithTimestamp — использует default реализацию
}

public class SmsNotification implements Notification {
    private String phone;

    public SmsNotification(String phone) { this.phone = phone; }

    @Override
    public void send(String message) {
        System.out.println("SMS → " + phone + ": " + message);
    }

    @Override // переопределяем default
    public void sendWithTimestamp(String message) {
        send("⏰ " + message); // своя реализация без timestamp
    }
}

// ── Использование ─────────────────────────────────────────
Notification email = new EmailNotification("user@example.com");
Notification sms   = new SmsNotification("+7-999-123-45-67");

if (Notification.isValidMessage("Привет!")) {
    email.sendWithTimestamp("Привет!"); // с timestamp
    sms.sendWithTimestamp("Привет!");   // без timestamp (своя реализация)
}
```

---

## 📌 Слайд 9: Функциональные интерфейсы

> **Функциональный интерфейс** — интерфейс с **одним абстрактным методом**. Может быть использован с лямбдами.

```java
// ── @FunctionalInterface — защита от случайного добавления методов ──
@FunctionalInterface
public interface Validator<T> {
    boolean validate(T value);

    // Default и static — разрешены
    default Validator<T> and(Validator<T> other) {
        return value -> this.validate(value) && other.validate(value);
    }
}

// ── Использование с лямбдами ──────────────────────────────
Validator<String> notEmpty  = s -> !s.isBlank();
Validator<String> notTooLong = s -> s.length() <= 100;
Validator<String> noSpaces  = s -> !s.contains(" ");

// Комбинирование через default метод
Validator<String> usernameValidator = notEmpty.and(notTooLong).and(noSpaces);

System.out.println(usernameValidator.validate("johndoe"));  // true
System.out.println(usernameValidator.validate(""));          // false
System.out.println(usernameValidator.validate("john doe"));  // false

// ── Встроенные функциональные интерфейсы (java.util.function) ──
Predicate<Integer> isPositive = n -> n > 0;
Function<String, Integer> strLen = String::length;
Consumer<String> printer = System.out::println;
Supplier<List<String>> listFactory = ArrayList::new;
```

---

## 📌 Слайд 10: Полная картина — пример

### Платёжная система:

```java
// ── Интерфейс — контракт ──────────────────────────────────
public interface PaymentMethod {
    boolean pay(double amount);
    String getName();

    default String receipt(double amount) {
        return String.format("Оплачено %.2f руб. через %s", amount, getName());
    }
}

// ── Абстрактный класс — общее состояние ───────────────────
public abstract class DigitalWallet implements PaymentMethod {
    protected String userId;
    protected double balance;

    public DigitalWallet(String userId, double balance) {
        this.userId  = userId;
        this.balance = balance;
    }

    protected boolean deduct(double amount) {
        if (balance >= amount) {
            balance -= amount;
            return true;
        }
        System.out.println("Недостаточно средств в " + getName());
        return false;
    }

    public double getBalance() { return balance; }
}

// ── Конкретные реализации ─────────────────────────────────
public class CreditCard implements PaymentMethod {
    private String cardNumber;
    private double limit;

    public CreditCard(String cardNumber, double limit) {
        this.cardNumber = cardNumber;
        this.limit      = limit;
    }

    @Override
    public boolean pay(double amount) {
        if (amount <= limit) {
            limit -= amount;
            System.out.println(receipt(amount));
            return true;
        }
        return false;
    }

    @Override public String getName() { return "Кредитная карта " + cardNumber; }
}

public class SberPay extends DigitalWallet {
    public SberPay(String userId, double balance) { super(userId, balance); }

    @Override
    public boolean pay(double amount) {
        boolean ok = deduct(amount);
        if (ok) System.out.println(receipt(amount));
        return ok;
    }

    @Override public String getName() { return "СберПэй (" + userId + ")"; }
}

// ── Полиморфизм ───────────────────────────────────────────
List<PaymentMethod> methods = Arrays.asList(
    new CreditCard("**** 1234", 50_000),
    new SberPay("user123", 10_000)
);

for (PaymentMethod pm : methods) {
    pm.pay(3_500);
}
```

---

## 📌 Слайд 11: Итоги

### Что нужно запомнить:

✅ **Абстрактный класс** — шаблон с частичной реализацией. Нельзя создать объект.

✅ **Интерфейс** — контракт. Класс может реализовывать несколько интерфейсов.

✅ **Полиморфизм** — один вызов, разное поведение в зависимости от реального типа.

✅ **Перегрузка** — одно имя, разные параметры, решается **компилятором**.

✅ **Переопределение** — наследник заменяет реализацию, решается в **рантайме**.

✅ **Default-методы** (Java 8+) — реализация в интерфейсе, можно переопределить.

✅ **@FunctionalInterface** — один абстрактный метод → можно использовать лямбды.

---

## 📌 Слайд 12: Домашнее задание

### Задание: Система уведомлений

```java
// Реализовать:
public interface Notifiable {
    void notify(String event, String message);
    default void notifyAll(List<String> events, String message) {
        events.forEach(e -> notify(e, message));
    }
}

// Классы:
// 1. EmailNotifier   — шлёт email
// 2. TelegramNotifier — шлёт в Telegram
// 3. LogNotifier     — записывает в лог

// abstract class BaseNotifier implements Notifiable {
//   Хранит список получателей (List<String> recipients)
//   Метод addRecipient(String r)
// }
```

1. Создать иерархию с `BaseNotifier` и тремя конкретными классами
2. Продемонстрировать полиморфизм через `List<Notifiable>`
3. Перегрузить метод `notify` — с приоритетом и без
4. **Бонус**: добавить `@FunctionalInterface MessageFormatter` для форматирования сообщений

---

## ❓ Вопросы для самопроверки

1. Чем **абстрактный класс** отличается от **интерфейса**?
2. Можно ли реализовать несколько интерфейсов? А унаследоваться от нескольких классов?
3. В чём разница между **перегрузкой** и **переопределением**?
4. Что такое `default` метод в интерфейсе?
5. Что такое **функциональный интерфейс**? Приведи пример.
6. Когда используется динамический dispatch (выбор метода в рантайме)?

---

## 📚 Дополнительные материалы

| Ресурс | Описание |
|---|---|
| [Oracle — Interfaces](https://docs.oracle.com/javase/tutorial/java/IandI/createinterface.html) | Официальная документация |
| [Oracle — Abstract Classes](https://docs.oracle.com/javase/tutorial/java/IandI/abstract.html) | Абстрактные классы |
| «Head First Java» — глава 8 | Интерфейсы и полиморфизм |
| [Baeldung — Polymorphism](https://www.baeldung.com/java-polymorphism) | Статья с примерами |

---

*Лекция 3 из 19 | Курс: Введение в ООП на Java | Семестр 2*

