# Лекция 2: Классы и объекты. Инкапсуляция и Наследование

---

## 🗂️ План лекции

1. Класс и объект — ключевые понятия ООП
2. Поля, методы, конструкторы
3. Ключевое слово `this`
4. Инкапсуляция — зачем скрывать данные?
5. Геттеры и сеттеры
6. Наследование — повторное использование кода
7. Ключевое слово `super`
8. Переопределение методов (`@Override`)
9. Финальные классы и методы (`final`)
10. Итоги и домашнее задание

---

## 📌 Слайд 1: Класс и объект

### Суть:
> **Класс** — это чертёж (шаблон).  
> **Объект** — это конкретный экземпляр, созданный по этому чертежу.

### Аналогия:
> Класс `Автомобиль` — это техническая документация.  
> Объект `myCar` — это конкретный автомобиль, который стоит у вас во дворе.

### Пример:
```java
// Класс — чертёж
public class Car {
    String brand;
    String model;
    int    year;
    double speed;

    void accelerate(double amount) {
        speed += amount;
        System.out.println(brand + " разгоняется до " + speed + " км/ч");
    }

    void brake() {
        speed = 0;
        System.out.println(brand + " остановился");
    }
}

// Объекты — конкретные экземпляры
public class Main {
    public static void main(String[] args) {
        Car car1 = new Car();       // создаём объект
        car1.brand = "Toyota";
        car1.model = "Camry";
        car1.year  = 2023;

        Car car2 = new Car();       // второй независимый объект
        car2.brand = "BMW";
        car2.model = "M3";
        car2.year  = 2024;

        car1.accelerate(60);        // Toyota разгоняется до 60.0 км/ч
        car2.accelerate(100);       // BMW разгоняется до 100.0 км/ч
        car1.brake();               // Toyota остановился

        System.out.println(car1.speed); // 0.0
        System.out.println(car2.speed); // 100.0  ← объекты независимы
    }
}
```

> ⚠️ `car1` и `car2` — **независимые объекты**. Изменение одного не влияет на другой.

---

## 📌 Слайд 2: Поля, методы, конструкторы

### Структура класса:

```java
public class Student {

    // ── ПОЛЯ (состояние объекта) ──────────────────────
    String name;           // имя
    int    age;            // возраст
    double gpa;            // средний балл

    // ── КОНСТРУКТОР (инициализация объекта) ──────────
    public Student(String name, int age, double gpa) {
        this.name = name;
        this.age  = age;
        this.gpa  = gpa;
    }

    // ── МЕТОДЫ (поведение объекта) ────────────────────
    public void study() {
        gpa += 0.1;
        System.out.println(name + " учится. Новый GPA: " + gpa);
    }

    public boolean isExcellent() {
        return gpa >= 9.0;
    }

    public String toString() {
        return "Student{name='" + name + "', age=" + age + ", gpa=" + gpa + "}";
    }
}

// Использование
Student alice = new Student("Алиса", 20, 8.5);
Student bob   = new Student("Боб",   22, 7.0);

alice.study();                               // Алиса учится. Новый GPA: 8.6
System.out.println(alice.isExcellent());     // false
System.out.println(bob);                     // Student{name='Боб', age=22, gpa=7.0}
```

### Конструктор по умолчанию:
```java
public class Point {
    int x;
    int y;

    // Если не написать конструктор — Java создаст такой автоматически:
    // public Point() {}  ← поля получат значения по умолчанию (0, null, false)
}

Point p = new Point();   // x=0, y=0
```

### Перегрузка конструкторов:
```java
public class Rectangle {
    double width;
    double height;

    public Rectangle() {                          // квадрат 1×1
        this(1.0, 1.0);
    }

    public Rectangle(double side) {               // квадрат
        this(side, side);
    }

    public Rectangle(double width, double height) { // прямоугольник
        this.width  = width;
        this.height = height;
    }

    public double area() {
        return width * height;
    }
}

Rectangle r1 = new Rectangle();        // 1.0 × 1.0
Rectangle r2 = new Rectangle(5.0);     // 5.0 × 5.0
Rectangle r3 = new Rectangle(3.0, 4.0);// 3.0 × 4.0

System.out.println(r3.area()); // 12.0
```

---

## 📌 Слайд 3: Ключевое слово `this`

> `this` — ссылка на **текущий объект**, внутри которого выполняется метод.

