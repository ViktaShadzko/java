# Лекция 18: Garbage Collector и управление памятью

---

## 🗂️ План лекции

1. Память JVM — структура
2. Stack vs Heap
3. Garbage Collector — как работает
4. Алгоритмы GC
5. Утечки памяти в Java
6. Слабые ссылки (WeakReference, SoftReference)
7. Профилирование и настройка GC
8. Итоги

---

## 📌 Слайд 1: Структура памяти JVM

```
JVM Memory
├── Heap (Куча) — объекты, создаваемые через new
│   ├── Young Generation (молодое поколение)
│   │   ├── Eden Space       — новые объекты
│   │   ├── Survivor S0      — пережившие 1+ GC
│   │   └── Survivor S1
│   └── Old Generation (Tenured) — долгоживущие объекты
│
├── Stack (Стек) — per-thread
│   ├── Stack Frame (для каждого вызова метода)
│   │   ├── Локальные переменные
│   │   ├── Ссылки на объекты в heap
│   │   └── Операнды
│   └── Stack Frame...
│
├── Metaspace (Java 8+) — метаданные классов (ранее PermGen)
├── Code Cache — скомпилированный JIT-код
└── Native Memory — JVM инфраструктура
```

---

## 📌 Слайд 2: Stack vs Heap

```java
public class MemoryExample {

    public static void main(String[] args) {
        // ── STACK ──────────────────────────────────────────
        int x = 10;         // примитив — на стеке
        int y = 20;         // примитив — на стеке

        // ── HEAP ───────────────────────────────────────────
        String s1 = new String("hello"); // объект — на heap, ссылка s1 — на стеке
        String s2 = "hello";             // string pool (часть heap)

        calculate(x, y);    // новый Stack Frame создаётся
        // после return — Stack Frame удаляется
    }

    public static int calculate(int a, int b) {
        // a, b — копии значений, на стеке этого фрейма
        int result = a + b; // result — на стеке
        return result;      // фрейм удаляется, result исчезает
    }
}
```

| | Stack | Heap |
|---|---|---|
| Хранит | Примитивы, ссылки, frame | Объекты |
| Размер | Небольшой (~1MB) | Большой (настраивается) |
| Управление | Автоматически (LIFO) | GC |
| Скорость | Очень быстрая | Медленнее |
| Thread-safe | Да (каждый поток — свой стек) | Нет (общий) |
| `StackOverflowError` | При переполнении | — |
| `OutOfMemoryError` | — | При переполнении |

---

## 📌 Слайд 3: Garbage Collector — принцип

> **GC** — автоматически освобождает память, занятую объектами, на которые **нет ссылок**.

```java
public class GcExample {
    public static void main(String[] args) {
        // ── Создание объектов ─────────────────────────────
        String a = new String("объект 1"); // heap: [объект 1]
        String b = new String("объект 2"); // heap: [объект 1, объект 2]

        a = b;    // a теперь указывает на "объект 2"
                  // "объект 1" больше недоступен → будет собран GC!

        b = null; // b теперь null
                  // "объект 2" всё ещё доступен через a!

        // GC запустится когда посчитает нужным (НЕ сразу)
        System.gc(); // просьба запустить GC — Java может проигнорировать!
    }
}

// ── Пример достижимости ───────────────────────────────────
public class Node {
    Node next;
    String data;
}

Node n1 = new Node(); // достижим через n1
Node n2 = new Node(); // достижим через n2
n1.next = n2;         // n2 достижим через n1.next

n2 = null;            // прямая ссылка на n2 потеряна...
                      // но n2 ещё достижим через n1.next!
                      // → GC его НЕ соберёт!

n1 = null;            // теперь ни n1, ни n2 недостижимы
                      // → оба будут собраны GC
```

---

## 📌 Слайд 4: Алгоритм Mark-and-Sweep

```
Фаза 1: MARK (Пометить)
- Начинаем с "GC Roots" (стек, статические поля, JNI ссылки)
- Обходим граф объектов
- Помечаем всё достижимое как "живое"

Фаза 2: SWEEP (Сметь)
- Освобождаем всё непомеченное
- (Compact) Перемещаем оставшиеся объекты, убираем фрагментацию

Поколенческая гипотеза:
"Большинство объектов умирают молодыми"

Young GC (Minor GC) — быстро
- Собирает только Young Generation
- Выжившие перемещаются в Survivor, затем в Old

Full GC (Major GC) — медленно
- Собирает весь Heap
- Вызывает "Stop-the-World" паузу!
```

