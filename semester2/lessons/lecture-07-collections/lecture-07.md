# Лекция 7: Коллекции

---

## 🗂️ План лекции

1. Collections Framework — обзор
2. List — упорядоченная последовательность
3. ArrayList vs LinkedList
4. Set — уникальные элементы
5. HashSet vs TreeSet vs LinkedHashSet
6. Queue и Deque
7. Collections — утилитный класс
8. Выбор коллекции — шпаргалка
9. Итоги и домашнее задание

---

## 📌 Слайд 1: Collections Framework

```
Iterable
└── Collection
    ├── List          — упорядоченная, с дубликатами
    │   ├── ArrayList
    │   └── LinkedList
    ├── Set           — уникальные элементы
    │   ├── HashSet
    │   ├── LinkedHashSet
    │   └── TreeSet
    └── Queue         — очередь
        ├── LinkedList
        ├── PriorityQueue
        └── Deque
            ├── ArrayDeque
            └── LinkedList
```

---

## 📌 Слайд 2: List — основные операции

```java
import java.util.*;

// ── Создание ──────────────────────────────────────────────
List<String> list = new ArrayList<>();         // изменяемый
List<String> fixed = List.of("a", "b", "c");   // неизменяемый (Java 9+)
List<String> copy  = new ArrayList<>(fixed);   // изменяемая копия

// ── CRUD операции ─────────────────────────────────────────
list.add("Иван");          // добавить в конец
list.add(0, "Анна");       // добавить по индексу
list.addAll(fixed);        // добавить все из другой коллекции

list.get(0);               // получить по индексу
list.set(0, "Новое имя");  // заменить по индексу

list.remove(0);            // удалить по индексу
list.remove("Иван");       // удалить по значению

// ── Поиск ─────────────────────────────────────────────────
list.contains("Анна");     // true / false
list.indexOf("Анна");      // первый индекс или -1
list.lastIndexOf("Анна");  // последний индекс или -1
list.size();               // размер
list.isEmpty();            // пустой ли

// ── Итерация ──────────────────────────────────────────────
for (String item : list) {
    System.out.println(item);
}

list.forEach(System.out::println);  // Java 8+

// ── Подсписок ─────────────────────────────────────────────
List<String> sub = list.subList(1, 3); // [1, 3) — индексы 1 и 2

// ── Сортировка ────────────────────────────────────────────
Collections.sort(list);                                    // по умолчанию
list.sort(Comparator.comparingInt(String::length));        // по длине
list.sort(Comparator.reverseOrder());                      // обратный порядок
```

---

## 📌 Слайд 3: ArrayList vs LinkedList

```java
// ── ArrayList — массив под капотом ───────────────────────
ArrayList<Integer> arrayList = new ArrayList<>(10); // начальная ёмкость

// ── LinkedList — двусвязный список ───────────────────────
LinkedList<Integer> linkedList = new LinkedList<>();
linkedList.addFirst(1);   // добавить в начало — O(1)
linkedList.addLast(2);    // добавить в конец — O(1)
linkedList.removeFirst(); // удалить первый — O(1)
linkedList.removeLast();  // удалить последний — O(1)
```

| Операция | ArrayList | LinkedList |
|---|---|---|
| `get(i)` | **O(1)** ← быстро | O(n) |
| `add` в конец | O(1) амортизированно | **O(1)** |
| `add` в начало/середину | O(n) | **O(1)** |
| `remove` по индексу | O(n) | O(n) |
| Память | Меньше | Больше (хранит ссылки) |
| **Рекомендуется** | Большинство случаев | Очереди и деки |

> 💡 **На практике:** `ArrayList` подходит в 95% случаев. `LinkedList` — только если много вставок/удалений в начало.

---

## 📌 Слайд 4: Set — уникальные элементы

