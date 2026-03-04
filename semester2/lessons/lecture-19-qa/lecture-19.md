# Лекция 19: Консультация и Q&A

---

## 🗂️ Что на этой лекции

1. Повторение ключевых тем курса
2. Разбор частых ошибок
3. Что изучать дальше
4. Советы для экзамена
5. Вопросы и ответы

---

## 📌 Слайд 1: Карта курса — что мы изучили

```
Семестр 2 — ООП на Java (19 лекций)

Часть 1: Основы ООП
├── Лек. 1  — Парадигмы программирования (Императивное, Процедурное, ООП, ФП)
├── Лек. 2  — Классы, объекты, инкапсуляция, наследование
├── Лек. 3  — Интерфейсы, абстрактные классы, полиморфизм
├── Лек. 4  — Enum и Singleton
└── Лек. 5  — Исключения и обработка ошибок

Часть 2: Типы и коллекции
├── Лек. 6  — Дженерики (Generics)
├── Лек. 7  — Коллекции (List, Set, Queue)
└── Лек. 8  — Мапы (Map)

Часть 3: Современная Java
├── Лек. 9  — Внутренние и анонимные классы
├── Лек. 10 — Лямбды, Comparable, Optional
└── Лек. 11 — Stream API

Часть 4: Архитектура
├── Лек. 12 — Модули и пакеты
└── Лек. 13 — Слоистая архитектура и MVC

Часть 5: Продвинутые темы
├── Лек. 14 — IO, Serializable, Cloneable
├── Лек. 15 — Локализация, DateTime
├── Лек. 16 — XML и JSON
├── Лек. 17 — Рефлексия и аннотации
└── Лек. 18 — Garbage Collector и память
```

---

## 📌 Слайд 2: Ключевые концепции — шпаргалка

### ООП (Лек. 2-3):
```java
// Инкапсуляция
private double balance;
public void deposit(double amount) { ... } // контролируемый доступ

// Наследование
class Dog extends Animal { ... }

// Полиморфизм
Animal a = new Dog("Рекс"); // переменная типа родителя → объект типа ребёнка
a.speak(); // вызывается Dog.speak(), не Animal.speak()!

// Абстракция
abstract class Shape { abstract double area(); }
interface Drawable { void draw(); }
```

### Коллекции (Лек. 7-8):
```java
List<String>   list    = new ArrayList<>();          // порядок + дубликаты
Set<String>    set     = new HashSet<>();             // уникальные
Map<String, Integer> map = new HashMap<>();          // ключ → значение
Deque<String>  stack   = new ArrayDeque<>();         // LIFO стек
Queue<String>  queue   = new LinkedList<>();         // FIFO очередь
```

### Stream API (Лек. 11):
```java
List<String> result = list.stream()
    .filter(s -> s.length() > 3)
    .map(String::toUpperCase)
    .sorted()
    .collect(Collectors.toList());
```

---

## 📌 Слайд 3: Типичные ошибки на экзамене

### ❌ Ошибка 1: NullPointerException
```java
// ❌ Всегда проверяй на null или используй Optional
String s = null;
s.length(); // NPE!

// ✅
if (s != null) s.length();
Optional.ofNullable(s).map(String::length).orElse(0);
```

### ❌ Ошибка 2: Изменение коллекции во время итерации
```java
// ❌
for (String item : list) {
    if (item.equals("удалить")) list.remove(item); // ConcurrentModificationException!
}

// ✅ Через итератор
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    if (it.next().equals("удалить")) it.remove();
}

// ✅ Через removeIf (Java 8+)
list.removeIf(item -> item.equals("удалить"));
```

### ❌ Ошибка 3: equals/hashCode не переопределены
```java
// ❌
Set<Point> set = new HashSet<>();
set.add(new Point(1, 2));
set.add(new Point(1, 2)); // дубликат не обнаружен!
System.out.println(set.size()); // 2 (должно быть 1)

// ✅ Переопредели equals() и hashCode()!
```

### ❌ Ошибка 4: Catching Exception слишком широко
```java
// ❌
try { ... }
catch (Exception e) { System.out.println("Ошибка"); } // теряем информацию!

// ✅
catch (FileNotFoundException e) { log.error("Файл не найден", e); }
catch (IOException e)           { log.error("IO ошибка", e); }
```

### ❌ Ошибка 5: Стрим после закрытия
```java
Stream<String> stream = list.stream().filter(s -> s.length() > 3);
stream.forEach(System.out::println); // OK
stream.count(); // ❌ IllegalStateException — стрим уже использован!
// Стрим можно использовать только ОДИН РАЗ
```

---

## 📌 Слайд 4: Часто задаваемые вопросы

### В чём разница между `==` и `equals()`?
```java
String a = new String("hello");
String b = new String("hello");

System.out.println(a == b);      // false (разные объекты в памяти)
System.out.println(a.equals(b)); // true  (одинаковое содержимое)

// Для примитивов — только ==
int x = 5, y = 5;
System.out.println(x == y); // true
```

### В чём разница между `abstract class` и `interface`?
| | Abstract Class | Interface |
|---|---|---|
| Поля | Любые | `public static final` |
| Конструктор | ✅ | ❌ |
| Наследование | Одиночное | Множественное |
| Реализация методов | Частичная | `default` (Java 8+) |

