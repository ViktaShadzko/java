# Лекция 1: Введение в ООП. История и Парадигмы Программирования

---

## 🗂️ План лекции

1. Что такое парадигма программирования?
2. История развития языков программирования
3. Императивное программирование
4. Процедурное программирование
5. Объектно-ориентированное программирование (ООП)
6. Функциональное программирование
7. Декларативное программирование
8. Мультипарадигменные языки
9. Java и парадигмы
10. Итоги и выводы

---

## 📌 Слайд 1: Что такое парадигма программирования?

> **Парадигма программирования** — это стиль, метод и способ мышления при написании программ.

Парадигма определяет:
- Как **структурировать** код
- Как **описывать** поведение программы
- Как **управлять** данными и состоянием

### Аналогия:
> Парадигма — это как **архитектурный стиль** в строительстве.  
> Можно построить дом в стиле классики, хай-тека или минимализма — цель одна, подход разный.

---

## 📌 Слайд 2: История — кратко

```
1950-е  →  Машинный код и Ассемблер (низкоуровневое)
1960-е  →  FORTRAN, COBOL (процедурное)
1970-е  →  C, Pascal (структурное/процедурное)
1980-е  →  C++, Smalltalk (ООП)
1990-е  →  Java, Python, Ruby (мультипарадигменное)
2000-е  →  Scala, Kotlin, F# (функциональное + ООП)
2010-е  →  Rust, Swift, Go (современные подходы)
```

### Ключевая мысль:
> Каждая новая парадигма появлялась как **ответ на проблемы** предыдущей.

---

## 📌 Слайд 3: Императивное программирование

### Суть:
> **Говоришь компьютеру КАК делать** шаг за шагом.

Программа — это последовательность команд, которые изменяют **состояние** программы.

> 💡 Императивное программирование — это **самая старая** парадигма. Ассемблер — её чистейший пример.

### Ассемблер — прародитель императивного стиля:

Ассемблер — это язык, в котором каждая строка кода — это **буквально одна команда процессору**.  
Никакой абстракции: ты управляешь регистрами, памятью и переходами вручную.

```nasm
; Ассемблер x86 — сложить числа от 1 до 5
; Эквивалент: int sum = 0;
        for(int i=1; i<=5; i++) sum += i;

section .data
    result dd 0          ; int result = 0  (4 байта в памяти)

section .text
    global _start

_start:
    mov eax, 0           ; eax = 0  (регистр — аналог переменной sum)
    mov ecx, 1           ; ecx = 1  (счётчик i)

loop_start:
    cmp ecx, 5           ; сравнить i с 5
    jg  done             ; если i > 5 — прыгнуть на done (выйти из цикла)
    add eax, ecx         ; eax = eax + ecx  (sum += i)
    inc ecx              ; ecx++  (i++)
    jmp loop_start       ; безусловный прыжок в начало цикла

done:
    mov [result], eax    ; сохранить результат в память
    ; → result = 15
```

> ⚠️ Это **15 строк** ради простого цикла! Именно поэтому появились языки высокого уровня.

### Тот же код на Java — императивный стиль:
```java
int sum = 0;
int[] numbers = {1, 2, 3, 4, 5};

for (int i = 0; i < numbers.length; i++) {
    sum = sum + numbers[i]; // явно меняем состояние — как в ассемблере, но читаемо
}

System.out.println("Сумма: " + sum); // Сумма: 15
```

> Java по-прежнему **императивна по природе** — циклы, переменные, присваивания.  
> Но JVM скрывает от нас работу с регистрами и памятью.

### Связь Ассемблер → Java:

| Ассемблер | Java | Смысл |
|---|---|---|
| `mov eax, 0` | `int sum = 0;` | Инициализация переменной |
| `add eax, ecx` | `sum += i;` | Изменение состояния |
| `cmp ecx, 5` + `jg done` | `if (i > 5) break;` | Условный переход |
| `jmp loop_start` | `for`/`while` | Цикл |
| `mov [result], eax` | Автоматически (GC + стек) | Сохранение в память |

### 🌍 Представители языков:

| Язык | Годы | Особенность |
|---|---|---|
| **Ассемблер** | 1950-е — наши дни | Прямое управление регистрами и памятью. Используется в драйверах, прошивках, играх (оптимизация) |
| **Fortran** | 1957 | Первый язык высокого уровня. Создан для научных вычислений. До сих пор используется в суперкомпьютерах |
| **COBOL** | 1959 | Разработан для бизнес-задач. До сих пор работает в банках и страховых компаниях (300 млрд строк кода в мире!) |
| **BASIC** | 1964 | Создан для обучения. Первый язык Билла Гейтса и многих программистов 80-х |

### ✅ Плюсы:
- Полный контроль над выполнением (особенно в ассемблере — вплоть до байта)
- Максимальная производительность
- Понятная последовательность шагов

