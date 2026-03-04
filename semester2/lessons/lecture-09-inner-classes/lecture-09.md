# Лекция 9: Внутренние и анонимные классы

---

## 🗂️ План лекции

1. Внутренние классы (Inner Classes) — виды
2. Нестатические внутренние классы
3. Статические вложенные классы
4. Локальные классы
5. Анонимные классы
6. Сравнение всех типов
7. Паттерн Builder с вложенным классом
8. Итоги и домашнее задание

---

## 📌 Слайд 1: Виды внутренних классов

```
Класс внутри класса — 4 вида:

1. Inner Class (нестатический внутренний)
   — имеет доступ к полям внешнего класса
   — нужен экземпляр внешнего класса

2. Static Nested Class (статический вложенный)
   — не привязан к экземпляру внешнего класса
   — как обычный класс, но "живёт" внутри другого

3. Local Class (локальный)
   — объявляется внутри метода
   — виден только в этом методе

4. Anonymous Class (анонимный)
   — класс без имени, реализует интерфейс или наследует класс
   — объявляется и создаётся в одном месте
```

---

## 📌 Слайд 2: Нестатический внутренний класс

```java
public class OuterClass {
    private String outerField = "Внешнее поле";
    private int    count = 0;

    // ── Внутренний класс — имеет доступ к внешнему ────────
    public class InnerClass {
        private String innerField = "Внутреннее поле";

        public void display() {
            // Может обращаться к private полям внешнего класса!
            System.out.println(outerField);   // ← доступ к внешнему
            System.out.println(innerField);
            count++;                          // ← изменяет внешнее поле
        }

        // Если поля совпадают по имени:
        public void showFields(String outerField) {
            System.out.println(outerField);          // параметр
            System.out.println(this.innerField);     // поле Inner
            System.out.println(OuterClass.this.outerField); // поле Outer
        }
    }

    public InnerClass createInner() {
        return new InnerClass();
    }
}

// ── Создание ──────────────────────────────────────────────
OuterClass outer = new OuterClass();
OuterClass.InnerClass inner = outer.new InnerClass(); // нужен экземпляр outer!
inner.display();

// Или через фабричный метод:
OuterClass.InnerClass inner2 = outer.createInner();
```

### Практический пример — итератор:
```java
public class NumberRange {
    private final int start;
    private final int end;

    public NumberRange(int start, int end) {
        this.start = start;
        this.end   = end;
    }

    // Внутренний класс итератора
    public class RangeIterator implements Iterator<Integer> {
        private int current = start; // доступ к полям внешнего!

        @Override public boolean hasNext() { return current <= end; }
        @Override public Integer next()    { return current++;      }
    }

    public Iterator<Integer> iterator() {
        return new RangeIterator();
    }
}

NumberRange range = new NumberRange(1, 5);
Iterator<Integer> it = range.iterator();
while (it.hasNext()) {
    System.out.print(it.next() + " "); // 1 2 3 4 5
}
```

---

## 📌 Слайд 3: Статический вложенный класс

```java
public class LinkedList<T> {

    // ── Статический вложенный класс — не нужен внешний объект ──
    private static class Node<T> {      // static!
        T    data;
        Node<T> next;

        Node(T data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node<T> head;
    private int     size;

    public void add(T data) {
        Node<T> newNode = new Node<>(data); // создаём без внешнего объекта
        if (head == null) {
            head = newNode;
        } else {
            Node<T> current = head;
            while (current.next != null) current = current.next;
            current.next = newNode;
        }
        size++;
    }

    public void print() {
        Node<T> current = head;
        while (current != null) {
            System.out.print(current.data + " → ");
            current = current.next;
        }
        System.out.println("null");
    }
}

// ── Создание статического вложенного класса ───────────────
// LinkedList.Node<String> node = new LinkedList.Node<>("test"); // ← если public
```

---

## 📌 Слайд 4: Локальные классы

