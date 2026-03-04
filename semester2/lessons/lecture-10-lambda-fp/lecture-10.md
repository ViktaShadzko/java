# Лекция 10: Лямбда-выражения и функциональное программирование. Comparable

---

## 🗂️ План лекции

1. Лямбда-выражения — синтаксис
2. Функциональные интерфейсы java.util.function
3. Ссылки на методы (Method References)
4. Comparable и Comparator
5. Замыкания (Closures) в Java
6. Функциональная композиция
7. Optional — замена null
8. Итоги и домашнее задание

---

## 📌 Слайд 1: Лямбда-выражения

> **Лямбда** — анонимная функция. Краткий способ реализовать функциональный интерфейс.

```java
// ── Синтаксис ─────────────────────────────────────────────
// (параметры) -> тело

// Без параметров
Runnable r = () -> System.out.println("Привет!");

// Один параметр — скобки необязательны
Consumer<String> printer = s -> System.out.println(s);

// Несколько параметров
Comparator<Integer> cmp = (a, b) -> a - b;

// Несколько строк — нужны фигурные скобки и return
Function<Integer, Integer> factorial = n -> {
    int result = 1;
    for (int i = 2; i <= n; i++) result *= i;
    return result;
};

System.out.println(factorial.apply(5)); // 120

// ── Лямбда vs анонимный класс ─────────────────────────────
// Старый способ:
Runnable old = new Runnable() {
    @Override public void run() { System.out.println("старый"); }
};

// Новый способ:
Runnable modern = () -> System.out.println("современный");
```

---

## 📌 Слайд 2: Функциональные интерфейсы java.util.function

```java
// ── Predicate<T> — T → boolean ───────────────────────────
Predicate<String> isLong    = s -> s.length() > 5;
Predicate<String> startsA   = s -> s.startsWith("А");
Predicate<String> isLongAndA = isLong.and(startsA);   // комбинирование
Predicate<String> either     = isLong.or(startsA);
Predicate<String> notLong    = isLong.negate();

System.out.println(isLongAndA.test("Анастасия")); // true
System.out.println(isLong.test("Ок"));            // false

// ── Function<T, R> — T → R ───────────────────────────────
Function<String, Integer> strToLen  = String::length;
Function<Integer, String> intToStr  = Object::toString;
Function<String, String>  composed  = strToLen.andThen(intToStr); // компоновка

System.out.println(composed.apply("Привет")); // "6"

// ── Consumer<T> — T → void ───────────────────────────────
Consumer<String> log   = s -> System.out.println("[LOG] " + s);
Consumer<String> save  = s -> System.out.println("[SAVE] " + s);
Consumer<String> both  = log.andThen(save);  // выполнить оба

both.accept("сообщение");
// [LOG] сообщение
// [SAVE] сообщение

// ── Supplier<T> — () → T ─────────────────────────────────
Supplier<List<String>> listFactory = ArrayList::new;
Supplier<LocalDate>    today       = LocalDate::now;

List<String> list = listFactory.get(); // новый список
System.out.println(today.get());       // 2026-03-04

// ── BiFunction<T, U, R> — (T, U) → R ─────────────────────
BiFunction<String, Integer, String> repeat = (s, n) -> s.repeat(n);
System.out.println(repeat.apply("ab", 3)); // ababab

// ── UnaryOperator<T> — T → T (специализация Function) ────
UnaryOperator<String> trim   = String::trim;
UnaryOperator<String> upper  = String::toUpperCase;
UnaryOperator<String> clean  = trim.andThen(upper);

System.out.println(clean.apply("  привет  ")); // ПРИВЕТ

// ── BinaryOperator<T> — (T, T) → T ──────────────────────
BinaryOperator<Integer> sum = Integer::sum;
BinaryOperator<Integer> max = Integer::max;
System.out.println(sum.apply(3, 4)); // 7
System.out.println(max.apply(3, 4)); // 4
```

---

## 📌 Слайд 3: Ссылки на методы (Method References)

