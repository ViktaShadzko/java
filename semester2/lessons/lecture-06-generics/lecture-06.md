# Лекция 6: Параметризованные типы и Дженерики

---

## 🗂️ План лекции

1. Зачем нужны дженерики?
2. Параметризованные классы
3. Параметризованные методы
4. Ограничения (bounds) — `extends` и `super`
5. Wildcards — `?`
6. Стирание типов (Type Erasure)
7. Дженерики и коллекции
8. Ограничения дженериков
9. Итоги и домашнее задание

---

## 📌 Слайд 1: Зачем нужны дженерики?

### Проблема без дженериков:
```java
// ❌ До Java 5 — List хранит Object, нужны касты
List list = new ArrayList();
list.add("Привет");
list.add(42);           // можно добавить что угодно!
list.add(new Object());

// Достаём — нужен явный каст, может упасть в рантайме
String s = (String) list.get(0); // ок
String n = (String) list.get(1); // ❌ ClassCastException в рантайме!
```

### Решение — дженерики:
```java
// ✅ С дженериками — компилятор проверяет типы
List<String> list = new ArrayList<>();
list.add("Привет");
list.add("Мир");
// list.add(42);    // ❌ compile error — Integer ≠ String

String s = list.get(0); // не нужен каст, компилятор знает тип
```

> **Дженерики** — это параметризация типов. Пишешь код один раз, работает с любым типом.

---

## 📌 Слайд 2: Параметризованный класс

```java
// ── Без дженерика — только для конкретного типа ──────────
public class IntBox {
    private int value;
    public IntBox(int value) { this.value = value; }
    public int getValue() { return value; }
}

// ── С дженериком — работает с любым типом ────────────────
public class Box<T> {            // T — параметр типа (Type Parameter)
    private T value;

    public Box(T value) { this.value = value; }

    public T getValue() { return value; }

    public void setValue(T value) { this.value = value; }

    public boolean isEmpty() { return value == null; }

    @Override
    public String toString() { return "Box[" + value + "]"; }
}

// ── Использование ─────────────────────────────────────────
Box<String>  strBox = new Box<>("Привет");
Box<Integer> intBox = new Box<>(42);
Box<Double>  dblBox = new Box<>(3.14);

System.out.println(strBox.getValue()); // Привет
System.out.println(intBox.getValue()); // 42
System.out.println(dblBox.getValue()); // 3.14

// Нет кастов, нет ошибок в рантайме!
String s = strBox.getValue(); // ← компилятор знает что это String
```

### Несколько параметров типа:
```java
public class Pair<K, V> {
    private final K key;
    private final V value;

    public Pair(K key, V value) {
        this.key   = key;
        this.value = value;
    }

    public K getKey()   { return key;   }
    public V getValue() { return value; }

    @Override
    public String toString() { return "(" + key + ", " + value + ")"; }
}

Pair<String, Integer>  age   = new Pair<>("Иван", 25);
Pair<String, String>   city  = new Pair<>("Столица", "Москва");
Pair<Integer, Double>  price = new Pair<>(1, 99.99);

System.out.println(age);   // (Иван, 25)
System.out.println(city);  // (Столица, Москва)
```

---

## 📌 Слайд 3: Параметризованные методы

```java
public class ArrayUtils {

    // Параметр типа <T> объявляется перед возвращаемым типом
    public static <T> void swap(T[] arr, int i, int j) {
        T temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public static <T> T findMax(T[] arr, Comparator<T> comparator) {
        if (arr == null || arr.length == 0) return null;
        T max = arr[0];
        for (T item : arr) {
            if (comparator.compare(item, max) > 0) {
                max = item;
            }
        }
        return max;
    }

    public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
        List<T> result = new ArrayList<>();
        for (T item : list) {
            if (predicate.test(item)) {
                result.add(item);
            }
        }
        return result;
    }
}

// ── Использование — тип выводится автоматически ───────────
String[] names = {"Аня", "Боб", "Вася"};
ArrayUtils.swap(names, 0, 2);
System.out.println(Arrays.toString(names)); // [Вася, Боб, Аня]

Integer[] nums = {3, 1, 4, 1, 5, 9};
Integer max = ArrayUtils.findMax(nums, Integer::compareTo);
System.out.println(max); // 9

List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6);
List<Integer> evens = ArrayUtils.filter(numbers, n -> n % 2 == 0);
System.out.println(evens); // [2, 4, 6]
```