### ❌ Минусы:
- Много «шума» в коде (особенно в ассемблере)
- Сложно масштабировать
- Легко допустить ошибку при управлении состоянием
- Ассемблер: код привязан к конкретному процессору (x86, ARM — разный ассемблер)

### ⚠️ Главная проблема императивного стиля — изменяемое состояние

Чем больше программа — тем больше мест, где состояние **неожиданно меняется**. Это главный источник трудноуловимых багов.

```java
// ❌ Классическая ловушка — побочный эффект издалека
public class OrderProcessor {

    private static double totalRevenue = 0; // глобальное состояние

    public static double applyDiscount(double price, int userAge) {
        if (userAge < 18) {
            price = price * 0.9;       // изменили price — хорошо
            totalRevenue += price;     // ❌ побочный эффект — кто-то снаружи
        } else {                       //    не ожидает что totalRevenue меняется
            totalRevenue += price;     //    внутри "скидочного" метода!
        }
        return price;
    }

    public static void main(String[] args) {
        double p1 = applyDiscount(100.0, 16);  // скидка И меняет totalRevenue
        double p2 = applyDiscount(200.0, 25);  // без скидки И меняет totalRevenue

        // Что сейчас в totalRevenue? 90? 290? 300?
        // Нужно читать всю функцию чтобы понять — это и есть проблема
        System.out.println(totalRevenue); // 290.0 — но это неочевидно!
    }
}
```

> 🐛 **Этот тип ошибок** — «изменение состояния в неожиданном месте» — один из самых частых в больших императивных программах. Особенно опасен в **многопоточном коде**: два потока меняют одну переменную одновременно → непредсказуемый результат.

### 💡 Как функциональное программирование решает эту проблему:

```java
// ✅ Функциональный подход — нет побочных эффектов, нет сюрпризов

// Чистая функция: результат зависит ТОЛЬКО от аргументов
// Она никогда не меняет ничего снаружи
public static double applyDiscount(double price, int userAge) {
    return userAge < 18 ? price * 0.9 : price;
    // ← просто возвращает значение, ничего не трогает
}

// Накопление дохода — отдельная, явная операция
public static double calcRevenue(List<Double> prices) {
    return prices.stream()
        .reduce(0.0, Double::sum); // явно, без скрытых изменений
}

public static void main(String[] args) {
    List<Order> orders = Arrays.asList(
        new Order(100.0, 16),
        new Order(200.0, 25)
    );

    // Каждый шаг изолирован — легко читать, легко тестировать
    List<Double> discounted = orders.stream()
        .map(o -> applyDiscount(o.price, o.userAge))  // чистая функция
        .collect(Collectors.toList());

    double total = calcRevenue(discounted);
    System.out.println(total); // 290.0 — и это очевидно из кода!
}
```

| Проблема в императивном | Решение в процедурном                                |
|---|------------------------------------------------------|
| Глобальное изменяемое состояние | Иммутабельные данные — нельзя изменить               |
| Побочные эффекты в функциях | Чистые функции — только входные → выходные данные    |
| Непредсказуемость в многопоточности | Нет общего состояния → нет гонки потоков             |
| Сложно тестировать — зависит от состояния снаружи | Легко тестировать — дал аргументы → получил результат |

> 💬 **Итог:** Функциональное программирование не заменяет императивное — оно **дополняет** его там, где важна предсказуемость, параллелизм и тестируемость. Именно поэтому Java 8 добавила лямбды и Stream API.

---

## 📌 Слайд 4: Процедурное программирование

### Суть:
> Расширение императивного — код разбивается на **процедуры (функции)**.

Принцип: **разделяй и властвуй** — каждая задача выносится в отдельную функцию.

### Пример на Java:
```java
public class Calculator {

    public static int sum(int[] numbers) {
        int result = 0;
        for (int n : numbers) {
            result += n;
        }
        return result;
    }

    public static double average(int[] numbers) {
        return (double) sum(numbers) / numbers.length;
    }

    public static void main(String[] args) {
        int[] data = {10, 20, 30, 40, 50};
        System.out.println("Сумма: " + sum(data));       // 150
        System.out.println("Среднее: " + average(data)); // 30.0
    }
}
```

### ✅ Плюсы:
- Переиспользование кода через функции
- Легче читать и тестировать части программы
- Уменьшение дублирования

### ❌ Минусы:
- Глобальное состояние — источник ошибок
- При росте проекта — «спагетти-код»
- Данные и поведение разделены

### 🌍 Представители языков:

| Язык | Годы | Особенность |
|---|---|---|
| **C** | 1972 | Фундамент современного программирования. На нём написаны Linux, Windows, Python, Java (JVM). Всё ещё один из самых популярных языков |
| **Pascal** | 1970 | Создан специально для обучения. Строгая типизация, понятный синтаксис |
| **Delphi** | 1995 | Развитие Pascal. Популярен для desktop-приложений в 90-е и 2000-е |
| **PHP** | 1994 | Процедурные корни, веб-ориентирован. До сих пор работает 78% сайтов в интернете (WordPress) |
| **Lua** | 1993 | Встраиваемый скриптовый язык. Используется в играх (Roblox, World of Warcraft) |

