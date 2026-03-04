# Лекция 11: Stream API и операции над коллекциями

---

## 🗂️ План лекции

1. Что такое Stream?
2. Создание Stream
3. Промежуточные операции
4. Терминальные операции
5. Collectors — сбор результатов
6. Параллельные стримы
7. Специализированные стримы (IntStream, LongStream)
8. Практические примеры
9. Итоги и домашнее задание

---

## 📌 Слайд 1: Что такое Stream?

> **Stream** — последовательность элементов с поддержкой операций. Stream **не хранит** данные — он их **обрабатывает**.

```
Источник (Collection, Array, ...)
    ↓
Промежуточные операции (filter, map, sorted, ...)  — ленивые!
    ↓
Терминальная операция (collect, count, forEach, ...) — запускает всё
```

```java
// ── Без Stream (императивный стиль) ───────────────────────
List<String> names = Arrays.asList("Иван", "Мария", "Пётр", "Анна", "Михаил");
List<String> result = new ArrayList<>();
for (String name : names) {
    if (name.length() > 4) {
        result.add(name.toUpperCase());
    }
}
Collections.sort(result);

// ── С Stream (декларативный стиль) ───────────────────────
List<String> result2 = names.stream()          // создать поток
    .filter(name -> name.length() > 4)          // оставить длинные
    .map(String::toUpperCase)                   // в верхний регистр
    .sorted()                                   // отсортировать
    .collect(Collectors.toList());              // собрать в список

System.out.println(result2); // [МАРИЯ, МИХАИЛ]
```

---

## 📌 Слайд 2: Создание Stream

```java
// ── Из коллекции ─────────────────────────────────────────
List<String> list = Arrays.asList("a", "b", "c");
Stream<String> s1 = list.stream();
Stream<String> s2 = list.parallelStream(); // параллельный

// ── Из массива ────────────────────────────────────────────
String[] arr = {"x", "y", "z"};
Stream<String> s3 = Arrays.stream(arr);
Stream<String> s4 = Arrays.stream(arr, 1, 3); // подмассив [1, 3)

// ── Из значений ───────────────────────────────────────────
Stream<String> s5 = Stream.of("один", "два", "три");
Stream<Integer> s6 = Stream.of(1, 2, 3, 4, 5);

// ── Генераторы ────────────────────────────────────────────
Stream<Integer> infinite1 = Stream.iterate(0, n -> n + 2);   // 0, 2, 4, 6...
Stream<Integer> infinite2 = Stream.iterate(0, n -> n < 100, n -> n + 10); // Java 9+
Stream<Double>  randoms   = Stream.generate(Math::random);

// Брать конечное количество из бесконечного
infinite1.limit(5).forEach(System.out::println); // 0 2 4 6 8

// ── Из строки (символы) ───────────────────────────────────
IntStream chars = "Hello".chars(); // IntStream символов

// ── Пустой stream ─────────────────────────────────────────
Stream<String> empty = Stream.empty();
```

---

## 📌 Слайд 3: Промежуточные операции

```java
List<Integer> numbers = Arrays.asList(5, 3, 8, 1, 9, 2, 7, 4, 6, 3, 1);

// ── filter — оставить элементы по условию ────────────────
numbers.stream()
    .filter(n -> n > 5)
    .forEach(System.out::print); // 8 9 7 6

// ── map — преобразовать каждый элемент ───────────────────
numbers.stream()
    .map(n -> n * n)
    .forEach(System.out::print); // 25 9 64 1 81 4 49 16 36 9 1

// ── distinct — уникальные элементы ───────────────────────
numbers.stream()
    .distinct()
    .forEach(System.out::print); // 5 3 8 1 9 2 7 4 6

// ── sorted — сортировка ───────────────────────────────────
numbers.stream()
    .sorted()
    .forEach(System.out::print); // 1 1 2 3 3 4 5 6 7 8 9

numbers.stream()
    .sorted(Comparator.reverseOrder())
    .forEach(System.out::print); // 9 8 7 6 5 4 3 3 2 1 1

// ── limit / skip — срез ───────────────────────────────────
numbers.stream().limit(3).forEach(System.out::print); // 5 3 8
numbers.stream().skip(3).forEach(System.out::print);  // 1 9 2 7 4 6 3 1

// ── flatMap — развернуть вложенные коллекции ─────────────
List<List<Integer>> nested = Arrays.asList(
    Arrays.asList(1, 2, 3),
    Arrays.asList(4, 5),
    Arrays.asList(6, 7, 8, 9)
);
nested.stream()
    .flatMap(Collection::stream)  // List<List<>> → Stream<Integer>
    .forEach(System.out::print);  // 1 2 3 4 5 6 7 8 9

// ── peek — просмотр без изменений (для отладки) ──────────
numbers.stream()
    .peek(n -> System.out.print("before:" + n + " "))
    .filter(n -> n > 5)
    .peek(n -> System.out.print("after:" + n + " "))
    .collect(Collectors.toList());
```

