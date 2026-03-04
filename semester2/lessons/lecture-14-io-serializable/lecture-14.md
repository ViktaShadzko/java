# Лекция 14: IO Streams, работа с файлами. Serializable, Cloneable

---

## 🗂️ План лекции

1. Иерархия IO Streams
2. Байтовые потоки (InputStream / OutputStream)
3. Символьные потоки (Reader / Writer)
4. Буферизованные потоки
5. Files и Path (NIO.2)
6. Сериализация (Serializable)
7. Cloneable и клонирование
8. Итоги и домашнее задание

---

## 📌 Слайд 1: Иерархия IO

```
java.io
├── InputStream (байты)
│   ├── FileInputStream
│   ├── ByteArrayInputStream
│   └── BufferedInputStream (обёртка)
├── OutputStream (байты)
│   ├── FileOutputStream
│   ├── ByteArrayOutputStream
│   └── BufferedOutputStream
├── Reader (символы/Unicode)
│   ├── FileReader
│   ├── StringReader
│   ├── BufferedReader
│   └── InputStreamReader (байты → символы)
└── Writer (символы/Unicode)
    ├── FileWriter
    ├── StringWriter
    ├── BufferedWriter
    └── PrintWriter

java.nio.file (NIO.2 — современный способ)
├── Path
├── Files
└── Paths
```

> **Правило выбора:**
> - Текст → `Reader/Writer`
> - Бинарные данные, изображения → `InputStream/OutputStream`
> - Новый код → `java.nio.file.Files` (NIO.2)

---

## 📌 Слайд 2: Байтовые потоки

```java
// ── Запись байтов в файл ──────────────────────────────────
try (FileOutputStream fos = new FileOutputStream("data.bin");
     BufferedOutputStream bos = new BufferedOutputStream(fos)) {

    byte[] data = {72, 101, 108, 108, 111}; // "Hello" в ASCII
    bos.write(data);
    bos.flush(); // сбросить буфер на диск
}

// ── Чтение байтов из файла ────────────────────────────────
try (FileInputStream fis = new FileInputStream("data.bin");
     BufferedInputStream bis = new BufferedInputStream(fis)) {

    byte[] buffer = new byte[1024];
    int bytesRead;
    while ((bytesRead = bis.read(buffer)) != -1) { // -1 = конец файла
        System.out.write(buffer, 0, bytesRead);
    }
}

// ── Копирование файла ─────────────────────────────────────
public static void copyFile(String src, String dest) throws IOException {
    try (FileInputStream  in  = new FileInputStream(src);
         FileOutputStream out = new FileOutputStream(dest)) {

        byte[] buffer = new byte[8192]; // 8KB буфер
        int n;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
    }
}
```

---

## 📌 Слайд 3: Символьные потоки

```java
// ── Запись текста ─────────────────────────────────────────
try (BufferedWriter writer = new BufferedWriter(
        new FileWriter("students.txt", StandardCharsets.UTF_8))) {

    writer.write("Имя,Возраст,GPA");
    writer.newLine(); // системный разделитель строк
    writer.write("Иван,20,8.5");
    writer.newLine();
    writer.write("Мария,21,9.2");
    writer.newLine();
}

// ── Чтение текста построчно ───────────────────────────────
try (BufferedReader reader = new BufferedReader(
        new FileReader("students.txt", StandardCharsets.UTF_8))) {

    String line;
    while ((line = reader.readLine()) != null) { // null = конец файла
        System.out.println(line);
    }
}

// ── PrintWriter — удобный форматированный вывод ───────────
try (PrintWriter pw = new PrintWriter(
        new BufferedWriter(new FileWriter("report.txt")))) {

    pw.println("=== ОТЧЁТ ===");
    pw.printf("Студент: %-20s GPA: %.2f%n", "Иван Иванов", 8.5);
    pw.printf("Студент: %-20s GPA: %.2f%n", "Мария Петрова", 9.2);
}
```

---

## 📌 Слайд 4: NIO.2 — Files и Path (современный способ)

