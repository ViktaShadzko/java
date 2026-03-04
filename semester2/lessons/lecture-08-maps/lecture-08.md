# Лекция 8: Мапы (Map)

---

## 🗂️ План лекции

1. Map — ключ-значение
2. HashMap — основные операции
3. TreeMap — сортированная Map
4. LinkedHashMap — порядок вставки
5. Итерация по Map
6. Методы Java 8+ в Map
7. Вложенные структуры данных
8. Выбор реализации Map
9. Итоги и домашнее задание

---

## 📌 Слайд 1: Map — что это?

> **Map** — коллекция пар **ключ → значение**. Каждый ключ уникален.

```
Map<K, V>
├── HashMap           — нет порядка, O(1) операции
├── LinkedHashMap     — порядок вставки
├── TreeMap           — отсортирован по ключу
└── Hashtable         — устаревший, потокобезопасный (не используй)
```

### Аналогия:
> Map — это как **словарь**: знаешь слово (ключ) → находишь определение (значение).

---

## 📌 Слайд 2: HashMap — основные операции

```java
import java.util.*;

Map<String, Integer> scores = new HashMap<>();

// ── Добавление ────────────────────────────────────────────
scores.put("Иван",   90);
scores.put("Мария",  85);
scores.put("Пётр",   92);
scores.put("Анна",   88);
scores.put("Иван",   95); // ← перезапишет, ключ уже есть

// ── Получение ─────────────────────────────────────────────
System.out.println(scores.get("Иван"));          // 95
System.out.println(scores.get("Неизвестный"));   // null
System.out.println(scores.getOrDefault("Неизвестный", 0)); // 0 — безопасно

// ── Проверка ──────────────────────────────────────────────
System.out.println(scores.containsKey("Мария"));  // true
System.out.println(scores.containsValue(85));      // true
System.out.println(scores.size());                 // 4
System.out.println(scores.isEmpty());              // false

// ── Удаление ──────────────────────────────────────────────
scores.remove("Пётр");                    // удалить по ключу
scores.remove("Анна", 88);               // удалить если значение совпадает

// ── Замена ────────────────────────────────────────────────
scores.replace("Мария", 90);             // заменить значение
scores.replace("Мария", 85, 90);         // заменить только если старое значение совпадает

// ── Все ключи, значения, пары ─────────────────────────────
Set<String>             keys    = scores.keySet();
Collection<Integer>     values  = scores.values();
Set<Map.Entry<String, Integer>> entries = scores.entrySet();
```

---

## 📌 Слайд 3: Итерация по Map

```java
Map<String, Integer> scores = new HashMap<>();
scores.put("Иван", 90);
scores.put("Мария", 85);
scores.put("Пётр", 92);

// ── Способ 1: entrySet — самый эффективный ────────────────
for (Map.Entry<String, Integer> entry : scores.entrySet()) {
    System.out.println(entry.getKey() + " → " + entry.getValue());
}

// ── Способ 2: forEach (Java 8+) — самый читаемый ──────────
scores.forEach((name, score) ->
    System.out.println(name + " → " + score));

// ── Способ 3: keySet — если нужны только ключи ────────────
for (String name : scores.keySet()) {
    System.out.println(name);
}

// ── Способ 4: values — если нужны только значения ─────────
for (int score : scores.values()) {
    System.out.println(score);
}

// ── Итерация + Stream ─────────────────────────────────────
// Найти студентов с оценкой >= 90
scores.entrySet().stream()
    .filter(e -> e.getValue() >= 90)
    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
    .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));
```

---

## 📌 Слайд 4: TreeMap — сортированная Map