```java
// ── HashSet — нет порядка, O(1) операции ─────────────────
Set<String> hashSet = new HashSet<>();
hashSet.add("банан");
hashSet.add("яблоко");
hashSet.add("банан");    // дубликат — не добавится!
System.out.println(hashSet.size()); // 2

// ── LinkedHashSet — сохраняет порядок вставки ─────────────
Set<String> linked = new LinkedHashSet<>();
linked.add("банан");
linked.add("яблоко");
linked.add("груша");
System.out.println(linked); // [банан, яблоко, груша] — порядок сохранён

// ── TreeSet — отсортированный порядок ────────────────────
Set<String> tree = new TreeSet<>();
tree.add("банан");
tree.add("яблоко");
tree.add("груша");
System.out.println(tree); // [банан, груша, яблоко] — алфавитный порядок

// ── Операции над множествами ──────────────────────────────
Set<Integer> a = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5));
Set<Integer> b = new HashSet<>(Arrays.asList(3, 4, 5, 6, 7));

// Пересечение
Set<Integer> intersection = new HashSet<>(a);
intersection.retainAll(b);
System.out.println(intersection); // [3, 4, 5]

// Объединение
Set<Integer> union = new HashSet<>(a);
union.addAll(b);
System.out.println(union); // [1, 2, 3, 4, 5, 6, 7]

// Разность
Set<Integer> diff = new HashSet<>(a);
diff.removeAll(b);
System.out.println(diff); // [1, 2]
```

---

## 📌 Слайд 5: equals и hashCode — основа Set и Map

> `HashSet` использует `hashCode()` для нахождения "корзины" и `equals()` для сравнения объектов.

```java
// ❌ Без переопределения — два "одинаковых" объекта считаются разными
public class BadPoint {
    int x, y;
    BadPoint(int x, int y) { this.x = x; this.y = y; }
}

Set<BadPoint> set = new HashSet<>();
set.add(new BadPoint(1, 2));
set.add(new BadPoint(1, 2)); // дубликат, но не обнаружен!
System.out.println(set.size()); // 2 — неправильно!

// ✅ С переопределением — работает корректно
public class Point {
    private final int x, y;

    Point(int x, int y) { this.x = x; this.y = y; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point p = (Point) o;
        return x == p.x && y == p.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y); // всегда используй Objects.hash!
    }
}

Set<Point> points = new HashSet<>();
points.add(new Point(1, 2));
points.add(new Point(1, 2)); // дубликат обнаружен!
System.out.println(points.size()); // 1 — правильно!
```

> ⚠️ **Правило:** если переопределяешь `equals` — **обязательно** переопределяй `hashCode`!

---

## 📌 Слайд 6: Queue и Deque

```java
// ── Queue (FIFO — First In, First Out) ───────────────────
Queue<String> queue = new LinkedList<>();
queue.offer("первый");   // добавить в хвост
queue.offer("второй");
queue.offer("третий");

System.out.println(queue.peek());  // посмотреть первый — "первый"
System.out.println(queue.poll());  // извлечь первый — "первый"
System.out.println(queue.size());  // 2

// ── PriorityQueue — приоритетная очередь ─────────────────
PriorityQueue<Integer> pq = new PriorityQueue<>(); // min-heap по умолчанию
pq.offer(5);
pq.offer(1);
pq.offer(3);

while (!pq.isEmpty()) {
    System.out.print(pq.poll() + " "); // 1 3 5 — всегда минимальный первый
}

// Max-heap
PriorityQueue<Integer> maxPq = new PriorityQueue<>(Comparator.reverseOrder());
maxPq.offer(5);
maxPq.offer(1);
maxPq.offer(3);
System.out.println(maxPq.poll()); // 5

// ── Deque (Double-Ended Queue) ────────────────────────────
Deque<String> deque = new ArrayDeque<>();
deque.addFirst("B");
deque.addFirst("A");  // [A, B]
deque.addLast("C");   // [A, B, C]

System.out.println(deque.peekFirst()); // A
System.out.println(deque.peekLast());  // C
System.out.println(deque.pollFirst()); // A → [B, C]

// Deque как стек (LIFO)
Deque<String> stack = new ArrayDeque<>();
stack.push("первый");   // addFirst
stack.push("второй");
System.out.println(stack.pop()); // "второй" — LIFO
```

---

## 📌 Слайд 7: Утилитный класс Collections

