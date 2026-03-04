# Лекция 17: Рефлексия и аннотации

---

## 🗂️ План лекции

1. Рефлексия — что это и зачем?
2. Class объект
3. Поля, методы, конструкторы через рефлексию
4. Создание объектов через рефлексию
5. Аннотации — создание и обработка
6. Retention и Target
7. Практические примеры (DI, валидация)
8. Итоги и домашнее задание

---

## 📌 Слайд 1: Что такое рефлексия?

> **Рефлексия** — возможность программы **изучать и изменять** свою собственную структуру во время выполнения.

### Зачем нужна:
- Фреймворки (Spring, Hibernate) — создание объектов по конфигурации
- Тестирование (JUnit) — поиск тест-методов
- Сериализация (Jackson) — чтение полей объекта
- IDE — автодополнение кода

```java
// ── Без рефлексии — всё известно на этапе компиляции ─────
Student s = new Student("Иван", 20);
System.out.println(s.getName()); // знаем тип, знаем метод

// ── С рефлексией — тип определяется в рантайме ───────────
Object obj = createFromConfig("com.example.Student");
Class<?> cls = obj.getClass();
Method method = cls.getMethod("getName");
Object result = method.invoke(obj); // вызываем метод, не зная тип!
System.out.println(result); // Иван
```

---

## 📌 Слайд 2: Объект Class

```java
// ── Получить Class объект ─────────────────────────────────
Class<String>  c1 = String.class;           // через .class
Class<?>       c2 = "hello".getClass();     // через объект
Class<?>       c3 = Class.forName("java.lang.String"); // через строку (throws)

// Всё это один и тот же объект
System.out.println(c1 == c2); // true
System.out.println(c1 == c3); // true

// ── Информация о классе ───────────────────────────────────
Class<?> cls = Student.class;

System.out.println(cls.getName());           // com.example.Student
System.out.println(cls.getSimpleName());     // Student
System.out.println(cls.getPackageName());    // com.example
System.out.println(cls.getSuperclass());     // class java.lang.Object
System.out.println(Arrays.toString(cls.getInterfaces())); // [interface java.lang.Comparable]

// Модификаторы
int mods = cls.getModifiers();
System.out.println(Modifier.isPublic(mods));   // true
System.out.println(Modifier.isFinal(mods));    // false
System.out.println(Modifier.isAbstract(mods)); // false

// Является ли интерфейсом, перечислением, примитивом
System.out.println(cls.isInterface()); // false
System.out.println(cls.isEnum());      // false
System.out.println(int.class.isPrimitive()); // true
```

---

## 📌 Слайд 3: Поля через рефлексию

```java
public class Person {
    public  String name;
    private int    age;
    private String password;
}

Class<?> cls = Person.class;

// ── Получить поля ─────────────────────────────────────────
Field[] publicFields = cls.getFields();         // только public (включая наследованные)
Field[] allFields    = cls.getDeclaredFields(); // все поля этого класса

for (Field f : allFields) {
    System.out.printf("%-10s %-10s %s%n",
        Modifier.toString(f.getModifiers()),
        f.getType().getSimpleName(),
        f.getName());
}
// public     String     name
// private    int        age
// private    String     password

// ── Чтение и запись поля ─────────────────────────────────
Person p = new Person();
p.name = "Иван";

Field nameField = cls.getField("name");
System.out.println(nameField.get(p)); // Иван

Field ageField = cls.getDeclaredField("age");
ageField.setAccessible(true); // открыть private доступ!
ageField.set(p, 25);
System.out.println(ageField.get(p)); // 25

// ── Перебор всех полей объекта ────────────────────────────
public static Map<String, Object> toMap(Object obj) throws Exception {
    Map<String, Object> map = new LinkedHashMap<>();
    for (Field f : obj.getClass().getDeclaredFields()) {
        f.setAccessible(true);
        map.put(f.getName(), f.get(obj));
    }
    return map;
}

System.out.println(toMap(p)); // {name=Иван, age=25, password=null}
```

---

## 📌 Слайд 4: Методы и конструкторы