---

## 📌 Слайд 4: Ограничения — Upper Bounded (`extends`)

```java
// <T extends Number> — T должен быть Number или его наследником
public class Statistics<T extends Number> {
    private List<T> data;

    public Statistics(List<T> data) { this.data = data; }

    public double sum() {
        return data.stream()
            .mapToDouble(Number::doubleValue) // можно вызывать методы Number!
            .sum();
    }

    public double average() {
        return data.isEmpty() ? 0 : sum() / data.size();
    }

    public T max() {
        return data.stream()
            .max(Comparator.comparingDouble(Number::doubleValue))
            .orElseThrow();
    }
}

// ── Использование ─────────────────────────────────────────
Statistics<Integer> intStats = new Statistics<>(Arrays.asList(1, 2, 3, 4, 5));
System.out.println(intStats.sum());     // 15.0
System.out.println(intStats.average()); // 3.0

Statistics<Double> dblStats = new Statistics<>(Arrays.asList(1.5, 2.5, 3.0));
System.out.println(dblStats.max());     // 3.0

// Statistics<String> strStats = ...; // ❌ String не extends Number

// ── Несколько ограничений ─────────────────────────────────
public <T extends Comparable<T> & Cloneable> T findMin(List<T> list) {
    return list.stream().min(Comparator.naturalOrder()).orElseThrow();
}
```

---

## 📌 Слайд 5: Wildcards — `?`

### `?` — неизвестный тип:
```java
// ── Unbounded Wildcard <?> ────────────────────────────────
public static void printList(List<?> list) {
    for (Object item : list) {
        System.out.println(item); // можем только читать как Object
    }
}

printList(Arrays.asList(1, 2, 3));        // ✅
printList(Arrays.asList("a", "b", "c"));  // ✅
printList(Arrays.asList(1.0, 2.0));       // ✅

// ── Upper Bounded <? extends T> — Producer ────────────────
// Можно ЧИТАТЬ (List производит элементы типа T)
public static double sumNumbers(List<? extends Number> list) {
    return list.stream().mapToDouble(Number::doubleValue).sum();
}

sumNumbers(Arrays.asList(1, 2, 3));       // List<Integer> ✅
sumNumbers(Arrays.asList(1.5, 2.5));      // List<Double>  ✅

// ── Lower Bounded <? super T> — Consumer ─────────────────
// Можно ПИСАТЬ (List потребляет элементы типа T)
public static void addNumbers(List<? super Integer> list) {
    list.add(1);
    list.add(2);
    list.add(3); // можно добавлять Integer и его предков
}

List<Number> numbers = new ArrayList<>();
addNumbers(numbers);    // List<Number> ✅
List<Object> objects = new ArrayList<>();
addNumbers(objects);    // List<Object> ✅
```

### PECS — Producer Extends, Consumer Super:
```java
// PECS: если из коллекции ДОСТАЁШЬ — extends, если КЛАДЁШЬ — super
public static <T> void copy(List<? extends T> src,  // Producer — extends
                             List<? super T>   dest) { // Consumer — super
    for (T item : src) {
        dest.add(item);
    }
}
```

---

## 📌 Слайд 6: Стирание типов (Type Erasure)

> Дженерики в Java — это **compile-time** фича. В байткоде типовых параметров **нет**.

```java
// Написано:
List<String> strings = new ArrayList<>();
List<Integer> integers = new ArrayList<>();

// После компиляции (байткод):
List strings = new ArrayList();    // T → Object
List integers = new ArrayList();   // T → Object

System.out.println(strings.getClass() == integers.getClass()); // TRUE!
```