```java
// ── TreeMap сортирует по ключу ────────────────────────────
Map<String, Integer> tree = new TreeMap<>();
tree.put("Вася",  70);
tree.put("Аня",   95);
tree.put("Дима",  80);
tree.put("Борис", 85);

tree.forEach((k, v) -> System.out.println(k + ": " + v));
// Аня: 95 → Борис: 85 → Вася: 70 → Дима: 80  (алфавитный порядок!)

// ── TreeMap — работа с диапазонами ───────────────────────
TreeMap<Integer, String> byScore = new TreeMap<>();
byScore.put(90, "Иван");
byScore.put(85, "Мария");
byScore.put(92, "Пётр");
byScore.put(78, "Анна");

System.out.println(byScore.firstKey()); // 78 — минимальный ключ
System.out.println(byScore.lastKey());  // 92 — максимальный ключ

// Все с оценкой от 85 до 92 включительно
SortedMap<Integer, String> top = byScore.subMap(85, true, 92, true);
top.forEach((score, name) -> System.out.println(name + ": " + score));
// Мария: 85 → Иван: 90 → Пётр: 92

System.out.println(byScore.floorKey(89));   // 85 — ближайший ≤ 89
System.out.println(byScore.ceilingKey(89)); // 90 — ближайший ≥ 89

// ── Обратный порядок ─────────────────────────────────────
TreeMap<Integer, String> desc = new TreeMap<>(Comparator.reverseOrder());
desc.put(90, "Иван");
desc.put(85, "Мария");
desc.put(92, "Пётр");
desc.forEach((score, name) -> System.out.println(name + ": " + score));
// Пётр: 92 → Иван: 90 → Мария: 85
```

---

## 📌 Слайд 5: LinkedHashMap

```java
// ── LinkedHashMap — сохраняет порядок вставки ─────────────
Map<String, Integer> linked = new LinkedHashMap<>();
linked.put("первый",  1);
linked.put("второй",  2);
linked.put("третий",  3);

linked.forEach((k, v) -> System.out.println(k + ": " + v));
// первый: 1 → второй: 2 → третий: 3  (порядок вставки!)

// ── LRU Cache через LinkedHashMap ────────────────────────
// accessOrder=true: при get() элемент перемещается в конец
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    private final int maxSize;

    public LRUCache(int maxSize) {
        super(maxSize, 0.75f, true); // accessOrder = true!
        this.maxSize = maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize; // удаляем самый старый если превысили лимит
    }
}

LRUCache<Integer, String> cache = new LRUCache<>(3);
cache.put(1, "один");
cache.put(2, "два");
cache.put(3, "три");
cache.get(1);        // обратились к 1 — он стал "новейшим"
cache.put(4, "четыре"); // 2 удалится — он самый старый из доступных
System.out.println(cache.keySet()); // [3, 1, 4]
```

---

## 📌 Слайд 6: Методы Java 8+ в Map

```java
Map<String, Integer> scores = new HashMap<>();
scores.put("Иван", 90);

// ── putIfAbsent — добавить если ключа нет ─────────────────
scores.putIfAbsent("Мария", 85);  // добавит
scores.putIfAbsent("Иван",  50);  // НЕ добавит — Иван уже есть
System.out.println(scores.get("Иван")); // 90

// ── computeIfAbsent — вычислить и добавить если нет ────────
Map<String, List<String>> groups = new HashMap<>();
// Старый способ:
if (!groups.containsKey("IT")) {
    groups.put("IT", new ArrayList<>());
}
groups.get("IT").add("Иван");

// Новый способ (Java 8+):
groups.computeIfAbsent("IT", k -> new ArrayList<>()).add("Иван");
groups.computeIfAbsent("IT", k -> new ArrayList<>()).add("Мария"); // список уже есть

// ── compute — вычислить новое значение ───────────────────
Map<String, Integer> wordCount = new HashMap<>();
String[] words = {"яблоко", "банан", "яблоко", "груша", "банан", "яблоко"};

for (String word : words) {
    wordCount.compute(word, (k, v) -> v == null ? 1 : v + 1);
}
System.out.println(wordCount); // {яблоко=3, банан=2, груша=1}

// ── merge — объединить значения ───────────────────────────
for (String word : words) {
    wordCount.merge(word, 1, Integer::sum); // ещё короче!
}

// ── getOrDefault ─────────────────────────────────────────
int score = scores.getOrDefault("Неизвестный", 0);

// ── replaceAll — заменить все значения ───────────────────
scores.replaceAll((name, s) -> s + 10); // всем +10 баллов
```

---

## 📌 Слайд 7: Вложенные структуры данных