```java
Class<?> cls = Student.class;

// ── Методы ────────────────────────────────────────────────
Method[] methods = cls.getDeclaredMethods();
for (Method m : methods) {
    System.out.printf("%s %s(%s)%n",
        m.getReturnType().getSimpleName(),
        m.getName(),
        Arrays.stream(m.getParameterTypes())
              .map(Class::getSimpleName)
              .collect(Collectors.joining(", ")));
}

// Вызов метода
Student s = new Student("Иван", 20, 8.5);
Method getNameMethod = cls.getMethod("getName"); // public метод
String name = (String) getNameMethod.invoke(s);  // вызов без аргументов
System.out.println(name); // Иван

Method setGpa = cls.getMethod("setGpa", double.class);
setGpa.invoke(s, 9.0); // вызов с аргументом

// Private метод
Method privateMethod = cls.getDeclaredMethod("calculateBonus");
privateMethod.setAccessible(true);
Object result = privateMethod.invoke(s);

// ── Конструкторы ─────────────────────────────────────────
Constructor<?>[] constructors = cls.getConstructors();
for (Constructor<?> c : constructors) {
    System.out.println(Arrays.toString(c.getParameterTypes()));
}

// Создать объект через рефлексию
Constructor<Student> ctor = Student.class.getConstructor(String.class, int.class, double.class);
Student newStudent = ctor.newInstance("Мария", 21, 9.2);
System.out.println(newStudent.getName()); // Мария

// Без аргументов (если есть конструктор по умолчанию)
Object obj = cls.getDeclaredConstructor().newInstance();
```

---

## 📌 Слайд 5: Создание аннотаций

```java
import java.lang.annotation.*;

// ── @Retention — как долго живёт аннотация ───────────────
// SOURCE   — только в исходном коде, удаляется компилятором
// CLASS    — в .class файле, но недоступна в рантайме
// RUNTIME  — доступна в рантайме через рефлексию ← нам нужна!
@Retention(RetentionPolicy.RUNTIME)

// ── @Target — где можно применять ────────────────────────
@Target({ElementType.FIELD, ElementType.METHOD})

// ── Объявление аннотации ──────────────────────────────────
public @interface Validate {
    // Элементы аннотации (с дефолтными значениями)
    boolean notNull()  default false;
    int     minLength() default 0;
    int     maxLength() default Integer.MAX_VALUE;
    String  pattern()  default "";         // regex
    String  message()  default "Ошибка валидации";
}

// ── Другие примеры аннотаций ──────────────────────────────
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Entity {
    String tableName() default "";
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {
    String name()       default "";
    boolean nullable()  default true;
    int     length()    default 255;
}

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cacheable {
    int expireAfterSeconds() default 300;
    String key()             default "";
}
```

---

## 📌 Слайд 6: Обработка аннотаций через рефлексию

```java
// ── Применение аннотаций ──────────────────────────────────
public class UserDto {
    @Validate(notNull = true, minLength = 2, maxLength = 50, message = "Имя: 2-50 символов")
    private String name;

    @Validate(notNull = true, pattern = "^[^@]+@[^@]+\\.[^@]+$", message = "Некорректный email")
    private String email;

    @Validate(minLength = 8, message = "Пароль: минимум 8 символов")
    private String password;

    private int age; // без аннотации — не валидируется

    // конструктор, геттеры, сеттеры
}

// ── Валидатор — читает аннотации через рефлексию ──────────
public class Validator {
    public List<String> validate(Object obj) throws IllegalAccessException {
        List<String> errors = new ArrayList<>();
        Class<?> cls = obj.getClass();

        for (Field field : cls.getDeclaredFields()) {
            if (!field.isAnnotationPresent(Validate.class)) continue;

            field.setAccessible(true);
            Object value = field.get(obj);
            Validate v = field.getAnnotation(Validate.class);

            // Проверка notNull
            if (v.notNull() && value == null) {
                errors.add(field.getName() + ": " + v.message());
                continue;
            }

            if (value instanceof String s) {
                // Проверка minLength
                if (s.length() < v.minLength()) {
                    errors.add(field.getName() + ": " + v.message());
                    continue;
                }
                // Проверка maxLength
                if (s.length() > v.maxLength()) {
                    errors.add(field.getName() + ": " + v.message());
                    continue;
                }
                // Проверка pattern
                if (!v.pattern().isEmpty() && !s.matches(v.pattern())) {
                    errors.add(field.getName() + ": " + v.message());
                }
            }
        }
        return errors;
    }
}

// ── Использование ─────────────────────────────────────────
UserDto user = new UserDto("И", "not-an-email", "123");
Validator validator = new Validator();
List<String> errors = validator.validate(user);

if (!errors.isEmpty()) {
    System.out.println("Ошибки валидации:");
    errors.forEach(e -> System.out.println("  - " + e));
}
// Ошибки валидации:
//   - name: Имя: 2-50 символов
//   - email: Некорректный email
//   - password: Пароль: минимум 8 символов
```

