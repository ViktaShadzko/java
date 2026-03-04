# Лекция 16: XML и JSON парсинг

---

## 🗂️ План лекции

1. Форматы данных — XML vs JSON
2. XML — структура и синтаксис
3. Парсинг XML: SAX, DOM, StAX
4. JSON — структура и синтаксис
5. Jackson — основной инструмент
6. Gson — альтернатива
7. Практические примеры
8. Итоги и домашнее задание

---

## 📌 Слайд 1: XML vs JSON

```xml
<!-- XML — многословный, но мощный -->
<?xml version="1.0" encoding="UTF-8"?>
<students>
    <student id="1">
        <name>Иван Иванов</name>
        <age>20</age>
        <gpa>8.5</gpa>
        <courses>
            <course>Java</course>
            <course>Math</course>
        </courses>
    </student>
</students>
```

```json
// JSON — компактный, популярный для REST API
{
  "students": [
    {
      "id": 1,
      "name": "Иван Иванов",
      "age": 20,
      "gpa": 8.5,
      "courses": ["Java", "Math"]
    }
  ]
}
```

| Критерий | XML | JSON |
|---|---|---|
| Читаемость | Хорошая | Отличная |
| Размер | Больше | Меньше |
| Схема | XSD, DTD | JSON Schema |
| Пространства имён | ✅ | ❌ |
| Комментарии | ✅ | ❌ |
| Популярность (REST API) | Устаревает | Стандарт |
| Популярность (Config) | Maven, Spring XML | — |

---

## 📌 Слайд 2: XML — DOM парсинг

```java
import org.w3c.dom.*;
import javax.xml.parsers.*;

// ── XML файл: students.xml ────────────────────────────────
/*
<students>
    <student id="1">
        <name>Иван</name>
        <gpa>8.5</gpa>
    </student>
    <student id="2">
        <name>Мария</name>
        <gpa>9.2</gpa>
    </student>
</students>
*/

// ── DOM парсинг — всё в памяти ───────────────────────────
DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
DocumentBuilder builder = factory.newDocumentBuilder();
Document doc = builder.parse(new File("students.xml"));

doc.getDocumentElement().normalize();

NodeList students = doc.getElementsByTagName("student");

for (int i = 0; i < students.getLength(); i++) {
    Node node = students.item(i);
    if (node.getNodeType() == Node.ELEMENT_NODE) {
        Element element = (Element) node;

        String id   = element.getAttribute("id");
        String name = element.getElementsByTagName("name").item(0).getTextContent();
        String gpa  = element.getElementsByTagName("gpa").item(0).getTextContent();

        System.out.printf("ID: %s, Имя: %s, GPA: %s%n", id, name, gpa);
    }
}
```

---

## 📌 Слайд 3: XML — запись через DOM

```java
// ── Создание XML ──────────────────────────────────────────
DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
DocumentBuilder builder = factory.newDocumentBuilder();
Document doc = builder.newDocument();

// Корневой элемент
Element root = doc.createElement("students");
doc.appendChild(root);

// Добавление студента
String[] names = {"Иван", "Мария"};
double[] gpas  = {8.5, 9.2};

for (int i = 0; i < names.length; i++) {
    Element student = doc.createElement("student");
    student.setAttribute("id", String.valueOf(i + 1));

    Element name = doc.createElement("name");
    name.setTextContent(names[i]);
    student.appendChild(name);

    Element gpa = doc.createElement("gpa");
    gpa.setTextContent(String.valueOf(gpas[i]));
    student.appendChild(gpa);

    root.appendChild(student);
}

// ── Запись в файл ─────────────────────────────────────────
TransformerFactory tf = TransformerFactory.newInstance();
Transformer transformer = tf.newTransformer();
transformer.setOutputProperty(OutputKeys.INDENT, "yes");
transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

transformer.transform(
    new DOMSource(doc),
    new StreamResult(new File("output.xml")));
```

---

## 📌 Слайд 4: Jackson — сериализация Java ↔ JSON