### ⚠️ Главная проблема процедурного стиля — данные и поведение живут отдельно

Рассмотрим пример: есть три животных — **Собака**, **Обычный кот** и **Рыжий кот**.  
У каждого своя энергия и поведение зависящее от **времени суток**.

```
Собака     — энергия: 100  — днём охотится,        ночью спит
Обычный кот — энергия: 40  — днём спит,             ночью охотится
Рыжий кот  — энергия: 70  — днём дуреет,            ночью охотится с 50% шансом
```

```java
import java.util.Random;

public class ProceduralAnimals {

    // ── ДАННЫЕ — просто массивы, никакой связи с поведением ──────────
    static String[] names   = {"Рекс",   "Барсик",  "Рыжик"};
    static String[] types   = {"dog",    "cat",     "ginger_cat"};
    static int[]    energy  = {100,       40,         70};          // Рекс > Рыжик > Барсик

    // ── ФУНКЦИИ — отдельно от данных ─────────────────────────────────

    static boolean hasEnergy(int index, int required) {
        return energy[index] >= required;
    }

    static void hunt(int index) {
        if (!hasEnergy(index, 10)) {                // ← проверка перед действием
            System.out.println(names[index] + " слишком устал чтобы охотиться 😴  [энергия: " + energy[index] + "]");
            sleep(index);                           // нет сил — вынужден спать
            return;
        }
        energy[index] -= 10;
        System.out.println(names[index] + " охотится 🐾  [энергия: " + energy[index] + "]");
    }

    static void sleep(int index) {
        energy[index] += 20;
        System.out.println(names[index] + " спит 💤  [энергия: " + energy[index] + "]");
    }

    static void goWild(int index) {
        if (!hasEnergy(index, 5)) {                 // ← проверка перед действием
            System.out.println(names[index] + " слишком устал даже чтобы дуреть 😵  [энергия: " + energy[index] + "]");
            sleep(index);
            return;
        }
        energy[index] -= 5;
        System.out.println(names[index] + " дурееет 🌀  [энергия: " + energy[index] + "]");
    }

    // ── ЛОГИКА — разбросана по if-else, завязана на магические строки ─
    static void tick(int index, String timeOfDay, Random rnd) {
        System.out.print("[" + timeOfDay.toUpperCase() + "] ");

        if (types[index].equals("dog")) {
            if (timeOfDay.equals("day"))  hunt(index);
            else  sleep(index);

        } else if (types[index].equals("cat")) {
            if (timeOfDay.equals("night")) hunt(index);
            else  sleep(index);

        } else if (types[index].equals("ginger_cat")) {
            if (timeOfDay.equals("night")) {
                if (rnd.nextBoolean())     hunt(index);   // 50% — охотится
                else                       sleep(index);  // 50% — спит
            } else {
                goWild(index);                             // днём всегда дурееет
            }
        }
    }

    public static void main(String[] args) {
        Random rnd = new Random(42);
        String[] schedule = {"day", "night", "day", "night"};

        for (String time : schedule) {
            System.out.println("\n── " + time.toUpperCase() + " ──────────────────");
            for (int i = 0; i < names.length; i++) {
                tick(i, time, rnd);
            }
        }
    }
}
```

**Вывод:**
```
── DAY ──────────────────
[DAY] Рекс охотится 🐾  [энергия: 90]
[DAY] Барсик спит 💤  [энергия: 60]
[DAY] Рыжик дурееет 🌀  [энергия: 65]

── NIGHT ──────────────────
[NIGHT] Рекс спит 💤  [энергия: 110]
[NIGHT] Барсик охотится 🐾  [энергия: 50]
[NIGHT] Рыжик охотится 🐾  [энергия: 55]   ← повезло, 50%

── DAY ──────────────────
[DAY] Рекс охотится 🐾  [энергия: 100]
[DAY] Барсик спит 💤  [энергия: 70]
[DAY] Рыжик дурееет 🌀  [энергия: 50]

── NIGHT ──────────────────
[NIGHT] Рекс спит 💤  [энергия: 120]
[NIGHT] Барсик охотится 🐾  [энергия: 60]
[NIGHT] Рыжик спит 💤  [энергия: 70]   ← не повезло, 50%
```

> 🐛 **Что здесь не так?**  
> - Данные (`names`, `types`, `energy`) и поведение (`hunt`, `sleep`, `tick`) **никак не связаны** — любой может изменить `energy[1]` откуда угодно  
> - Добавить нового животного — нужно лезть в `tick()` и добавлять ещё один `else if`  
> - Опечатка в строке `"ginger_cat"` не поймается компилятором — упадёт только в рантайме  
> - Чем больше типов животных — тем длиннее и страшнее становится `tick()`