---

## 📌 Слайд 5: Виды GC в Java

| GC | Команда | Когда использовать |
|---|---|---|
| **Serial GC** | `-XX:+UseSerialGC` | Маленькие приложения, один поток |
| **Parallel GC** | `-XX:+UseParallelGC` | Пропускная способность (throughput) |
| **G1 GC** | `-XX:+UseG1GC` | Баланс паузы/пропускной (Java 9+ default) |
| **ZGC** | `-XX:+UseZGC` | Очень малые паузы (Java 15+) |
| **Shenandoah** | `-XX:+UseShenandoahGC` | Малые паузы (OpenJDK) |

```bash
# Настройка размера heap
java -Xms512m -Xmx2g -jar app.jar
# -Xms — начальный размер heap
# -Xmx — максимальный размер heap

# GC логирование
java -Xlog:gc*:gc.log -jar app.jar

# Выбор GC
java -XX:+UseG1GC -XX:MaxGCPauseMillis=200 -jar app.jar
```

---

## 📌 Слайд 6: Утечки памяти в Java

> В Java есть GC, но утечки памяти **всё равно возможны**! Если объект достижим, но не нужен — это утечка.

```java
// ── Утечка 1: забытые элементы в статической коллекции ───
public class EventBus {
    private static final List<EventListener> listeners = new ArrayList<>();

    public static void subscribe(EventListener l) {
        listeners.add(l);       // добавляем...
    }

    // Нет метода unsubscribe! Listeners никогда не удаляются!
    // Если listeners — GUI компоненты, они не будут собраны GC!
}

// ── Утечка 2: неправильная реализация кэша ───────────────
public class BadCache {
    private static final Map<String, HeavyObject> cache = new HashMap<>();

    public static HeavyObject get(String key) {
        return cache.computeIfAbsent(key, k -> new HeavyObject(k));
    }
    // кэш растёт бесконечно! нет ограничения размера, нет TTL
}

// ── Утечка 3: незакрытые ресурсы ────────────────────────
public class LeakyReader {
    public String readFile(String path) throws IOException {
        FileReader reader = new FileReader(path); // ← открываем
        BufferedReader br = new BufferedReader(reader);
        return br.readLine();
        // ❌ reader и br никогда не закрываются!
    }
    // Используй try-with-resources!
}

// ── Утечка 4: ThreadLocal без удаления ───────────────────
ThreadLocal<HeavyObject> tl = ThreadLocal.withInitial(HeavyObject::new);
// В thread pool потоки живут долго → объекты в ThreadLocal тоже!
// Всегда: tl.remove() после использования!
```

---

## 📌 Слайд 7: Слабые ссылки

```java
import java.lang.ref.*;

// ── Strong Reference (обычная) — GC не удалит ────────────
String strong = new String("Привет");
// Пока strong != null — объект не удаляется

// ── WeakReference — удаляется при первом же GC ───────────
WeakReference<String> weak = new WeakReference<>(new String("слабый"));
System.gc();
System.out.println(weak.get()); // null — уже собран!

// Практический пример — WeakHashMap (кэш без утечек)
Map<Object, String> cache = new WeakHashMap<>();
Object key = new Object();
cache.put(key, "значение");

System.out.println(cache.get(key)); // значение

key = null;     // ключ недоступен
System.gc();
System.out.println(cache.size()); // 0 — запись автоматически удалена!

// ── SoftReference — удаляется только при нехватке памяти ─
SoftReference<byte[]> soft = new SoftReference<>(new byte[1024 * 1024]); // 1MB

// Пока памяти хватает — объект живёт
byte[] data = soft.get();
if (data != null) {
    // используем
} else {
    // GC удалил при нехватке памяти — перечитываем
}
// ← идеально для кэшей изображений!

// ── PhantomReference — только для finalization ────────────
ReferenceQueue<Object> queue = new ReferenceQueue<>();
PhantomReference<Object> phantom = new PhantomReference<>(new Object(), queue);
// phantom.get() всегда возвращает null
// Используется для кастомной очистки ресурсов
```

---

## 📌 Слайд 8: finalize() и Cleaner