### Три способа использования `this`:

```java
public class Person {
    String name;
    int    age;

    // 1️⃣ this — различить поле и параметр с одинаковым именем
    public Person(String name, int age) {
        this.name = name;   // this.name — поле, name — параметр
        this.age  = age;
    }

    // 2️⃣ this() — вызов другого конструктора
    public Person(String name) {
        this(name, 0);      // вызываем конструктор выше
    }

    // 3️⃣ this — передать текущий объект как аргумент
    public void register(Registry registry) {
        registry.add(this); // передаём самого себя
    }

    public String greet() {
        return "Привет, я " + this.name + "!"; // this здесь необязателен
    }
}
```

---

## 📌 Слайд 4: Инкапсуляция

### Суть:
> **Инкапсуляция** — скрытие внутреннего состояния объекта и предоставление контролируемого доступа через методы.

### Зачем это нужно?

```java
// ❌ БЕЗ инкапсуляции — данные открыты, защиты нет
public class BankAccountBad {
    public double balance; // любой может изменить напрямую!
}

BankAccountBad acc = new BankAccountBad();
acc.balance = -1_000_000; // ❌ никто не проверяет — катастрофа!
```

```java
// ✅ С инкапсуляцией — данные защищены
public class BankAccount {
    private double balance;  // закрыто — доступ только через методы

    public BankAccount(double initialBalance) {
        if (initialBalance < 0)
            throw new IllegalArgumentException("Начальный баланс не может быть отрицательным");
        this.balance = initialBalance;
    }

    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Сумма должна быть > 0");
        balance += amount;
        System.out.println("Пополнено: +" + amount + ". Баланс: " + balance);
    }

    public void withdraw(double amount) {
        if (amount <= 0)       throw new IllegalArgumentException("Сумма должна быть > 0");
        if (amount > balance)  throw new IllegalStateException("Недостаточно средств");
        balance -= amount;
        System.out.println("Снято: -" + amount + ". Баланс: " + balance);
    }

    public double getBalance() {   // только читать, изменить нельзя
        return balance;
    }
}
```

### Модификаторы доступа:

| Модификатор | Класс | Пакет | Наследник | Все |
|---|:---:|:---:|:---:|:---:|
| `private`   | ✅ | ❌ | ❌ | ❌ |
| *(default)* | ✅ | ✅ | ❌ | ❌ |
| `protected` | ✅ | ✅ | ✅ | ❌ |
| `public`    | ✅ | ✅ | ✅ | ✅ |

> 💡 **Правило:** поля — `private`, методы доступа — `public`.

---

## 📌 Слайд 5: Геттеры и сеттеры

> **Геттер** — метод для чтения поля.  
> **Сеттер** — метод для записи поля с валидацией.

```java
public class Temperature {
    private double celsius; // всегда в Цельсиях

    public Temperature(double celsius) {
        setCelsius(celsius); // валидация через сеттер
    }

    // ── Геттеры ──────────────────────────────────────
    public double getCelsius() {
        return celsius;
    }

    public double getFahrenheit() {          // вычисляемое свойство
        return celsius * 9.0 / 5.0 + 32;
    }

    public double getKelvin() {
        return celsius + 273.15;
    }

    // ── Сеттер с валидацией ───────────────────────────
    public void setCelsius(double celsius) {
        if (celsius < -273.15)
            throw new IllegalArgumentException(
                "Температура не может быть ниже абсолютного нуля (-273.15°C)");
        this.celsius = celsius;
    }
}

Temperature t = new Temperature(100.0);
System.out.println(t.getCelsius());     //  100.0  °C
System.out.println(t.getFahrenheit());  //  212.0  °F
System.out.println(t.getKelvin());      //  373.15 K

t.setCelsius(-300); // ❌ IllegalArgumentException
```

### Когда НЕ нужен сеттер:
```java
public class ImmutablePoint {
    private final double x;  // final — нельзя изменить после создания
    private final double y;

    public ImmutablePoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() { return x; }
    public double getY() { return y; }

    // Вместо сеттера — метод, возвращающий новый объект
    public ImmutablePoint translate(double dx, double dy) {
        return new ImmutablePoint(x + dx, y + dy); // новый объект!
    }
}
```

---

## 📌 Слайд 6: Наследование

### Суть:
> **Наследование** позволяет создать новый класс на основе существующего, **переиспользовав** его поля и методы.