```java
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

// ── Path ──────────────────────────────────────────────────
Path path = Path.of("data", "students.txt");  // data/students.txt
Path abs  = path.toAbsolutePath();
Path parent = path.getParent();               // data
Path name   = path.getFileName();             // students.txt

System.out.println(path.toString());          // data\students.txt (Windows)

// ── Запись файла — одна строка! ───────────────────────────
List<String> lines = Arrays.asList("Иван,20,8.5", "Мария,21,9.2");
Files.write(Path.of("students.txt"), lines, StandardCharsets.UTF_8);

// Дозапись
Files.write(Path.of("students.txt"),
    Arrays.asList("Пётр,22,7.8"),
    StandardCharsets.UTF_8,
    StandardOpenOption.APPEND);

// ── Чтение файла ──────────────────────────────────────────
// Всё сразу (для небольших файлов)
List<String> allLines = Files.readAllLines(Path.of("students.txt"), StandardCharsets.UTF_8);
String content = Files.readString(Path.of("students.txt")); // Java 11+

// Стрим строк (ленивый — для больших файлов)
try (Stream<String> stream = Files.lines(Path.of("students.txt"))) {
    stream.filter(l -> !l.startsWith("#"))
          .map(String::trim)
          .forEach(System.out::println);
}

// ── Операции с файлами ────────────────────────────────────
Path src  = Path.of("source.txt");
Path dest = Path.of("destination.txt");

Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
Files.move(src, dest, StandardCopyOption.REPLACE_EXISTING);
Files.delete(dest);
Files.deleteIfExists(dest); // не бросает если нет

// ── Проверки ──────────────────────────────────────────────
Files.exists(path);
Files.isDirectory(path);
Files.isReadable(path);
Files.size(path);        // размер в байтах
Files.getLastModifiedTime(path);

// ── Создание директорий ───────────────────────────────────
Files.createDirectory(Path.of("output"));
Files.createDirectories(Path.of("output/2026/reports")); // создаёт все уровни

// ── Список файлов ─────────────────────────────────────────
try (Stream<Path> files = Files.list(Path.of("."))) {
    files.filter(Files::isRegularFile)
         .filter(p -> p.toString().endsWith(".java"))
         .forEach(System.out::println);
}

// Рекурсивный обход
try (Stream<Path> walk = Files.walk(Path.of("src"))) {
    walk.filter(Files::isRegularFile)
        .forEach(System.out::println);
}
```

---

## 📌 Слайд 5: Сериализация (Serializable)

> **Сериализация** — преобразование объекта в байты (для сохранения или передачи по сети).

```java
// ── Класс должен реализовывать Serializable ───────────────
public class Student implements Serializable {
    // serialVersionUID — версия для совместимости
    private static final long serialVersionUID = 1L;

    private String name;
    private int    age;
    private double gpa;

    // transient — поле НЕ сериализуется
    private transient String password; // пароли не сохраняем!
    private transient Connection dbConnection; // соединения не сохраняем

    public Student(String name, int age, double gpa) {
        this.name = name;
        this.age  = age;
        this.gpa  = gpa;
    }
    // геттеры...
}

// ── Сериализация (запись) ─────────────────────────────────
Student student = new Student("Иван", 20, 8.5);

try (ObjectOutputStream oos = new ObjectOutputStream(
        new BufferedOutputStream(new FileOutputStream("student.ser")))) {

    oos.writeObject(student);
    System.out.println("Сохранено!");
}

// ── Десериализация (чтение) ───────────────────────────────
try (ObjectInputStream ois = new ObjectInputStream(
        new BufferedInputStream(new FileInputStream("student.ser")))) {

    Student loaded = (Student) ois.readObject();
    System.out.println(loaded.getName()); // Иван
    System.out.println(loaded.getGpa());  // 8.5
    // password == null — transient поле не восстановлено
}

// ── Сериализация списка ───────────────────────────────────
List<Student> students = Arrays.asList(
    new Student("Иван",  20, 8.5),
    new Student("Мария", 21, 9.2)
);

try (ObjectOutputStream oos = new ObjectOutputStream(
        new FileOutputStream("students.ser"))) {
    oos.writeObject(students);
}
```