```java
// ── finalize() — устарел, не используй! ──────────────────
// Вызывается перед сборкой — но:
// - нет гарантии когда вызовется
// - может замедлить GC
// - может воскресить объект (zombie object!)
@Deprecated
@Override
protected void finalize() throws Throwable {
    // ❌ Не делай так! Используй try-with-resources или Cleaner
}

// ── Cleaner (Java 9+) — правильная замена ────────────────
import java.lang.ref.Cleaner;

public class ManagedResource implements AutoCloseable {
    private static final Cleaner cleaner = Cleaner.create();

    private final Cleaner.Cleanable cleanable;
    private final NativeResource nativeResource;

    public ManagedResource() {
        this.nativeResource = new NativeResource();
        // Регистрируем действие очистки
        this.cleanable = cleaner.register(this, nativeResource::cleanup);
    }

    @Override
    public void close() {
        cleanable.clean(); // явная очистка (через try-with-resources)
    }

    // Если close() не вызван — cleaner вызовет cleanup() при GC
    private static class NativeResource {
        void cleanup() {
            System.out.println("Ресурс освобождён!");
        }
    }
}
```

---

## 📌 Слайд 9: Практические советы

```java
// ── 1. Освобождай ссылки явно в long-lived объектах ──────
public class LargeCache {
    private Object[] elements = new Object[100];
    private int size = 0;

    public Object pop() {
        Object e = elements[--size];
        elements[size] = null; // ← явно зануляем освобождённую ячейку!
        return e;
    }
}

// ── 2. Используй пул строк ────────────────────────────────
String s1 = new String("hello"); // новый объект в heap
String s2 = "hello";              // из string pool

String s3 = s1.intern();          // помещает в pool и возвращает ссылку
System.out.println(s2 == s3);     // true — одна и та же ссылка

// ── 3. Limit collection sizes ─────────────────────────────
// LRU Cache через LinkedHashMap с ограниченным размером (лекция 8)

// ── 4. Осторожно с внутренними классами ──────────────────
// Нестатический внутренний класс хранит ссылку на внешний!
// Если внутренний живёт дольше внешнего — утечка!

// ❌ Анонимный класс в Activity (Android-пример проблемы)
// Button btn = new Button();
// btn.setOnClickListener(new OnClickListener() {
//     void onClick() { /* неявно хранит ссылку на Activity */ }
// });
// Используй static inner class или слабую ссылку
```

---

## 📌 Слайд 10: Итоги

✅ **Heap** — все объекты. **Stack** — примитивы, ссылки, вызовы методов.

✅ **GC** — автоматически освобождает недостижимые объекты. НЕ вызывай `System.gc()`!

✅ **Young/Old Generation** — объекты продвигаются. Minor GC — быстро, Full GC — медленно.

✅ **Stop-the-World** — приложение паузируется во время GC. G1/ZGC минимизируют паузы.

✅ **Утечки** возможны! Статические коллекции, незакрытые ресурсы, ThreadLocal без remove().

✅ **WeakReference** — исчезает при GC. Основа `WeakHashMap`.

✅ **SoftReference** — исчезает при нехватке памяти. Идеален для кэшей.

✅ Используй `try-with-resources` и `Cleaner` вместо `finalize()`.

---

## 📌 Слайд 11: Домашнее задание

```java
// 1. Найти утечку памяти:
// В коде ниже найди все утечки памяти и исправь их:
class SessionManager {
    static Map<String, Session> sessions = new HashMap<>();
    static List<Runnable> shutdownHooks = new ArrayList<>();

    public Session createSession(String userId) {
        Session s = new Session(userId, new byte[1024 * 1024]);
        sessions.put(userId, s);
        return s;
    }

    public void addHook(Runnable r) { shutdownHooks.add(r); }
    // Нет метода invalidateSession()!
    // Нет метода removeHook()!
}

// 2. Реализовать Image Cache через SoftReference:
// - кэш хранит изображения
// - при нехватке памяти GC удаляет их автоматически
// - при промахе кэша — перечитываем из файла

// 3. Написать MemoryMonitor:
// - каждые 5 секунд выводит: total heap, used heap, free heap
// - использует Runtime.getRuntime().totalMemory() / freeMemory()
```

---

## ❓ Вопросы для самопроверки

1. Что хранится на стеке, а что на heap?
2. Что такое "Stop-the-World" пауза?
3. В чём разница между Minor GC и Full GC?
4. Почему `System.gc()` не рекомендуется вызывать?
5. Чем `WeakReference` отличается от `SoftReference`?
6. Приведи пример утечки памяти в Java.

---

*Лекция 18 из 19 | Курс: Введение в ООП на Java | Семестр 2*