### Что такое `static` в контексте класса?
```java
class MyClass {
    static int count = 0;      // одна переменная на ВЕСЬ класс
    int id;                    // у каждого объекта своя

    static void staticMethod() { /* нет доступа к this! */ }
    void instanceMethod()      { /* есть доступ к this */ }
}
```

### Что такое `final`?
```java
final int x = 10;                  // переменная — нельзя изменить
final class MyClass { }            // класс — нельзя наследовать
final void myMethod() { }          // метод — нельзя переопределить
```

---

## 📌 Слайд 5: Что изучать дальше

### Следующий семестр / курсы:

```
1. Spring Framework & Spring Boot
   ├── IoC Container, Dependency Injection
   ├── Spring MVC — веб-приложения
   ├── Spring Data JPA — работа с БД
   └── Spring Security — аутентификация

2. Базы данных
   ├── SQL — основы
   ├── JDBC — прямая работа с БД из Java
   ├── JPA / Hibernate — ORM
   └── PostgreSQL / MySQL

3. Тестирование
   ├── JUnit 5 — unit тесты
   ├── Mockito — моки
   └── TestContainers — интеграционные тесты

4. Инструменты
   ├── Maven / Gradle — сборка
   ├── Git — версионирование
   ├── Docker — контейнеризация
   └── IntelliJ IDEA — продвинутые возможности

5. Алгоритмы и структуры данных
   ├── Сортировки (Quick, Merge, Heap)
   ├── Деревья (BST, AVL, Red-Black)
   ├── Графы (BFS, DFS, Dijkstra)
   └── Задачи с LeetCode / HackerRank
```

---

## 📌 Слайд 6: Советы для экзамена

### Как готовиться:

✅ Повтори **4 принципа ООП** с примерами (инкапсуляция, наследование, полиморфизм, абстракция)

✅ Знай разницу: **abstract class vs interface**, **overloading vs overriding**

✅ Умей объяснить **checked vs unchecked** исключения

✅ Знай основные **коллекции** и когда какую использовать

✅ Умей написать простой **Stream** с filter/map/collect

✅ Понимай что такое **Comparable** и **Comparator**

✅ Знай что такое **GC** и как работает память JVM

### На самом экзамене:

- Читай вопрос **до конца** перед ответом
- Начинай с **простых** вопросов
- Если пишешь код — добавляй **комментарии** к сложным местам
- Не знаешь ответ — пиши что знаешь **рядом** с темой

---

## 📌 Слайд 7: Мини-тест — проверь себя

```java
// Вопрос 1: Что выведет этот код?
String s1 = "hello";
String s2 = "hello";
String s3 = new String("hello");

System.out.println(s1 == s2);      // ?
System.out.println(s1 == s3);      // ?
System.out.println(s1.equals(s3)); // ?

// Вопрос 2: Что выведет?
List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5);
int result = nums.stream()
    .filter(n -> n % 2 == 0)
    .mapToInt(Integer::intValue)
    .sum();
System.out.println(result); // ?

// Вопрос 3: Найди ошибку
public class Stack<T> {
    private List<T> items = new ArrayList<>();

    public void push(T item) { items.add(item); }
    public T pop() { return items.remove(items.size()); } // ← ошибка!
}

// Вопрос 4: Почему этот Singleton не потокобезопасен?
public class Singleton {
    private static Singleton instance;
    private Singleton() {}
    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton(); // два потока могут зайти сюда одновременно!
        }
        return instance;
    }
}
```

### Ответы:
```
Вопрос 1: true, false, true
Вопрос 2: 6  (2 + 4)
Вопрос 3: items.size() → IndexOutOfBoundsException! Нужно items.size() - 1
Вопрос 4: Race condition при проверке instance == null
```

---

## 📌 Слайд 8: Ресурсы для продолжения

| Ресурс | Что изучать |
|---|---|
| [docs.oracle.com/java/](https://docs.oracle.com/en/java/) | Официальная документация |
| [spring.io/guides](https://spring.io/guides) | Spring Boot туториалы |
| [baeldung.com](https://baeldung.com) | Практические статьи по Java |
| «Effective Java» — Joshua Bloch | Лучшие практики Java |
| «Clean Code» — Robert C. Martin | Принципы хорошего кода |
| «Head First Design Patterns» | Паттерны проектирования |
| [leetcode.com](https://leetcode.com) | Алгоритмические задачи |
| [javarush.com](https://javarush.com) | Практика с гейм-элементами |

---

## 📌 Слайд 9: Итоги курса

### Что вы освоили за этот семестр:

✅ **Объектно-ориентированное мышление** — моделирование реального мира через классы

✅ **4 парадигмы программирования** — и когда какую применять

✅ **Коллекции и обобщения** — типобезопасная работа с данными

✅ **Функциональный стиль** — лямбды, стримы, Optional

✅ **Архитектурные принципы** — слоистая архитектура, SOLID, DI

✅ **Работа с данными** — файлы, JSON, XML, сериализация

✅ **Продвинутые темы** — рефлексия, аннотации, память JVM

### Это фундамент для:
> **Spring Boot · JPA/Hibernate · REST API · Микросервисы · Android**

---

## 📌 Слайд 10: Спасибо!

> Курс завершён! Вы проделали огромную работу.

**Лектор:** Виктор  
**Преподаватель практических занятий:** Многоуважаемая Анастасия Юрьевна

---

*Лекция 19 из 19 | Курс: Введение в ООП на Java | Семестр 2*