---

## 📌 Слайд 4: Терминальные операции

```java
List<Integer> nums = Arrays.asList(1, 2, 3, 4, 5);

// ── Агрегирование ─────────────────────────────────────────
long   count = nums.stream().count();               // 5
int    sum   = nums.stream().mapToInt(i -> i).sum();// 15
double avg   = nums.stream().mapToInt(i -> i).average().getAsDouble(); // 3.0
int    max   = nums.stream().mapToInt(i -> i).max().getAsInt(); // 5
int    min   = nums.stream().mapToInt(i -> i).min().getAsInt(); // 1

// ── reduce — свёртка ──────────────────────────────────────
int product = nums.stream().reduce(1, (a, b) -> a * b); // 120
Optional<Integer> sum2 = nums.stream().reduce(Integer::sum); // Optional[15]

// ── find ──────────────────────────────────────────────────
Optional<Integer> first = nums.stream().filter(n -> n > 3).findFirst(); // Optional[4]
Optional<Integer> any   = nums.stream().filter(n -> n > 3).findAny();   // любой > 3

// ── match ─────────────────────────────────────────────────
boolean anyPositive  = nums.stream().anyMatch(n -> n > 0);   // true — хоть один
boolean allPositive  = nums.stream().allMatch(n -> n > 0);   // true — все
boolean noneNegative = nums.stream().noneMatch(n -> n < 0);  // true — ни одного

// ── forEach ───────────────────────────────────────────────
nums.stream().forEach(System.out::println);
nums.stream().forEachOrdered(System.out::println); // гарантированный порядок

// ── toArray ───────────────────────────────────────────────
Integer[] array = nums.stream().toArray(Integer[]::new);
Object[]  arr   = nums.stream().toArray();
```

---

## 📌 Слайд 5: Collectors

```java
List<Student> students = Arrays.asList(
    new Student("Иван",   "IT",   8.5),
    new Student("Мария",  "IT",   9.2),
    new Student("Пётр",   "Math", 7.8),
    new Student("Анна",   "Math", 8.9),
    new Student("Дмитрий","IT",   6.5)
);

// ── Сбор в коллекции ─────────────────────────────────────
List<String>   names   = students.stream().map(Student::getName).collect(Collectors.toList());
Set<String>    nameSet = students.stream().map(Student::getName).collect(Collectors.toSet());
List<Student>  sorted  = students.stream().sorted().collect(Collectors.toUnmodifiableList());

// ── Строка ────────────────────────────────────────────────
String nameStr = students.stream()
    .map(Student::getName)
    .collect(Collectors.joining(", ", "[", "]")); // [Иван, Мария, Пётр, Анна, Дмитрий]

// ── Группировка ───────────────────────────────────────────
Map<String, List<Student>> byFaculty = students.stream()
    .collect(Collectors.groupingBy(Student::getFaculty));
// {IT=[Иван, Мария, Дмитрий], Math=[Пётр, Анна]}

// Группировка + подсчёт
Map<String, Long> countByFaculty = students.stream()
    .collect(Collectors.groupingBy(Student::getFaculty, Collectors.counting()));
// {IT=3, Math=2}

// Группировка + средний GPA
Map<String, Double> avgGpaByFaculty = students.stream()
    .collect(Collectors.groupingBy(
        Student::getFaculty,
        Collectors.averagingDouble(Student::getGpa)));
// {IT=8.07, Math=8.35}

// ── Разбивка по условию (partitioningBy) ─────────────────
Map<Boolean, List<Student>> partition = students.stream()
    .collect(Collectors.partitioningBy(s -> s.getGpa() >= 8.5));
// {true=[Иван, Мария, Анна], false=[Пётр, Дмитрий]}

// ── Статистика ────────────────────────────────────────────
DoubleSummaryStatistics stats = students.stream()
    .collect(Collectors.summarizingDouble(Student::getGpa));
System.out.println(stats.getAverage()); // 8.18
System.out.println(stats.getMax());     // 9.2
System.out.println(stats.getMin());     // 6.5
```

---

## 📌 Слайд 6: Специализированные стримы

```java
// ── IntStream — работа с int без boxing ──────────────────
IntStream range    = IntStream.range(1, 6);     // 1, 2, 3, 4, 5
IntStream rangeClosed = IntStream.rangeClosed(1, 5); // 1, 2, 3, 4, 5

int sum = IntStream.rangeClosed(1, 100).sum();  // 5050 — сумма от 1 до 100!

// Статистика
IntSummaryStatistics stats = IntStream.of(3, 1, 4, 1, 5, 9, 2, 6)
    .summaryStatistics();
System.out.println(stats.getMax());     // 9
System.out.println(stats.getAverage()); // 3.875

// Конвертация
List<Integer> list = IntStream.range(1, 6)
    .boxed()                            // int → Integer
    .collect(Collectors.toList());

int[] arr = IntStream.range(1, 6).toArray(); // [1, 2, 3, 4, 5]

// ── mapToInt — переход к IntStream ───────────────────────
List<String> words = Arrays.asList("hello", "world", "java");
int totalLength = words.stream()
    .mapToInt(String::length)    // Stream<String> → IntStream
    .sum();                      // 14
```