```
Animal          ← родительский класс (суперкласс, parent)
├── Dog         ← дочерний класс (подкласс, child)
├── Cat
└── Bird
    └── Parrot  ← наследование может быть многоуровневым
```

```java
// ── Родительский класс ────────────────────────────────────
public class Animal {
    protected String name;
    protected int    age;

    public Animal(String name, int age) {
        this.name = name;
        this.age  = age;
    }

    public void eat() {
        System.out.println(name + " ест");
    }

    public void sleep() {
        System.out.println(name + " спит");
    }

    public String getInfo() {
        return name + " (" + age + " лет)";
    }
}

// ── Дочерний класс ────────────────────────────────────────
public class Dog extends Animal {
    private String breed; // новое поле только у Dog

    public Dog(String name, int age, String breed) {
        super(name, age); // вызов конструктора Animal
        this.breed = breed;
    }

    public void bark() {  // новый метод только у Dog
        System.out.println(name + " лает: Гав!");
    }

    @Override
    public String getInfo() {  // переопределяем метод родителя
        return super.getInfo() + ", порода: " + breed;
    }
}

// ── Использование ─────────────────────────────────────────
Animal animal = new Animal("Животное", 5);
Dog    dog    = new Dog("Рекс", 3, "Лабрадор");

animal.eat();           // Животное ест
dog.eat();              // Рекс ест        ← унаследовано от Animal
dog.sleep();            // Рекс спит       ← унаследовано от Animal
dog.bark();             // Рекс лает: Гав! ← только у Dog

System.out.println(animal.getInfo()); // Животное (5 лет)
System.out.println(dog.getInfo());    // Рекс (3 лет), порода: Лабрадор
```

### Что наследуется, а что нет:

| Что | Наследуется? |
|---|:---:|
| `public` и `protected` поля и методы | ✅ |
| `private` поля | ❌ (недоступны, но существуют) |
| Конструкторы | ❌ (но вызываются через `super()`) |
| `static` члены | ✅ (но не переопределяются) |

---

## 📌 Слайд 7: Ключевое слово `super`

> `super` — ссылка на **родительский класс**. Используется для вызова его конструктора или методов.

```java
public class Vehicle {
    protected String brand;
    protected int    maxSpeed;

    public Vehicle(String brand, int maxSpeed) {
        this.brand    = brand;
        this.maxSpeed = maxSpeed;
    }

    public String describe() {
        return brand + ", макс. скорость: " + maxSpeed + " км/ч";
    }
}

public class ElectricCar extends Vehicle {
    private int batteryCapacity; // кВт·ч

    public ElectricCar(String brand, int maxSpeed, int batteryCapacity) {
        super(brand, maxSpeed);  // 1️⃣ вызов конструктора родителя — ПЕРВОЙ строкой!
        this.batteryCapacity = batteryCapacity;
    }

    @Override
    public String describe() {
        // 2️⃣ вызов метода родителя + расширение
        return super.describe() + ", батарея: " + batteryCapacity + " кВт·ч";
    }

    public int estimateRange() {
        return batteryCapacity * 6; // ~6 км на 1 кВт·ч
    }
}

ElectricCar tesla = new ElectricCar("Tesla", 250, 100);
System.out.println(tesla.describe());       // Tesla, макс. скорость: 250 км/ч, батарея: 100 кВт·ч
System.out.println(tesla.estimateRange()); // 600
```

> ⚠️ Вызов `super()` **должен быть первой строкой** в конструкторе дочернего класса.

---

## 📌 Слайд 8: Переопределение методов (`@Override`)

### Суть:
> **Переопределение** — дочерний класс даёт **свою реализацию** метода, объявленного в родителе.

```java
public class Shape {
    public double area() {
        return 0.0; // базовая реализация — «заглушка»
    }

    public String describe() {
        return "Фигура с площадью: " + area();
    }
}

public class Circle extends Shape {
    private double radius;

    public Circle(double radius) {
        this.radius = radius;
    }

    @Override                            // аннотация — защита от опечаток
    public double area() {
        return Math.PI * radius * radius;
    }
}

public class Triangle extends Shape {
    private double base;
    private double height;

    public Triangle(double base, double height) {
        this.base   = base;
        this.height = height;
    }

    @Override
    public double area() {
        return 0.5 * base * height;
    }
}

// Полиморфизм в действии!
Shape[] shapes = {
    new Circle(5),
    new Triangle(6, 4),
    new Shape()
};

for (Shape s : shapes) {
    System.out.println(s.describe());
}
// Фигура с площадью: 78.53981633974483
// Фигура с площадью: 12.0
// Фигура с площадью: 0.0
```