### Следствия стирания типов:
```java
// ❌ Нельзя создать массив параметризованного типа
T[] arr = new T[10];           // compile error!

// ❌ Нельзя использовать instanceof с параметром типа
if (obj instanceof T) { }      // compile error!

// ❌ Нельзя создать объект параметризованного типа
T obj = new T();               // compile error!

// ✅ Обходные пути
public class TypedContainer<T> {
    private final Class<T> type;

    public TypedContainer(Class<T> type) { this.type = type; }

    public T create() throws Exception {
        return type.getDeclaredConstructor().newInstance(); // через рефлексию
    }

    public boolean isInstanceOf(Object obj) {
        return type.isInstance(obj);
    }
}
```

---

## 📌 Слайд 7: Практический пример — Repository

```java
// ── Интерфейс — дженерик репозиторий ─────────────────────
public interface Repository<T, ID> {
    void save(T entity);
    Optional<T> findById(ID id);
    List<T> findAll();
    void delete(ID id);
    int count();
}

// ── Базовая реализация ────────────────────────────────────
public abstract class InMemoryRepository<T, ID> implements Repository<T, ID> {
    protected final Map<ID, T> storage = new HashMap<>();

    @Override
    public void save(T entity) {
        storage.put(getId(entity), entity);
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<T> findAll() { return new ArrayList<>(storage.values()); }

    @Override
    public void delete(ID id) { storage.remove(id); }

    @Override
    public int count() { return storage.size(); }

    protected abstract ID getId(T entity);
}

// ── Конкретная реализация ─────────────────────────────────
public class UserRepository extends InMemoryRepository<User, Long> {
    @Override
    protected Long getId(User user) { return user.getId(); }

    // Специфичный метод только для User
    public List<User> findByName(String name) {
        return storage.values().stream()
            .filter(u -> u.getName().equals(name))
            .collect(Collectors.toList());
    }
}

// ── Использование ─────────────────────────────────────────
UserRepository repo = new UserRepository();
repo.save(new User(1L, "Иван"));
repo.save(new User(2L, "Мария"));

Optional<User> user = repo.findById(1L);
user.ifPresent(u -> System.out.println(u.getName())); // Иван
System.out.println(repo.count()); // 2
```

---

## 📌 Слайд 8: Итоги

### Что нужно запомнить:

✅ **Дженерики** = типобезопасность без кастов + переиспользование кода.

✅ `<T extends Foo>` — T должен быть Foo или его наследником.

✅ **PECS**: читаешь из коллекции → `extends`, пишешь в коллекцию → `super`.

✅ **Type Erasure** — параметры типа существуют только при компиляции.

✅ Нельзя: `new T()`, `new T[]`, `instanceof T`.

✅ Дженерики широко используются в Collections Framework, Stream API, Optional.

---

## 📌 Слайд 9: Домашнее задание

```java
// 1. Реализовать параметризованный стек:
public class Stack<T> {
    // push(T item)
    // T pop()           — бросает EmptyStackException если пуст
    // T peek()          — смотрит вершину без удаления
    // boolean isEmpty()
    // int size()
}

// 2. Реализовать параметризованную пару и тройку:
Pair<String, Integer> nameAge = new Pair<>("Иван", 25);
Triple<String, Integer, Double> student =
    new Triple<>("Иван", 25, 8.5); // имя, возраст, GPA

// 3. Написать метод:
// <T extends Comparable<T>> T clamp(T value, T min, T max)
// — возвращает value если min <= value <= max, иначе min или max

// Бонус: реализовать Result<T> — аналог Optional с сообщением об ошибке
// Result.success(value)
// Result.failure(message)
// boolean isSuccess()
// T getValue()
// String getError()
```

---

## ❓ Вопросы для самопроверки

1. Что такое **стирание типов** (type erasure)?
2. Почему нельзя создать `new T[]`?
3. В чём разница между `List<?>` и `List<Object>`?
4. Что означает PECS?
5. Можно ли параметр типа ограничить двумя интерфейсами одновременно?

---

## 📚 Дополнительные материалы

| Ресурс | Описание |
|---|---|
| [Oracle — Generics](https://docs.oracle.com/javase/tutorial/java/generics/) | Официальная документация |
| «Effective Java» Item 26-33 | Глава о дженериках |
| [Baeldung — Java Generics](https://www.baeldung.com/java-generics) | Практические примеры |

---

*Лекция 6 из 19 | Курс: Введение в ООП на Java | Семестр 2*