```java
public class Formatter {

    public String formatNumbers(List<Integer> numbers, boolean showIndex) {

        // ── Локальный класс — живёт только внутри метода ──
        class NumberEntry {
            final int index;
            final int value;

            NumberEntry(int index, int value) {
                this.index = index;
                this.value = value;
            }

            // Доступ к effectively final переменным метода!
            String format() {
                if (showIndex) {          // ← захватывает переменную метода
                    return index + ": " + value;
                }
                return String.valueOf(value);
            }
        }

        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < numbers.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(new NumberEntry(i, numbers.get(i)).format());
        }
        return sb.append("]").toString();
    }
}

Formatter f = new Formatter();
System.out.println(f.formatNumbers(Arrays.asList(10, 20, 30), true));
// [0: 10, 1: 20, 2: 30]
System.out.println(f.formatNumbers(Arrays.asList(10, 20, 30), false));
// [10, 20, 30]
```

---

## 📌 Слайд 5: Анонимные классы

```java
// ── Анонимный класс — реализует интерфейс ────────────────
Comparator<String> byLength = new Comparator<String>() {
    @Override
    public int compare(String a, String b) {
        return Integer.compare(a.length(), b.length());
    }
};

List<String> words = Arrays.asList("банан", "яблоко", "груша", "ки");
words.sort(byLength);
System.out.println(words); // [ки, груша, банан, яблоко]

// ── Анонимный класс — наследует класс ────────────────────
abstract class Greeter {
    abstract String greet(String name);

    void printGreeting(String name) {
        System.out.println(greet(name));
    }
}

Greeter formal = new Greeter() {     // анонимный подкласс Greeter
    @Override
    String greet(String name) {
        return "Уважаемый " + name + "!";
    }
};

Greeter casual = new Greeter() {
    @Override
    String greet(String name) {
        return "Привет, " + name + "!";
    }
};

formal.printGreeting("Иван"); // Уважаемый Иван!
casual.printGreeting("Иван"); // Привет, Иван!

// ── Анонимный класс с дополнительными полями ─────────────
Runnable counter = new Runnable() {
    private int count = 0; // поле анонимного класса

    @Override
    public void run() {
        count++;
        System.out.println("Вызов #" + count);
    }
};

counter.run(); // Вызов #1
counter.run(); // Вызов #2
counter.run(); // Вызов #3
```

---

## 📌 Слайд 6: Анонимные классы vs Лямбды

```java
// ── Анонимный класс (до Java 8) ──────────────────────────
Runnable oldWay = new Runnable() {
    @Override
    public void run() {
        System.out.println("Привет из анонимного класса!");
    }
};

// ── Лямбда (Java 8+) — короче, если один абстрактный метод ──
Runnable lambda = () -> System.out.println("Привет из лямбды!");

// ── Когда НУЖЕН анонимный класс (лямбда не поможет) ──────
// 1. Несколько методов
Comparator<String> complex = new Comparator<String>() {
    @Override
    public int compare(String a, String b) { return a.compareTo(b); }

    // equals можно переопределить только в анонимном классе
    @Override
    public boolean equals(Object obj) { return false; }
};

// 2. Нужны поля или инициализатор
Iterator<Integer> statefulIterator = new Iterator<Integer>() {
    private int current = 0;  // ← поле — только в анонимном классе
    @Override public boolean hasNext() { return current < 10; }
    @Override public Integer next()    { return current++;    }
};
```

| | Анонимный класс | Лямбда |
|---|---|---|
| Синтаксис | Подробный | Компактный |
| Поля | ✅ Может иметь | ❌ |
| Несколько методов | ✅ | ❌ |
| Доступ к `this` | Свой экземпляр | Внешний объект |
| Когда использовать | Нужно состояние/несколько методов | Функциональный интерфейс |

---

## 📌 Слайд 7: Паттерн Builder