```xml
<!-- pom.xml -->
<dependency>
    <groupId>com.fasterxml.jackson.core</groupId>
    <artifactId>jackson-databind</artifactId>
    <version>2.16.1</version>
</dependency>
```

```java
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.annotation.*;

// ── Модель ────────────────────────────────────────────────
public class Student {
    private Long   id;
    private String name;
    private int    age;
    private double gpa;
    private List<String> courses;

    // @JsonIgnore — не включать в JSON
    @JsonIgnore
    private String password;

    // @JsonProperty — переименовать поле в JSON
    @JsonProperty("full_name")
    private String fullName;

    // @JsonFormat — формат даты
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate birthDate;

    // Геттеры и сеттеры обязательны!
}

// ── ObjectMapper — главный инструмент Jackson ─────────────
ObjectMapper mapper = new ObjectMapper();
mapper.findAndRegisterModules(); // для java.time

// ── Java → JSON (сериализация) ────────────────────────────
Student student = new Student();
student.setName("Иван");
student.setAge(20);
student.setGpa(8.5);
student.setCourses(Arrays.asList("Java", "Math"));

// В строку
String json = mapper.writeValueAsString(student);
System.out.println(json);
// {"id":null,"name":"Иван","age":20,"gpa":8.5,"courses":["Java","Math"]}

// Красивый вывод
String prettyJson = mapper.writerWithDefaultPrettyPrinter()
    .writeValueAsString(student);

// В файл
mapper.writeValue(new File("student.json"), student);
```

---

## 📌 Слайд 5: Jackson — десериализация

```java
// ── JSON → Java (десериализация) ──────────────────────────
String json = """
    {
        "id": 1,
        "name": "Мария",
        "age": 21,
        "gpa": 9.2,
        "courses": ["Java", "Math", "Physics"]
    }
    """;

// Один объект
Student student = mapper.readValue(json, Student.class);
System.out.println(student.getName()); // Мария

// Из файла
Student fromFile = mapper.readValue(new File("student.json"), Student.class);

// Список объектов
String jsonArray = """
    [
        {"name": "Иван", "age": 20},
        {"name": "Мария", "age": 21}
    ]
    """;

List<Student> students = mapper.readValue(jsonArray,
    mapper.getTypeFactory().constructCollectionType(List.class, Student.class));

// Или через TypeReference
List<Student> students2 = mapper.readValue(jsonArray,
    new TypeReference<List<Student>>() {});

System.out.println(students.size()); // 2

// ── Map из JSON ───────────────────────────────────────────
String jsonObj = "{\"key\": \"value\", \"num\": 42}";
Map<String, Object> map = mapper.readValue(jsonObj, new TypeReference<>() {});
System.out.println(map.get("key")); // value
System.out.println(map.get("num")); // 42
```

---

## 📌 Слайд 6: Jackson — конфигурация

```java
ObjectMapper mapper = new ObjectMapper();

// ── Полезные настройки ────────────────────────────────────
// Игнорировать неизвестные поля (не падать если в JSON есть лишнее)
mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

// null поля не включать в JSON
mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

// Поддержка java.time
mapper.registerModule(new JavaTimeModule());
mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

// ── Паттерн Builder через @JsonDeserialize ────────────────
@JsonDeserialize(builder = User.Builder.class)
public class User {
    private final String name;
    private final String email;

    private User(Builder b) {
        this.name  = b.name;
        this.email = b.email;
    }

    @JsonPOJOBuilder(withPrefix = "")
    public static class Builder {
        private String name;
        private String email;

        public Builder name(String name) { this.name = name; return this; }
        public Builder email(String e)   { this.email = e;   return this; }
        public User build() { return new User(this); }
    }
}
```

---

## 📌 Слайд 7: Gson — альтернатива Jackson

```xml
<dependency>
    <groupId>com.google.code.gson</groupId>
    <artifactId>gson</artifactId>
    <version>2.10.1</version>
</dependency>
```