---

## 📌 Слайд 7: Параллельные стримы

```java
List<Integer> bigList = IntStream.rangeClosed(1, 1_000_000)
    .boxed()
    .collect(Collectors.toList());

// ── Последовательный стрим ────────────────────────────────
long sum1 = bigList.stream()
    .mapToLong(Integer::longValue)
    .sum();

// ── Параллельный стрим — автоматически несколько потоков ─
long sum2 = bigList.parallelStream()
    .mapToLong(Integer::longValue)
    .sum();

System.out.println(sum1 == sum2); // true — результат одинаков

// ⚠️ Осторожно с порядком!
List<Integer> ordered = new ArrayList<>();
bigList.stream().limit(10).forEach(ordered::add);    // порядок сохранён

List<Integer> unordered = new ArrayList<>();
// ❌ Не делай так — параллельный поток не гарантирует порядок!
bigList.parallelStream().limit(10).forEach(unordered::add);

// ✅ Если нужен порядок — forEachOrdered
bigList.parallelStream().limit(10).forEachOrdered(System.out::println);

// ── Когда parallelStream НЕ стоит использовать ───────────
// 1. Маленькие коллекции (накладные расходы > выигрыш)
// 2. Операции с side effects (запись в общую коллекцию)
// 3. Порядок важен и нельзя переключиться на forEachOrdered
```

---

## 📌 Слайд 8: Ленивость стримов

```java
// ── Стримы — ленивые! ────────────────────────────────────
// Промежуточные операции НЕ выполняются до терминальной

Stream<Integer> stream = Stream.of(1, 2, 3, 4, 5)
    .filter(n -> {
        System.out.println("filter: " + n);
        return n % 2 == 0;
    })
    .map(n -> {
        System.out.println("map: " + n);
        return n * 10;
    });

System.out.println("До терминальной операции — ничего не выведено!");
List<Integer> result = stream.collect(Collectors.toList()); // ← вот здесь всё запускается
System.out.println(result);

// Вывод:
// До терминальной операции — ничего не выведено!
// filter: 1
// filter: 2    ← прошёл фильтр
// map: 2       ← сразу же замапился
// filter: 3
// filter: 4    ← прошёл фильтр
// map: 4
// filter: 5
// [20, 40]

// ── findFirst + ленивость = эффективность ────────────────
Optional<Integer> first = Stream.iterate(1, n -> n + 1)
    .filter(n -> n % 17 == 0)
    .findFirst(); // ← остановится как только найдёт первое!
System.out.println(first); // Optional[17]
```

---

## 📌 Слайд 9: Итоги

✅ **Stream** — не хранит данные, обрабатывает их. Ленивый: работает только при терминальной операции.

✅ **Промежуточные операции**: `filter`, `map`, `flatMap`, `sorted`, `distinct`, `limit`, `skip`.

✅ **Терминальные операции**: `collect`, `count`, `reduce`, `findFirst`, `anyMatch`, `forEach`.

✅ **Collectors**: `toList`, `groupingBy`, `partitioningBy`, `joining`, `counting`, `summarizingDouble`.

✅ **IntStream/LongStream** — работают с примитивами без boxing, есть `range`, `sum`, `average`.

✅ **parallelStream** — автоматическая параллелизация. Осторожно с порядком и side effects.

---

## 📌 Слайд 10: Домашнее задание

```java
// Дан список транзакций:
class Transaction {
    String id;
    String type;     // "deposit" | "withdrawal"
    double amount;
    String currency;
    LocalDate date;
}

// Задания:
// 1. Найти все депозиты > 1000 в USD за последний месяц
// 2. Посчитать общую сумму снятий по каждой валюте (groupingBy + sum)
// 3. Найти топ-3 самые большие транзакции
// 4. Проверить: есть ли транзакции в EUR?
// 5. Собрать Map<String, List<Transaction>> — ключ: тип транзакции
// 6. Вывести строку: "USD: 5 транзакций, EUR: 3 транзакций, ..."
//    используя Collectors.joining
```

---

## ❓ Вопросы для самопроверки

1. Чем отличается промежуточная операция от терминальной?
2. Что значит "стримы ленивые"?
3. Чем `flatMap` отличается от `map`?
4. В каком случае `parallelStream` может замедлить программу?
5. Что делает `Collectors.groupingBy`?

---

*Лекция 11 из 19 | Курс: Введение в ООП на Java | Семестр 2*