```java
// ── Типы ссылок на методы ─────────────────────────────────

// 1. Статический метод: ClassName::staticMethod
Function<String, Integer> parse    = Integer::parseInt;
BiFunction<Integer,Integer,Integer> sum = Integer::sum;

// 2. Метод экземпляра (конкретного объекта): instance::method
String prefix = "Привет, ";
Function<String, String> greet = prefix::concat;
System.out.println(greet.apply("Иван")); // Привет, Иван

// 3. Метод экземпляра (произвольного объекта): ClassName::instanceMethod
Function<String, String>  toUpper  = String::toUpperCase;
Function<String, Integer>  length   = String::length;
Predicate<String>          isEmpty  = String::isEmpty;

// 4. Конструктор: ClassName::new
Supplier<ArrayList<String>>  listSupp  = ArrayList::new;
Function<String, StringBuilder> sbSupp = StringBuilder::new;

// ── Практический пример ───────────────────────────────────
List<String> names = Arrays.asList("  Иван  ", "  Мария  ", "  Пётр  ");

// С лямбдами:
List<String> cleaned1 = names.stream()
    .map(s -> s.trim())
    .map(s -> s.toUpperCase())
    .collect(Collectors.toList());

// С Method References — читается как описание:
List<String> cleaned2 = names.stream()
    .map(String::trim)
    .map(String::toUpperCase)
    .collect(Collectors.toList());

System.out.println(cleaned2); // [ИВАН, МАРИЯ, ПЁТР]

// Вывод с method reference
cleaned2.forEach(System.out::println);
```

---

## 📌 Слайд 4: Comparable и Comparator

```java
// ── Comparable — "естественный порядок" ───────────────────
public class Student implements Comparable<Student> {
    private String name;
    private double gpa;
    private int    age;

    // ... конструктор, геттеры ...

    @Override
    public int compareTo(Student other) {
        // Отрицательное: this < other
        // 0: this == other
        // Положительное: this > other
        return Double.compare(other.gpa, this.gpa); // по GPA убывание
    }
}

List<Student> students = Arrays.asList(
    new Student("Иван",  8.5, 20),
    new Student("Мария", 9.2, 21),
    new Student("Пётр",  7.8, 22)
);

Collections.sort(students); // использует compareTo
students.forEach(s -> System.out.println(s.getName() + ": " + s.getGpa()));
// Мария: 9.2 → Иван: 8.5 → Пётр: 7.8

// ── Comparator — "внешний порядок" ───────────────────────
// По имени
Comparator<Student> byName = Comparator.comparing(Student::getName);

// По GPA убывание, затем по имени
Comparator<Student> byGpaDesc = Comparator
    .comparingDouble(Student::getGpa)
    .reversed()
    .thenComparing(Student::getName);

// По возрасту, затем имени
Comparator<Student> complex = Comparator
    .comparingInt(Student::getAge)
    .thenComparing(Student::getName);

students.sort(byGpaDesc);
students.forEach(s -> System.out.println(s.getName() + ": " + s.getGpa()));
// Мария: 9.2 → Иван: 8.5 → Пётр: 7.8

// ── nullsFirst / nullsLast ─────────────────────────────────
Comparator<String> nullSafe = Comparator.nullsFirst(Comparator.naturalOrder());
List<String> withNulls = Arrays.asList("Б", null, "А", null, "В");
withNulls.sort(nullSafe);
System.out.println(withNulls); // [null, null, А, Б, В]
```

---

## 📌 Слайд 5: Замыкания в Java

```java
// ── Лямбда захватывает переменные из окружения ────────────
int baseScore = 10; // effectively final!

Function<Integer, Integer> addBase = n -> n + baseScore; // захватили baseScore

System.out.println(addBase.apply(5));  // 15
System.out.println(addBase.apply(20)); // 30

// baseScore = 20; // ❌ compile error — нельзя менять захваченную переменную!

// ── Обход через массив или AtomicInteger ──────────────────
int[] counter = {0};          // массив — effectively final (ссылка не меняется)
Runnable increment = () -> counter[0]++;

increment.run();
increment.run();
System.out.println(counter[0]); // 2

// ── Практический пример — создание функций ────────────────
public static Predicate<Integer> between(int min, int max) {
    return n -> n >= min && n <= max; // захватывают min и max
}

Predicate<Integer> teens  = between(13, 19);
Predicate<Integer> adults = between(18, 65);

System.out.println(teens.test(15));  // true
System.out.println(teens.test(25));  // false
System.out.println(adults.test(25)); // true
```

---

## 📌 Слайд 6: Optional — борьба с null