### Правила переопределения:

| Правило | Описание |
|---|---|
| Подпись метода | Должна быть **точно такой же** (имя + параметры) |
| Тип возврата | Может быть **ковариантным** (подтип) |
| Модификатор доступа | Можно сделать **шире**, но не уже |
| `@Override` | Не обязателен, но **рекомендуется** всегда |
| `private` методы | **Не переопределяются** (скрываются) |

---

## 📌 Слайд 9: `final` — запрет изменений

```java
// ── final КЛАСС — нельзя наследовать ─────────────────────
public final class ImmutableId {
    private final long value;

    public ImmutableId(long value) {
        this.value = value;
    }

    public long getValue() { return value; }
}

// class MyId extends ImmutableId {} // ❌ compile error — нельзя!


// ── final МЕТОД — нельзя переопределить ──────────────────
public class Account {
    private double balance;

    public final double getBalance() { // нельзя переопределить
        return balance;
    }

    public void deposit(double amount) { // можно переопределить
        balance += amount;
    }
}


// ── final ПОЛЕ — нельзя изменить после инициализации ─────
public class Config {
    public static final double TAX_RATE  = 0.20;    // константа
    public static final String APP_NAME  = "MyApp"; // константа

    private final String userId;  // задаётся один раз в конструкторе

    public Config(String userId) {
        this.userId = userId;
        // this.userId = "other"; // ❌ compile error
    }
}
```

### Зачем `final`?
- **Безопасность**: предотвращает непреднамеренное изменение поведения
- **Читаемость**: явно сигнализирует о намерениях
- **Оптимизация**: JVM может оптимизировать `final` методы

---

## 📌 Слайд 10: Полная иерархия — пример

### Система сотрудников компании:

```java
// ── Базовый класс ─────────────────────────────────────────
public class Employee {
    protected String name;
    protected String department;
    protected double baseSalary;

    public Employee(String name, String department, double baseSalary) {
        this.name       = name;
        this.department = department;
        this.baseSalary = baseSalary;
    }

    public double calculateSalary() {
        return baseSalary;
    }

    public String getInfo() {
        return String.format("[%s] %s — %.2f руб.", department, name, calculateSalary());
    }
}

// ── Менеджер — с бонусом ──────────────────────────────────
public class Manager extends Employee {
    private double bonusPercent;

    public Manager(String name, String department,
                   double baseSalary, double bonusPercent) {
        super(name, department, baseSalary);
        this.bonusPercent = bonusPercent;
    }

    @Override
    public double calculateSalary() {
        return baseSalary + baseSalary * bonusPercent / 100.0;
    }
}

// ── Разработчик — с доплатой за проекты ──────────────────
public class Developer extends Employee {
    private int    completedProjects;
    private double projectBonus;

    public Developer(String name, double baseSalary,
                     int completedProjects, double projectBonus) {
        super(name, "IT", baseSalary);
        this.completedProjects = completedProjects;
        this.projectBonus      = projectBonus;
    }

    @Override
    public double calculateSalary() {
        return baseSalary + completedProjects * projectBonus;
    }
}

// ── Стажёр — получает долю от базовой ────────────────────
public class Intern extends Employee {
    private double rate; // 0.0 — 1.0

    public Intern(String name, String department,
                  double baseSalary, double rate) {
        super(name, department, baseSalary);
        this.rate = rate;
    }

    @Override
    public double calculateSalary() {
        return baseSalary * rate;
    }
}

// ── Расчёт зарплат через полиморфизм ─────────────────────
public class Payroll {
    public static void main(String[] args) {
        List<Employee> staff = Arrays.asList(
            new Manager("Анна",     "Продажи", 80_000, 20),
            new Developer("Иван",             100_000, 3, 5_000),
            new Intern("Мария",    "IT",       40_000, 0.5),
            new Developer("Пётр",             120_000, 5, 5_000)
        );

        double total = 0;
        for (Employee e : staff) {
            System.out.println(e.getInfo());
            total += e.calculateSalary();
        }
        System.out.printf("%nИтого ФОТ: %.2f руб.%n", total);
    }
}
```