```java
List<Integer> nums = new ArrayList<>(Arrays.asList(3, 1, 4, 1, 5, 9, 2, 6));

// ── Сортировка ────────────────────────────────────────────
Collections.sort(nums);                    // [1, 1, 2, 3, 4, 5, 6, 9]
Collections.sort(nums, Comparator.reverseOrder()); // [9, 6, 5, 4, 3, 2, 1, 1]

// ── Поиск (только в отсортированном списке) ───────────────
Collections.sort(nums);
int idx = Collections.binarySearch(nums, 5); // индекс 5

// ── Перемешать, перевернуть ───────────────────────────────
Collections.shuffle(nums);          // случайный порядок
Collections.reverse(nums);          // обратный порядок
Collections.rotate(nums, 2);        // сдвиг на 2 позиции

// ── Мин / Макс ────────────────────────────────────────────
System.out.println(Collections.min(nums));
System.out.println(Collections.max(nums));

// ── Частота / присутствие ─────────────────────────────────
System.out.println(Collections.frequency(nums, 1)); // сколько раз встречается 1

// ── Неизменяемые обёртки ──────────────────────────────────
List<String> mutable   = new ArrayList<>(Arrays.asList("a", "b"));
List<String> immutable = Collections.unmodifiableList(mutable);
// immutable.add("c"); // ❌ UnsupportedOperationException

// ── Пустые и singleton коллекции ─────────────────────────
List<String>  emptyList = Collections.emptyList();
Set<String>   emptySet  = Collections.emptySet();
List<Integer> single    = Collections.singletonList(42);
```

---

## 📌 Слайд 8: Шпаргалка — выбор коллекции

| Задача | Коллекция | Почему |
|---|---|---|
| Список с быстрым доступом по индексу | `ArrayList` | O(1) get |
| Часто вставляю/удаляю в начало | `LinkedList` | O(1) add/remove first |
| Уникальные элементы, порядок не важен | `HashSet` | O(1) add/contains |
| Уникальные элементы, порядок вставки | `LinkedHashSet` | Сохраняет порядок |
| Уникальные элементы, отсортированные | `TreeSet` | Всегда отсортирован |
| Очередь FIFO | `LinkedList` / `ArrayDeque` | O(1) offer/poll |
| Очередь по приоритету | `PriorityQueue` | Heap |
| Стек LIFO | `ArrayDeque` | Быстрее чем Stack |

---

## 📌 Слайд 9: Итоги

✅ **List** — упорядоченная последовательность с дубликатами. `ArrayList` — выбор по умолчанию.

✅ **Set** — уникальные элементы. `HashSet` — быстро, `TreeSet` — сортировка, `LinkedHashSet` — порядок вставки.

✅ **Queue/Deque** — очереди. `ArrayDeque` — лучший выбор для стека и очереди.

✅ `equals` + `hashCode` — **всегда переопределять вместе** для объектов в Set/Map.

✅ `Collections.unmodifiableList()` / `List.of()` — для неизменяемых коллекций.

---

## 📌 Слайд 10: Домашнее задание

```java
// 1. Задача на List:
// Дан список слов, найти все уникальные слова длиной > 4 символов,
// отсортировать по алфавиту, вывести с порядковым номером.

// 2. Задача на Set:
// Даны два списка студентов. Найти:
// - студентов, присутствующих в обоих списках (пересечение)
// - студентов только из первого списка (разность)
// - всех студентов (объединение)

// 3. Задача на Queue:
// Реализовать симуляцию кассы в магазине:
// - покупатели приходят в очередь (offer)
// - кассир обслуживает первого (poll)
// - вывести статистику: сколько обслужено, средняя длина очереди

// Бонус: реализовать LRU Cache через LinkedHashMap
```

---

## ❓ Вопросы для самопроверки

1. В чём разница между `ArrayList` и `LinkedList`?
2. Почему важно переопределять `equals` и `hashCode` вместе?
3. Какой порядок элементов гарантирует `TreeSet`?
4. Когда использовать `PriorityQueue`?
5. Что вернёт `Collections.binarySearch` на неотсортированном списке?

---

## 📚 Дополнительные материалы

| Ресурс | Описание |
|---|---|
| [Oracle — Collections](https://docs.oracle.com/javase/tutorial/collections/) | Официальная документация |
| «Effective Java» Item 28-29 | Списки vs массивы |
| [Baeldung — Java Collections](https://www.baeldung.com/java-collections) | Практические примеры |

---

*Лекция 7 из 19 | Курс: Введение в ООП на Java | Семестр 2*