```java
// ── Создание Optional ─────────────────────────────────────
Optional<String> empty    = Optional.empty();
Optional<String> present  = Optional.of("Привет");
Optional<String> nullable = Optional.ofNullable(null);    // не бросает NPE

// ── Проверка и получение ──────────────────────────────────
System.out.println(present.isPresent());          // true
System.out.println(present.isEmpty());            // false (Java 11+)
System.out.println(present.get());                // Привет
System.out.println(empty.orElse("default"));      // default
System.out.println(empty.orElseGet(() -> "вычислено")); // вычислено

// orElseThrow
String value = empty.orElseThrow(
    () -> new IllegalStateException("Значение отсутствует!"));

// ── Трансформации ─────────────────────────────────────────
Optional<Integer> len = present.map(String::length);         // Optional[6]
Optional<String>  upper = present.map(String::toUpperCase);  // Optional[ПРИВЕТ]
Optional<String>  filtered = present.filter(s -> s.length() > 3); // Optional[Привет]

// flatMap — если функция тоже возвращает Optional
Optional<String> nested = present.flatMap(s ->
    s.isBlank() ? Optional.empty() : Optional.of(s.trim())
);

// ── Практический пример ───────────────────────────────────
public class UserService {

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email); // может вернуть пустой Optional
    }

    public String getUserCity(String email) {
        return findByEmail(email)
            .map(User::getAddress)
            .map(Address::getCity)
            .orElse("Город неизвестен");
    }
}

// ── Если использовать null — ужас NullPointerException ────
// user.getAddress().getCity() // ← NPE если address или city == null
```

---

## 📌 Слайд 7: Функциональная композиция на практике

```java
// ── Цепочка обработки данных ──────────────────────────────
public class DataPipeline {

    public static <T, R> Function<T, R> pipeline(
            Function<T, ?>... functions) {
        // ...
    }

    public static void main(String[] args) {
        List<String> rawData = Arrays.asList(
            "  Иван Иванов, 25, ivan@mail.ru  ",
            "  МАРИЯ ПЕТРОВА, 30, maria@gmail.com  ",
            "  пётр сидоров, 22, petr@ya.ru  ",
            null,
            "  Анна, -5, bad-email  "
        );

        List<User> users = rawData.stream()
            .filter(Objects::nonNull)           // убрать null
            .map(String::trim)                  // обрезать пробелы
            .filter(s -> !s.isBlank())          // убрать пустые
            .map(DataPipeline::parseLine)        // распарсить
            .filter(Optional::isPresent)        // убрать невалидные
            .map(Optional::get)                 // достать User
            .filter(u -> u.getAge() > 0)        // валидный возраст
            .collect(Collectors.toList());

        users.forEach(System.out::println);
    }

    private static Optional<User> parseLine(String line) {
        try {
            String[] parts = line.split(",");
            return Optional.of(new User(
                parts[0].trim(),
                Integer.parseInt(parts[1].trim()),
                parts[2].trim()
            ));
        } catch (Exception e) {
            return Optional.empty(); // невалидная строка
        }
    }
}
```

---

## 📌 Слайд 8: Итоги

✅ **Лямбды** — краткая запись функциональных интерфейсов. Основа Java 8+.

✅ **Method references** — ещё короче лямбд. Читаются как описание действия.

✅ **java.util.function** — готовые функциональные интерфейсы: `Predicate`, `Function`, `Consumer`, `Supplier`.

✅ **Comparable** — естественный порядок объекта. **Comparator** — внешний порядок.

✅ **Optional** — явная обёртка для "значение может отсутствовать". Борьба с NPE.

✅ Лямбды захватывают только **effectively final** переменные.

---

## 📌 Слайд 9: Домашнее задание

```java
// 1. Цепочка трансформаций:
// Дан список строк с числами (некоторые невалидные):
// ["1", "abc", "2", "", "3", "10", "xyz"]
// - отфильтровать невалидные (try-catch внутри map)
// - умножить на 2
// - оставить только чётные
// - вычислить сумму

// 2. Comparator-цепочка:
// Отсортировать List<Product> по:
// 1) категории (алфавит)
// 2) цене убывание
// 3) названию алфавит
// nullsLast для цены

// 3. Optional-цепочка:
// userService.findById(id)
//   .map(User::getProfile)
//   .map(Profile::getAvatar)
//   .filter(url -> url.startsWith("https"))
//   .orElse("https://default-avatar.png")
```

---

## ❓ Вопросы для самопроверки

1. Что такое "effectively final" в контексте лямбд?
2. В чём разница между `Comparable` и `Comparator`?
3. Какие есть типы ссылок на методы?
4. Зачем нужен `Optional`? Почему нельзя просто проверять `null`?
5. Что делает `Function.compose()` vs `Function.andThen()`?

---

*Лекция 10 из 19 | Курс: Введение в ООП на Java | Семестр 2*