**Вывод:**
```
[Продажи] Анна — 96000.00 руб.
[IT] Иван — 115000.00 руб.
[IT] Мария — 20000.00 руб.
[IT] Пётр — 145000.00 руб.

Итого ФОТ: 376000.00 руб.
```

---

## 📌 Слайд 11: Типичные ошибки при наследовании

### ❌ Ошибка 1: Забыть вызвать `super()` в конструкторе:
```java
public class Cat extends Animal {
    private String color;

    public Cat(String name, int age, String color) {
        // ❌ Если у Animal нет конструктора без аргументов — compile error!
        // super(name, age);  ← нужно добавить!
        this.color = color;
    }
}
```

### ❌ Ошибка 2: Изменить сигнатуру вместо переопределения:
```java
public class Bird extends Animal {

    // ❌ Это НЕ переопределение — это новый метод (другой параметр)!
    public void eat(String food) {
        System.out.println(name + " ест " + food);
    }

    // ✅ Правильное переопределение — та же сигнатура
    @Override
    public void eat() {
        System.out.println(name + " клюёт зерно");
    }
}
```

### ❌ Ошибка 3: Сужение модификатора доступа:
```java
public class Wolf extends Animal {

    // ❌ Compile error — нельзя сузить public → protected!
    @Override
    protected String getInfo() {
        return "Волк: " + name;
    }
}
```

---

## 📌 Слайд 12: Итоги

### Что нужно запомнить:

✅ **Класс** — шаблон, **объект** — экземпляр. Каждый объект независим.

✅ **Конструктор** инициализирует объект. Можно перегружать.

✅ **`this`** — ссылка на текущий объект, **`super`** — на родительский.

✅ **Инкапсуляция**: поля `private` + методы `public`. Защищает данные от некорректного изменения.

✅ **Наследование** (`extends`): дочерний класс получает всё от родителя и может добавить своё.

✅ **`@Override`**: явное переопределение метода родителя. Ставь всегда!

✅ **`final`**: запрещает наследование (класс), переопределение (метод) или изменение (поле).

---

## 📌 Слайд 13: Домашнее задание

### Задание: Система фигур с вычислением площади и периметра

Реализуйте иерархию классов:

```
Shape               ← базовый класс
├── Circle          ← круг
├── Rectangle       ← прямоугольник
│   └── Square      ← квадрат (частный случай прямоугольника)
└── Triangle        ← треугольник
```

#### Требования:

```java
public class Shape {
    protected String color;

    public Shape(String color) { ... }

    public double area()      { return 0; }  // переопределить в наследниках
    public double perimeter() { return 0; }  // переопределить в наследниках

    public void printInfo() {
        System.out.printf("%s | цвет: %s | площадь: %.2f | периметр: %.2f%n",
            getClass().getSimpleName(), color, area(), perimeter());
    }
}
```

1. Реализовать `Circle`, `Rectangle`, `Square`, `Triangle` — с полями, конструкторами, `@Override area()` и `perimeter()`
2. `Square` должен наследовать `Rectangle` и использовать `super(side, side)`
3. Создать массив `Shape[]` из 5 разных фигур и вывести информацию о каждой
4. Найти фигуру с **максимальной площадью** (процедурно — через цикл)

#### Бонус:
Найти фигуру с максимальной площадью **функционально** через Stream API.

---

## ❓ Вопросы для самопроверки

1. В чём разница между **классом** и **объектом**?
2. Зачем нужны **модификаторы доступа**? Какой из них самый строгий?
3. Что делает ключевое слово `super` в конструкторе?
4. Можно ли переопределить `private` метод родителя?
5. Что произойдёт, если поставить `final` на класс?
6. Какую проблему решает **инкапсуляция**? Приведи пример без неё.

---

## 📚 Дополнительные материалы

| Ресурс | Описание |
|---|---|
| [Oracle — Classes](https://docs.oracle.com/javase/tutorial/java/javaOO/classes.html) | Официальная документация по классам |
| [Oracle — Inheritance](https://docs.oracle.com/javase/tutorial/java/IandI/subclasses.html) | Официальная документация по наследованию |
| «Head First Java» — глава 7–8 | Наследование и полиморфизм |
| «Clean Code» — глава 10 | Принципы дизайна классов |

---

*Лекция 2 из 19 | Курс: Введение в ООП на Java | Семестр 2*