```java
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

// ── Создание Gson ─────────────────────────────────────────
Gson gson = new Gson();

// Красивый вывод
Gson prettyGson = new GsonBuilder()
    .setPrettyPrinting()
    .setDateFormat("dd.MM.yyyy")
    .excludeFieldsWithModifiers(Modifier.TRANSIENT) // исключить transient
    .create();

// ── Сериализация ──────────────────────────────────────────
Student student = new Student("Иван", 20, 8.5);
String json = gson.toJson(student);
System.out.println(json);

// В файл
try (Writer writer = new FileWriter("student.json")) {
    prettyGson.toJson(student, writer);
}

// ── Десериализация ────────────────────────────────────────
Student loaded = gson.fromJson(json, Student.class);

// Список
String jsonList = "[{\"name\":\"Иван\"},{\"name\":\"Мария\"}]";
Type listType = new TypeToken<List<Student>>(){}.getType();
List<Student> students = gson.fromJson(jsonList, listType);

// Из файла
try (Reader reader = new FileReader("student.json")) {
    Student fromFile = gson.fromJson(reader, Student.class);
}

// ── Аннотации Gson ────────────────────────────────────────
public class Product {
    @SerializedName("product_name")  // имя в JSON
    private String name;

    @Expose(serialize = true, deserialize = false) // только для записи
    private String internalCode;
}
```

---

## 📌 Слайд 8: Практический пример — конфиг приложения

```java
// ── config.json ───────────────────────────────────────────
/*
{
    "database": {
        "url": "jdbc:postgresql://localhost:5432/app",
        "username": "admin",
        "maxPoolSize": 10
    },
    "server": {
        "port": 8080,
        "host": "localhost"
    },
    "features": {
        "enableCache": true,
        "cacheExpireMinutes": 30
    }
}
*/

// ── Модели конфига ────────────────────────────────────────
public class AppConfig {
    private DatabaseConfig database;
    private ServerConfig   server;
    private FeaturesConfig features;
    // геттеры
}

public class DatabaseConfig {
    private String url;
    private String username;
    private int    maxPoolSize;
    // геттеры
}

// ── Загрузка конфига ──────────────────────────────────────
public class ConfigLoader {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static AppConfig load(String path) throws IOException {
        return MAPPER.readValue(new File(path), AppConfig.class);
    }

    public static void save(AppConfig config, String path) throws IOException {
        MAPPER.writerWithDefaultPrettyPrinter()
              .writeValue(new File(path), config);
    }
}

AppConfig config = ConfigLoader.load("config.json");
System.out.println(config.getDatabase().getUrl());
System.out.println(config.getServer().getPort());
```

---

## 📌 Слайд 9: Итоги

✅ **XML** — мощный, с атрибутами и пространствами имён. Используется в Maven, Spring XML.

✅ **JSON** — компактный, читаемый, стандарт для REST API.

✅ **Jackson** — стандарт де-факто для Java. `ObjectMapper` — главный класс.

✅ `@JsonIgnore`, `@JsonProperty`, `@JsonFormat` — управление сериализацией.

✅ **Gson** — альтернатива, проще в конфигурации для простых случаев.

✅ Используй `TypeReference<List<T>>` для десериализации в параметризованные типы.

---

## 📌 Слайд 10: Домашнее задание

```java
// 1. Создать систему хранения данных в JSON:
// - ProductCatalog: List<Product>
// - Сохранить каталог в файл catalog.json
// - Загрузить и найти все продукты дешевле 1000 руб.
// - Сохранить отфильтрованный список в filtered.json

// 2. XML конфиг:
// - Создать settings.xml с настройками приложения
// - Загрузить через DOM
// - Изменить значение и сохранить обратно

// Бонус: написать универсальный JsonRepository<T>:
// - void save(List<T> items, Class<T> type, Path file)
// - List<T> load(Class<T> type, Path file)
```

---

## ❓ Вопросы для самопроверки

1. В чём основное отличие SAX от DOM парсинга?
2. Что делает `@JsonIgnore`?
3. Как десериализовать `List<Student>` через Jackson?
4. Что такое `TypeReference` и зачем он нужен?
5. Почему нельзя просто написать `mapper.readValue(json, List.class)` для `List<Student>`?

---

*Лекция 16 из 19 | Курс: Введение в ООП на Java | Семестр 2*