### 💡 Как это решает ООП — каждое животное знает своё поведение само:

```java
// ✅ В ООП-стиле данные и поведение живут вместе в одном объекте
// Добавить нового животного = создать новый класс, не трогая остальных

abstract class Animal {
    protected final String name;
    protected int energy;

    // минимальная энергия для каждого действия
    private static final int HUNT_COST   = 10;
    private static final int WILD_COST   = 5;
    private static final int SLEEP_GAIN  = 20;

    Animal(String name, int energy) {
        this.name   = name;
        this.energy = energy;
    }

    abstract void tick(String timeOfDay);   // каждый сам знает что делать

    protected boolean hasEnergy(int required) {
        return energy >= required;
    }

    protected void hunt() {
        if (!hasEnergy(HUNT_COST)) {        // ← единое место проверки для всех наследников
            System.out.println(name + " слишком устал чтобы охотиться 😴  [энергия: " + energy + "]");
            sleep();                         // нет сил — вынужден отдохнуть
            return;
        }
        energy -= HUNT_COST;
        System.out.println(name + " охотится 🐾  [энергия: " + energy + "]");
    }

    protected void sleep() {
        energy += SLEEP_GAIN;               // сон всегда доступен — восстанавливает силы
        System.out.println(name + " спит 💤  [энергия: " + energy + "]");
    }

    protected void goWild() {
        if (!hasEnergy(WILD_COST)) {        // ← даже на "подурачиться" нужны силы
            System.out.println(name + " слишком устал даже чтобы дуреть 😵  [энергия: " + energy + "]");
            sleep();
            return;
        }
        energy -= WILD_COST;
        System.out.println(name + " дурееет 🌀  [энергия: " + energy + "]");
    }
}

class Dog extends Animal {
    Dog(String name) { super(name, 100); }  // у собаки энергии больше всего

    @Override
    void tick(String timeOfDay) {
        System.out.print("[" + timeOfDay.toUpperCase() + "] ");
        if (timeOfDay.equals("day")) hunt(); else sleep();
        // hunt() сам проверит энергию — Dog об этом не думает
    }
}

class Cat extends Animal {
    Cat(String name) { super(name, 40); }   // у обычного кота меньше всего

    @Override
    void tick(String timeOfDay) {
        System.out.print("[" + timeOfDay.toUpperCase() + "] ");
        if (timeOfDay.equals("night")) hunt(); else sleep();
    }
}

class GingerCat extends Animal {
    private final Random rnd = new Random();

    GingerCat(String name) { super(name, 70); }  // рыжий — посередине

    @Override
    void tick(String timeOfDay) {
        System.out.print("[" + timeOfDay.toUpperCase() + "] ");
        if (timeOfDay.equals("night")) {
            if (rnd.nextBoolean()) hunt(); else sleep();  // 50/50
        } else {
            goWild();   // goWild() сам проверит энергию
        }
    }
}

// main — чистый, ничего не знает о деталях каждого животного
public class Main {
    public static void main(String[] args) {
        List<Animal> animals = Arrays.asList(
            new Dog("Рекс"),
            new Cat("Барсик"),
            new GingerCat("Рыжик")
        );

        for (String time : new String[]{"day", "night", "day", "night"}) {
            System.out.println("\n── " + time.toUpperCase() + " ──────────────────");
            for (Animal a : animals) a.tick(time);  // каждый знает сам что делать!
        }
    }
}
```

| Проблема в процедурном | Решение в ООП |
|---|---|
| Данные и поведение разделены — связь через индексы | Данные и поведение в одном объекте |
| `tick()` знает о всех типах — растёт с каждым новым | Каждый класс знает только о себе |
| Магические строки `"dog"`, `"cat"` — не проверяются компилятором | Типы проверяются на этапе компиляции |
| Добавить животное = изменить `tick()` | Добавить животное = новый класс, `tick()` не трогаем |
| Проверка энергии дублируется в каждой функции — легко забыть | Проверка в базовом `Animal.hunt()` — работает для **всех** наследников автоматически |

---

## 📌 Слайд 5: Объектно-ориентированное программирование (ООП)

### Суть:
> Программа моделирует **реальный мир** через объекты, у которых есть **данные** (поля) и **поведение** (методы).


### 4 кита ООП:
| Принцип | Описание |
|---|---|
| **Инкапсуляция** | Скрытие деталей реализации |
| **Наследование** | Переиспользование через иерархию классов |
| **Полиморфизм** | Один интерфейс — разные реализации |
| **Абстракция** | Выделение важного, сокрытие лишнего |