```java
// ── Builder с статическим вложенным классом ───────────────
public class HttpRequest {
    // Поля запроса — только чтение!
    private final String  url;
    private final String  method;
    private final Map<String, String> headers;
    private final String  body;
    private final int     timeout;

    // Приватный конструктор — только Builder может создать!
    private HttpRequest(Builder builder) {
        this.url     = builder.url;
        this.method  = builder.method;
        this.headers = Collections.unmodifiableMap(builder.headers);
        this.body    = builder.body;
        this.timeout = builder.timeout;
    }

    // ── Статический вложенный Builder ─────────────────────
    public static class Builder {
        private final String url;                           // обязательное
        private String method  = "GET";                    // по умолчанию
        private Map<String, String> headers = new HashMap<>();
        private String body    = null;
        private int    timeout = 30;

        public Builder(String url) {
            this.url = Objects.requireNonNull(url, "URL не может быть null");
        }

        public Builder method(String method) {
            this.method = method;
            return this; // ← возврат this для цепочки вызовов
        }

        public Builder header(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder timeout(int seconds) {
            if (seconds <= 0) throw new IllegalArgumentException("Timeout > 0");
            this.timeout = seconds;
            return this;
        }

        public HttpRequest build() {
            if ("POST".equals(method) && body == null) {
                throw new IllegalStateException("POST-запрос должен иметь тело");
            }
            return new HttpRequest(this);
        }
    }

    @Override
    public String toString() {
        return method + " " + url + " (timeout=" + timeout + "s)";
    }
}

// ── Использование — цепочка вызовов ──────────────────────
HttpRequest request = new HttpRequest.Builder("https://api.example.com/users")
    .method("POST")
    .header("Content-Type", "application/json")
    .header("Authorization", "Bearer token123")
    .body("{\"name\": \"Иван\"}")
    .timeout(60)
    .build();

System.out.println(request); // POST https://api.example.com/users (timeout=60s)
```

---

## 📌 Слайд 8: Сравнение типов внутренних классов

| Тип | Объявление | Доступ к внешнему | Нужен экземпляр | Когда использовать |
|---|---|---|---|---|
| Inner | Нестатическое поле | Полный | ✅ Да | Итераторы, View-объекты |
| Static Nested | Статическое поле | Нет | ❌ Нет | Вспомогательные структуры, Builder |
| Local | Внутри метода | Effectively final | N/A | Локальная логика |
| Anonymous | Выражение | Effectively final | N/A | Один-два метода, Callbacks |

---

## 📌 Слайд 9: Итоги

✅ **Inner class** — имеет доступ к private членам внешнего. Нужен экземпляр.

✅ **Static nested class** — не зависит от экземпляра. Идеален для Builder, Node.

✅ **Local class** — локальная логика внутри метода. Редко нужен.

✅ **Anonymous class** — быстрая реализация интерфейса. В Java 8+ заменяется лямбдами.

✅ **Паттерн Builder** — использует static nested class для удобного конструирования объектов.

---

## 📌 Слайд 10: Домашнее задание

```java
// 1. Реализовать паттерн Builder для класса User:
// User.builder()
//     .name("Иван")
//     .email("ivan@example.com")
//     .age(25)
//     .role(Role.USER)
//     .build();
// Валидация: email обязателен, имя >= 2 символов

// 2. Реализовать итерируемый класс Matrix с внутренним итератором:
// Matrix matrix = new Matrix(3, 3);
// for (int value : matrix) { System.out.println(value); }

// 3. Написать сортировку с анонимным Comparator и с лямбдой — сравнить код
```

---

## ❓ Вопросы для самопроверки

1. В чём разница между `Inner Class` и `Static Nested Class`?
2. Что значит "effectively final" для переменных, захватываемых анонимным классом?
3. Когда лямбда не может заменить анонимный класс?
4. Почему в паттерне Builder вложенный класс делают `static`?
5. Может ли анонимный класс реализовывать несколько интерфейсов?

---

*Лекция 9 из 19 | Курс: Введение в ООП на Java | Семестр 2*