---

## 📌 Слайд 6: Cloneable и клонирование

```java
// ── Поверхностное клонирование (Shallow Copy) ────────────
public class Address implements Cloneable {
    private String city;
    private String street;

    @Override
    public Address clone() {
        try {
            return (Address) super.clone(); // ← создаёт новый объект с теми же полями
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}

public class Person implements Cloneable {
    private String  name;
    private int     age;
    private Address address; // ← ссылочное поле!

    @Override
    public Person clone() {
        try {
            return (Person) super.clone();
            // ⚠️ address — та же ссылка! Shallow Copy
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}

Person p1 = new Person("Иван", 20, new Address("Москва", "Ленина 1"));
Person p2 = p1.clone();

p2.setName("Мария");         // не влияет на p1 (String иммутабельный)
p2.getAddress().setCity("СПб"); // ❌ влияет на p1! Тот же объект Address!

// ── Глубокое клонирование (Deep Copy) ────────────────────
public class PersonDeep implements Cloneable {
    private String  name;
    private int     age;
    private Address address;

    @Override
    public PersonDeep clone() {
        try {
            PersonDeep copy = (PersonDeep) super.clone();
            copy.address = this.address.clone(); // ← клонируем вложенный объект!
            return copy;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }
}

// ── Альтернатива clone — конструктор копирования ──────────
public class Student {
    private String       name;
    private List<String> courses; // изменяемый список

    // Конструктор копирования — явный и безопасный
    public Student(Student other) {
        this.name    = other.name;                     // String иммутабельный
        this.courses = new ArrayList<>(other.courses); // ← новая коллекция!
    }
}

Student original = new Student("Иван", Arrays.asList("Java", "Math"));
Student copy     = new Student(original); // глубокая копия

copy.getCourses().add("Physics");
System.out.println(original.getCourses()); // [Java, Math] — не изменился!
```

---

## 📌 Слайд 7: Итоги

✅ **Байтовые потоки** (`InputStream/OutputStream`) — для бинарных данных.

✅ **Символьные потоки** (`Reader/Writer`) — для текста. Всегда указывай кодировку!

✅ **Буферизация** — всегда оборачивай в `Buffered*` для производительности.

✅ **NIO.2** (`java.nio.file.Files`) — современный способ работы с файлами.

✅ **try-with-resources** — единственный правильный способ работы с потоками.

✅ **Serializable** — помечает класс для сериализации. `transient` — исключает поле.

✅ **Shallow vs Deep copy** — `clone()` по умолчанию поверхностный. Для вложенных объектов — клонировать явно.

---

## 📌 Слайд 8: Домашнее задание

```java
// 1. CSV-работа с файлами:
// Написать StudentCsvService:
// - void saveAll(List<Student> students, Path path)  — записать в CSV
// - List<Student> loadAll(Path path)                 — прочитать из CSV
// - void appendStudent(Student s, Path path)          — дозаписать строку

// 2. Сериализация:
// - Сохранить List<Student> в бинарный файл
// - Загрузить и убедиться что данные совпадают
// - Добавить поле password с @transient — убедиться что не сохраняется

// 3. Deep Copy:
// Реализовать класс Classroom с полями:
// - String name
// - List<Student> students    ← изменяемый список
// Добавить конструктор копирования и продемонстрировать
// что изменения в копии не влияют на оригинал
```

---

## ❓ Вопросы для самопроверки

1. Чем `Reader` отличается от `InputStream`?
2. Зачем нужна буферизация?
3. Что делает `transient`?
4. Что такое `serialVersionUID` и зачем он нужен?
5. В чём разница между поверхностным и глубоким копированием?
6. Почему лучше конструктор копирования, чем `Cloneable`?

---

*Лекция 14 из 19 | Курс: Введение в ООП на Java | Семестр 2*