### Пример на Java:
```java
// Абстракция и Инкапсуляция
public class BankAccount {
    private String owner;
    private double balance; // скрытое состояние

    public BankAccount(String owner, double initialBalance) {
        this.owner = owner;
        this.balance = initialBalance;
    }

    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
            System.out.println("Пополнено: +" + amount);
        }
    }

    public void withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            System.out.println("Снято: -" + amount);
        } else {
            System.out.println("Недостаточно средств!");
        }
    }

    public double getBalance() {
        return balance;
    }
}

// Наследование и Полиморфизм
public class SavingsAccount extends BankAccount {
    private double interestRate;

    public SavingsAccount(String owner, double balance, double rate) {
        super(owner, balance);
        this.interestRate = rate;
    }

    public void applyInterest() {
        double interest = getBalance() * interestRate;
        deposit(interest);
        System.out.println("Начислены проценты: " + interest);
    }
}

// Использование
public class Main {
    public static void main(String[] args) {
        BankAccount account = new SavingsAccount("Иван", 1000, 0.05);
        account.deposit(500);
        account.withdraw(200);
        ((SavingsAccount) account).applyInterest();
        System.out.println("Баланс: " + account.getBalance());
    }
}
```

### ✅ Плюсы:
- Код отражает реальный мир — легко понять
- Модульность и переиспользование
- Легче поддерживать большие проекты
- Широкая поддержка (Java, C++, Python, C#)

### ❌ Минусы:
- Избыточность для простых задач
- Наследование может стать сложным («хрупкий базовый класс»)
- Трудно с параллельным программированием (изменяемое состояние)
- Сложнее в тестировании (зависимости между объектами)

### 🌍 Представители языков:

| Язык | Годы | Особенность |
|---|---|---|
| **Smalltalk** | 1972 | Первый «чистый» ООП-язык. Именно здесь появились понятия класса, объекта и сообщений |
| **C++** | 1985 | ООП поверх C. Используется в играх (Unreal Engine), системном ПО, высокочастотной торговле |
| **Java** | 1995 | «Write once, run anywhere». Основа корпоративного backend, Android. Наш язык курса ☕ |
| **Python** | 1991 | Мультипарадигменный, но ООП — основа. Лидер в Data Science, ML, автоматизации |
| **C#** | 2000 | Java-подобный язык от Microsoft. .NET, Unity (90% игр на Unity написаны на C#) |
| **Ruby** | 1995 | «Всё есть объект» — даже числа и `nil`. Известен фреймворком Ruby on Rails |
| **Kotlin** | 2016 | Современная замена Java. Официальный язык Android с 2017 года |

---

## 📌 Слайд 6: Функциональное программирование

### Суть:
> **Говоришь компьютеру ЧТО вычислить**, а не как.  
> Программа — это набор **чистых функций** без побочных эффектов.

### Ключевые концепции:
- **Чистые функции** — результат зависит только от аргументов
- **Иммутабельность** — данные не изменяются
- **Функции высшего порядка** — функции принимают и возвращают функции

### Пример на Java (с Java 8+):
```java
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FunctionalExample {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // Декларативно: ЧТО нужно сделать — без явных циклов
        int sumOfEvenSquares = numbers.stream()
            .filter(n -> n % 2 == 0)        // оставить чётные
            .map(n -> n * n)                 // возвести в квадрат
            .reduce(0, Integer::sum);        // сложить

        System.out.println("Сумма квадратов чётных: " + sumOfEvenSquares); // 220

        // Функция высшего порядка
        List<String> names = Arrays.asList("Анна", "Борис", "Виктор", "Алина");
        List<String> filtered = names.stream()
            .filter(name -> name.startsWith("А"))
            .sorted()
            .collect(Collectors.toList());

        System.out.println("Имена на 'А': " + filtered); // [Алина, Анна]
    }
}
```

### Чистая функция — пример:
```java
// ✅ Чистая функция — нет побочных эффектов
public static int multiply(int a, int b) {
    return a * b; // результат зависит только от a и b
}

// ❌ Нечистая функция — меняет внешнее состояние
private static int counter = 0;
public static int incrementAndGet() {
    return ++counter; // побочный эффект!
}
```

### ✅ Плюсы:
- Код легко тестировать (нет состояния = нет сюрпризов)
- Отлично подходит для параллельного выполнения
- Выразительный и краткий код
- Меньше ошибок, связанных с состоянием

### ❌ Минусы:
- Сложнее для новичков
- Может быть менее производительным (иммутабельность = много копий)
- Не интуитивно для задач с реальным состоянием (UI, БД)

### ⚠️ Главная проблема функционального стиля — реальный мир имеет состояние

Чистые функции и иммутабельность прекрасны для вычислений, но **реальные системы** — банки, игры, UI — по природе своей **stateful**: у пользователя есть сессия, у счёта — баланс, у игрока — позиция.

```java
// ❌ Проблема ФП — моделировать изменяемые сущности неудобно

// Иммутабельный подход: каждое изменение = новый объект
record BankAccountState(String owner, double balance) {}

// Пополнить счёт — создаём НОВЫЙ объект вместо изменения старого
static BankAccountState deposit(BankAccountState acc, double amount) {
    return new BankAccountState(acc.owner(), acc.balance() + amount);
}

static BankAccountState withdraw(BankAccountState acc, double amount) {
    if (amount > acc.balance())
        throw new IllegalStateException("Недостаточно средств");
    return new BankAccountState(acc.owner(), acc.balance() - amount);
}

public static void main(String[] args) {
    BankAccountState acc = new BankAccountState("Иван", 1000.0);

    // Каждая операция — новый объект в памяти
    acc = deposit(acc, 500.0);    // новый объект: balance=1500
    acc = withdraw(acc, 200.0);   // ещё один новый объект: balance=1300
    acc = deposit(acc, 100.0);    // ещё один: balance=1400

    // Вопрос: а где история операций? Как откатить? Кто "владеет" acc?
    // При 1000 транзакций в секунду — 1000 новых объектов в секунду → давление на GC
    System.out.println(acc.balance()); // 1400.0
}
```

> 🐛 **Проблема:** Для простых вычислений это отлично, но для долгоживущих сущностей (пользователь, заказ, игровой персонаж) создание нового объекта на каждое изменение — **расточительно по памяти** и **неестественно** для предметной области.

### 💡 Как это решается — **ООП инкапсулирует изменяемое состояние**:

```java
// ✅ ООП подход — состояние инкапсулировано, изменяется контролируемо

public class BankAccount {
    private final String owner;
    private double       balance;                    // изменяемое состояние
    private final List<String> history = new ArrayList<>(); // история операций

    public BankAccount(String owner, double initialBalance) {
        this.owner   = owner;
        this.balance = initialBalance;
        history.add("Открыт счёт: " + initialBalance);
    }

    public void deposit(double amount) {
        if (amount <= 0) throw new IllegalArgumentException("Сумма > 0");
        balance += amount;                           // изменяем состояние — это НОРМАЛЬНО
        history.add("Пополнение: +" + amount + " → " + balance);
    }

    public void withdraw(double amount) {
        if (amount > balance) throw new IllegalStateException("Недостаточно средств");
        balance -= amount;
        history.add("Снятие: -" + amount + " → " + balance);
    }

    // ФП-стиль внутри ООП — для аналитики используем Stream!
    public double getTotalDeposited() {
        return history.stream()
            .filter(h -> h.startsWith("Пополнение"))
            .mapToDouble(h -> Double.parseDouble(h.split("\\+")[1].split(" ")[0]))
            .sum();
    }

    public void printHistory() { history.forEach(System.out::println); }
    public double getBalance()  { return balance; }
}

// Использование — естественно и понятно
BankAccount acc = new BankAccount("Иван", 1000.0);
acc.deposit(500.0);
acc.withdraw(200.0);
acc.deposit(100.0);
acc.printHistory();
// Открыт счёт: 1000.0
// Пополнение: +500.0 → 1500.0
// Снятие: -200.0 → 1300.0
// Пополнение: +100.0 → 1400.0
```

| Проблема в ФП (иммутабельность) | Решение в ООП (инкапсуляция) |
|---|---|
| Новый объект на каждое изменение — нагрузка на GC | Один объект живёт и меняется контролируемо |
| Сложно хранить историю изменений | Объект сам ведёт историю внутри себя |
| Нет чёткого «владельца» состояния | Объект — единственный владелец своих данных |
| Неудобно моделировать сущности реального мира | Класс — прямое отражение предметной области |

> 💬 **Итог:** ФП и ООП — **не конкуренты, а союзники**. В реальных Java-приложениях граница чёткая: ООП отвечает за **структуру и состояние** (классы, сущности, сервисы), а ФП — за **трансформации данных** (фильтрация, маппинг, агрегация через Stream API). Spring Boot использует оба подхода одновременно.

### 🌍 Представители языков:

| Язык | Годы | Особенность |
|---|---|---|
| **Lisp** | 1958 | Старейший функциональный язык. Идеи Lisp живут в лямбдах Java и Python по сей день |
| **Haskell** | 1990 | Эталон чистого функционального программирования. Всё иммутабельно, побочных эффектов нет |
| **Erlang** | 1986 | Создан в Ericsson для телекома. Миллионы конкурентных процессов. Основа WhatsApp (2 млрд пользователей) |
| **Scala** | 2004 | ФП + ООП на JVM. Используется в Apache Spark (Big Data), Twitter, LinkedIn |
| **F#** | 2005 | Функциональный язык в экосистеме .NET от Microsoft |
| **Clojure** | 2007 | Диалект Lisp на JVM. Популярен в финтех и стартапах Силиконовой долины |
| **Java 8+** | 2014 | Добавил лямбды и Stream API — функциональный стиль в ООП-языке |

---

## 📌 Слайд 7: Декларативное программирование

### Суть:
> Описываешь **что нужно получить**, а не как это делать.

Декларативный подход — это **надстройка** над императивным.

### Примеры декларативного подхода:
```sql
-- SQL — чистый декларативный язык
SELECT name, salary
FROM employees
WHERE department = 'IT'
ORDER BY salary DESC;
```

```java
// Java Streams — декларативный стиль в Java
List<String> result = employees.stream()
    .filter(e -> e.getDepartment().equals("IT"))
    .sorted(Comparator.comparing(Employee::getSalary).reversed())
    .map(Employee::getName)
    .collect(Collectors.toList());
```

```java
// JPA — декларативный запрос
@Query("SELECT e FROM Employee e WHERE e.department = :dept ORDER BY e.salary DESC")
List<Employee> findByDepartmentOrderBySalaryDesc(@Param("dept") String dept);
```

### ✅ Плюсы:
- Читаемый код — близок к человеческому языку
- Меньше кода для выражения намерения
- Легче поддерживать

### ❌ Минусы:
- Меньше контроля над деталями выполнения
- Производительность зависит от реализации «под капотом»

### 🌍 Представители языков:

| Язык / Технология | Область | Особенность |
|---|---|---|
| **SQL** | Базы данных | Описываешь ЧТО получить, СУБД сама решает КАК. Используется повсеместно |
| **HTML** | Веб | Описываешь структуру страницы, браузер сам рисует |
| **CSS** | Веб | Описываешь стиль элементов, браузер сам применяет |
| **XSLT** | XML | Декларативные трансформации XML-документов |
| **Prolog** | ИИ, логика | Описываешь факты и правила, система сама строит цепочку выводов |
| **Terraform** | DevOps / IaC | Описываешь желаемую инфраструктуру, инструмент сам её создаёт |
| **Dockerfile** | DevOps | Описываешь образ — Docker сам собирает |
| **Spring аннотации** | Java Backend | `@Transactional`, `@Cacheable` — описываешь намерение, Spring делает всё сам |

---

## 📌 Слайд 8: Сравнительная таблица парадигм

| Парадигма | Ключевой вопрос | Состояние | Пример языка |
|---|---|---|---|
| **Императивная** | КАК делать? | Изменяемое | C, Assembler |
| **Процедурная** | КАК делать через функции? | Изменяемое | C, Pascal |
| **ООП** | КТО делает? | Инкапсулировано | Java, C++, C# |
| **Функциональная** | ЧТО вычислить? | Иммутабельное | Haskell, Scala |
| **Декларативная** | ЧТО получить? | Скрытое | SQL, HTML |

---

## 📌 Слайд 9: Java — мультипарадигменный язык

Java **изначально** создавалась как ООП-язык, но с версии 8 активно поддерживает несколько парадигм.

### Все парадигмы в одном файле:
```java
import java.util.*;
import java.util.stream.*;
import java.util.function.*;

public class AllParadigms {

    // === ПРОЦЕДУРНЫЙ СТИЛЬ ===
    public static int imperativeSum(List<Integer> list) {
        int sum = 0;
        for (int n : list) {
            sum += n; // явное управление состоянием
        }
        return sum;
    }

    // === ООП СТИЛЬ ===
    static class NumberCollection {
        private final List<Integer> numbers;

        public NumberCollection(List<Integer> numbers) {
            this.numbers = numbers;
        }

        public int sum() {
            return numbers.stream().mapToInt(Integer::intValue).sum();
        }

        public NumberCollection filterPositive() {
            return new NumberCollection(
                numbers.stream().filter(n -> n > 0).collect(Collectors.toList())
            );
        }
    }

    // === ФУНКЦИОНАЛЬНЫЙ СТИЛЬ ===
    static final Function<List<Integer>, Integer> functionalSum =
        list -> list.stream().reduce(0, Integer::sum);

    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, -2, 3, -4, 5);

        // Процедурный подход
        System.out.println("Процедурный: " + imperativeSum(numbers));

        // ООП подход
        NumberCollection collection = new NumberCollection(numbers);
        System.out.println("ООП: " + collection.filterPositive().sum());

        // Функциональный подход
        int result = numbers.stream()
            .filter(n -> n > 0)
            .reduce(0, Integer::sum);
        System.out.println("Функциональный: " + result);

        // Все три дают одинаковый результат для положительных чисел
    }
}
```

### Эволюция Java:
| Версия | Что добавили |
|---|---|
| Java 1.0 (1996) | ООП, классы, интерфейсы |
| Java 5 (2004) | Дженерики, Enum, for-each |
| **Java 8 (2014)** | Лямбды, Stream API, Optional — **функциональное ФП** |
| Java 14+ | Records — иммутабельные объекты |
| Java 17+ | Sealed classes, Pattern Matching |

---

## 📌 Слайд 10: Практическая задача — сравни подходы

### Задача: Найти топ-3 самых дорогих продукта из категории "Электроника"

```java
import java.util.*;
import java.util.stream.*;

class Product {
    String name;
    String category;
    double price;

    Product(String name, String category, double price) {
        this.name = name;
        this.category = category;
        this.price = price;
    }
}

public class ProductExample {
    public static void main(String[] args) {
        List<Product> products = Arrays.asList(
            new Product("Телефон", "Электроника", 599.99),
            new Product("Ноутбук", "Электроника", 1299.99),
            new Product("Книга", "Образование", 29.99),
            new Product("Наушники", "Электроника", 199.99),
            new Product("Планшет", "Электроника", 449.99),
            new Product("Ручка", "Канцелярия", 2.99),
            new Product("Монитор", "Электроника", 899.99)
        );

        // ============ ИМПЕРАТИВНЫЙ ПОДХОД ============
        List<Product> electronics = new ArrayList<>();
        for (Product p : products) {
            if (p.category.equals("Электроника")) {
                electronics.add(p);
            }
        }
        electronics.sort((a, b) -> Double.compare(b.price, a.price));
        List<Product> top3Imperative = electronics.subList(0, Math.min(3, electronics.size()));

        System.out.println("=== Императивный ===");
        for (Product p : top3Imperative) {
            System.out.println(p.name + " - $" + p.price);
        }

        // ============ ФУНКЦИОНАЛЬНЫЙ/ДЕКЛАРАТИВНЫЙ ПОДХОД ============
        System.out.println("\n=== Функциональный ===");
        products.stream()
            .filter(p -> p.category.equals("Электроника"))
            .sorted(Comparator.comparingDouble(Product::getPrice).reversed())  // ИСПРАВЛЕНО
            .limit(3)
            .forEach(p -> System.out.println(p.name + " - $" + p.price));
    }
}
```

> **Вывод:** Функциональный подход в 2 раза короче и читается как текст на русском:  
> *«Отфильтруй → Отсортируй → Возьми 3 → Выведи»*

---

## 📌 Слайд 11: Антипаттерны — чего избегать

### ❌ Глобальное состояние (антипаттерн процедурного стиля):
```java
// ПЛОХО — глобальное состояние
public class BadExample {
    public static int globalCounter = 0; // ❌

    public static void process(String data) {
        globalCounter++; // неявный побочный эффект
        System.out.println("Processing: " + data);
    }
}
```

### ❌ «Бог-класс» (антипаттерн ООП):
```java
// ПЛОХО — один класс делает всё
public class GodClass { // ❌
    public void connectToDatabase() { ... }
    public void sendEmail() { ... }
    public void calculateTax() { ... }
    public void generatePDF() { ... }
    public void validateUser() { ... }
    // 500 методов...
}
```

### ✅ Правильный подход — Single Responsibility:
```java
// ХОРОШО — каждый класс отвечает за одно
public class DatabaseConnector { ... }
public class EmailService { ... }
public class TaxCalculator { ... }
public class PdfGenerator { ... }
public class UserValidator { ... }
```

---

## 📌 Слайд 12: Итоги

### Что нужно запомнить:

✅ **Парадигма** — это способ мышления и структурирования кода

✅ **Нет «лучшей» парадигмы** — каждая решает свои задачи

✅ **Java поддерживает несколько парадигм**:
   - ООП — основа языка
   - Функциональное — с Java 8 (лямбды, Stream API)
   - Декларативное — SQL через JPA, аннотации Spring

✅ **Главная цель любой парадигмы** — написать код, который:
   - Легко **читать**
   - Легко **тестировать**
   - Легко **изменять**

---

## 📌 Слайд 13: Домашнее задание

### Задание:
Напишите программу на Java, которая обрабатывает список студентов:

```java
class Student {
    String name;
    int age;
    double gpa; // средний балл (0.0 - 10.0)
    String faculty;
}
```

Требования — реализовать **двумя способами** (императивный И функциональный):
1. Найти всех студентов факультета `"Информатика"` со средним баллом выше `7.0`
2. Отсортировать по баллу (по убыванию)
3. Вывести имена и баллы топ-5

### Бонус:
Добавить класс-обёртку `StudentRepository` в ООП-стиле с методами `findTopStudents(String faculty, double minGpa, int limit)`.

---

## 📚 Дополнительные материалы

| Ресурс | Описание |
|---|---|
| [Oracle Java Docs](https://docs.oracle.com/en/java/) | Официальная документация Java |
| «Clean Code» — Robert C. Martin | Про принципы написания хорошего кода |
| «Head First Java» | Введение в Java и ООП |
| [java-oop.ru](https://javarush.com) | JavaRush — практические задания |

---

## ❓ Вопросы для самопроверки

1. В чём разница между **императивным** и **декларативным** подходом?
2. Назови **4 принципа ООП** и приведи пример каждого.
3. Что такое **чистая функция**? Напиши пример.
4. Начиная с какой версии Java поддерживает **функциональное программирование**?
5. Почему **иммутабельность** важна в функциональном стиле?

---

*Лекция 1 из 19 | Курс: Введение в ООП на Java | Семестр 2*