```java
// ── Map<String, List<String>> — группировка ───────────────
Map<String, List<String>> departmentEmployees = new HashMap<>();

departmentEmployees.computeIfAbsent("IT",      k -> new ArrayList<>()).add("Иван");
departmentEmployees.computeIfAbsent("IT",      k -> new ArrayList<>()).add("Мария");
departmentEmployees.computeIfAbsent("Продажи", k -> new ArrayList<>()).add("Пётр");
departmentEmployees.computeIfAbsent("Продажи", k -> new ArrayList<>()).add("Анна");

departmentEmployees.forEach((dept, employees) -> {
    System.out.println(dept + ": " + employees);
});
// IT: [Иван, Мария]
// Продажи: [Пётр, Анна]

// ── Map<String, Map<String, Integer>> — вложенная ─────────
Map<String, Map<String, Integer>> grades = new HashMap<>();
grades.computeIfAbsent("Иван",  k -> new HashMap<>()).put("Математика", 90);
grades.computeIfAbsent("Иван",  k -> new HashMap<>()).put("Java",       95);
grades.computeIfAbsent("Мария", k -> new HashMap<>()).put("Математика", 88);

System.out.println(grades.get("Иван").get("Java")); // 95

// ── Группировка через Stream (удобнее!) ──────────────────
List<Student> students = Arrays.asList(
    new Student("Иван",  "IT"),
    new Student("Мария", "IT"),
    new Student("Пётр",  "Математика")
);

Map<String, List<Student>> byFaculty = students.stream()
    .collect(Collectors.groupingBy(Student::getFaculty));
```

---

## 📌 Слайд 8: Выбор реализации Map

| Реализация | Порядок | Производительность | Когда использовать |
|---|---|---|---|
| `HashMap` | Нет | O(1) средний | Большинство случаев |
| `LinkedHashMap` | Вставки | O(1) | Нужен порядок вставки, LRU Cache |
| `TreeMap` | Отсортирован | O(log n) | Нужна сортировка по ключу, диапазоны |
| `EnumMap` | Порядок Enum | O(1) очень быстро | Ключи — Enum |
| `ConcurrentHashMap` | Нет | O(1) потокобезопасный | Многопоточность |

---

## 📌 Слайд 9: Итоги

✅ **Map** — структура ключ → значение. Ключи уникальны.

✅ **HashMap** — выбор по умолчанию. O(1). Не гарантирует порядок.

✅ **TreeMap** — всегда отсортирован по ключу. O(log n). Поддерживает диапазоны.

✅ **LinkedHashMap** — сохраняет порядок вставки. Основа LRU Cache.

✅ **Java 8+**: `computeIfAbsent`, `merge`, `getOrDefault`, `forEach`, `replaceAll`.

✅ Для ключей должны быть корректно определены `equals` и `hashCode`.

---

## 📌 Слайд 10: Домашнее задание

```java
// 1. Подсчёт частоты слов:
// Дан текст, найти топ-5 самых часто встречающихся слов.
// Использовать Map<String, Integer> + Stream для сортировки.

// 2. Телефонная книга:
// Map<String, List<String>> — имя → список телефонов
// Методы: addPhone(name, phone), getPhones(name), removePhone(name, phone)

// 3. Инвентарь:
// Map<String, Map<String, Integer>>
// category → (itemName → quantity)
// Методы: addItem, removeItem, getTotal(category), findLowStock(minQuantity)

// Бонус: реализовать BiMap — двунаправленная Map
// (можно найти ключ по значению и значение по ключу, оба уникальны)
```

---

## ❓ Вопросы для самопроверки

1. Что будет если добавить в `HashMap` два ключа с одинаковым `hashCode` но разным `equals`?
2. В чём отличие `put` от `putIfAbsent`?
3. Когда использовать `TreeMap` вместо `HashMap`?
4. Что делает метод `merge`?
5. Почему ключи в `HashMap` должны быть иммутабельными?

---

## 📚 Дополнительные материалы

| Ресурс | Описание |
|---|---|
| [Oracle — Map Interface](https://docs.oracle.com/javase/tutorial/collections/interfaces/map.html) | Официальная документация |
| [Baeldung — HashMap](https://www.baeldung.com/java-hashmap) | Внутреннее устройство HashMap |

---

*Лекция 8 из 19 | Курс: Введение в ООП на Java | Семестр 2*