---

## 📌 Слайд 7: Мини-DI контейнер через рефлексию

```java
// ── Аннотации ─────────────────────────────────────────────
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component { }

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.CONSTRUCTOR})
public @interface Inject { }

// ── Компоненты ────────────────────────────────────────────
@Component
public class UserRepository {
    public String findById(Long id) { return "User#" + id; }
}

@Component
public class UserService {
    @Inject
    private UserRepository repository; // ← рефлексия внедрит сюда!

    public String getUser(Long id) { return repository.findById(id); }
}

// ── Простой DI контейнер ──────────────────────────────────
public class Container {
    private final Map<Class<?>, Object> beans = new HashMap<>();

    public void register(Class<?>... classes) throws Exception {
        // Создать все объекты
        for (Class<?> cls : classes) {
            beans.put(cls, cls.getDeclaredConstructor().newInstance());
        }
        // Внедрить зависимости
        for (Object bean : beans.values()) {
            for (Field field : bean.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Inject.class)) {
                    field.setAccessible(true);
                    Object dep = beans.get(field.getType());
                    if (dep != null) field.set(bean, dep);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> cls) {
        return (T) beans.get(cls);
    }
}

// ── Использование ─────────────────────────────────────────
Container container = new Container();
container.register(UserRepository.class, UserService.class);

UserService service = container.get(UserService.class);
System.out.println(service.getUser(1L)); // User#1
```

---

## 📌 Слайд 8: Итоги

✅ **Рефлексия** — изучение и изменение структуры программы в рантайме.

✅ `Class.forName()`, `.getFields()`, `.getMethods()`, `.getConstructors()`.

✅ `setAccessible(true)` — открывает доступ к `private` членам.

✅ **Аннотации** — метаданные для классов, полей, методов.

✅ `@Retention(RUNTIME)` — аннотация доступна через рефлексию.

✅ `@Target` — где можно применять аннотацию.

✅ Рефлексия — основа Spring, Hibernate, JUnit, Jackson.

---

## 📌 Слайд 9: Домашнее задание

```java
// 1. Аннотация @Table и @Column:
// Создать простой ORM:
// @Table("users")
// class User {
//     @Column("user_id")  Long id;
//     @Column("user_name") String name;
// }
// generateCreateTableSql(Class<?> entity) → "CREATE TABLE users (user_id ..., user_name ...)"
// generateInsertSql(Object entity)        → "INSERT INTO users VALUES (...)"

// 2. Аннотация @Benchmark:
// Метод с @Benchmark замеряет время выполнения и выводит в лог
// Реализовать через рефлексию-proxy (или просто через вызов)

// 3. @Required:
// Аннотация поля — выбрасывает исключение если поле null после конструктора
// Реализовать как post-construct проверку
```

---

## ❓ Вопросы для самопроверки

1. Зачем нужен `setAccessible(true)`?
2. Чем `getFields()` отличается от `getDeclaredFields()`?
3. Что означает `@Retention(RetentionPolicy.RUNTIME)`?
4. Как получить значение аннотации поля через рефлексию?
5. Назови три фреймворка которые активно используют рефлексию.

---

*Лекция 17 из 19 | Курс: Введение в ООП на Java | Семестр 2*

