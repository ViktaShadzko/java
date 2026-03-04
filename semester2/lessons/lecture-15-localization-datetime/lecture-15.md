# Лекция 15: Локализация, интернационализация, Properties, DateTime

---

## 🗂️ План лекции

1. Локализация (L10n) и интернационализация (I18n)
2. Locale — представление региона
3. ResourceBundle и .properties файлы
4. Форматирование чисел и валют
5. Java Date/Time API (java.time, Java 8+)
6. Форматирование дат
7. Работа с часовыми поясами
8. Итоги и домашнее задание

---

## 📌 Слайд 1: I18n и L10n

> **Интернационализация (I18n)** — проектирование приложения так, чтобы его было легко адаптировать к разным языкам и регионам.
>
> **Локализация (L10n)** — адаптация приложения к конкретному языку/региону.

### Что нужно локализовать:
- Текстовые строки (сообщения, метки)
- Форматы дат: `04.03.2026` (RU) vs `3/4/2026` (US) vs `2026-03-04` (ISO)
- Числа и валюты: `1.234,56 ₽` (RU) vs `$1,234.56` (US)
- Направление текста: LTR (English) vs RTL (Arabic, Hebrew)

---

## 📌 Слайд 2: Locale

```java
import java.util.Locale;

// ── Создание Locale ───────────────────────────────────────
Locale ru    = new Locale("ru", "RU");
Locale us    = Locale.US;
Locale uk    = Locale.UK;
Locale de    = Locale.GERMANY;
Locale cn    = Locale.CHINA;

// Через Builder (Java 7+)
Locale custom = new Locale.Builder()
    .setLanguage("ru")
    .setRegion("RU")
    .build();

// ── Информация о Locale ───────────────────────────────────
System.out.println(ru.getLanguage());       // ru
System.out.println(ru.getCountry());        // RU
System.out.println(ru.getDisplayName());    // русский (Россия)
System.out.println(ru.getDisplayName(Locale.ENGLISH)); // Russian (Russia)

// ── Текущая Locale ────────────────────────────────────────
Locale defaultLocale = Locale.getDefault();
System.out.println(defaultLocale); // зависит от системы

// Все доступные Locale
for (Locale l : Locale.getAvailableLocales()) {
    System.out.println(l.toLanguageTag()); // ru-RU, en-US, de-DE ...
}
```

---

## 📌 Слайд 3: ResourceBundle и .properties

```
Структура файлов локализации:
src/main/resources/
├── messages.properties          ← файл по умолчанию (английский)
├── messages_ru.properties       ← русская локаль
├── messages_de.properties       ← немецкая
└── messages_fr.properties       ← французская
```

```properties
# messages.properties (default/English)
app.title=Library Management System
greeting=Hello, {0}!
books.found=Found {0} books
error.notfound=Book not found: {0}
button.save=Save
button.cancel=Cancel
```

```properties
# messages_ru.properties
app.title=Система управления библиотекой
greeting=Привет, {0}!
books.found=Найдено книг: {0}
error.notfound=Книга не найдена: {0}
button.save=Сохранить
button.cancel=Отмена
```

```java
import java.util.*;
import java.text.*;

// ── Загрузка ResourceBundle ───────────────────────────────
ResourceBundle bundle = ResourceBundle.getBundle("messages", new Locale("ru", "RU"));

System.out.println(bundle.getString("app.title"));   // Система управления библиотекой
System.out.println(bundle.getString("button.save")); // Сохранить

// ── Параметры через MessageFormat ────────────────────────
String template = bundle.getString("greeting"); // "Привет, {0}!"
String message  = MessageFormat.format(template, "Иван");
System.out.println(message); // Привет, Иван!

String booksMsg = MessageFormat.format(
    bundle.getString("books.found"), 42);
System.out.println(booksMsg); // Найдено книг: 42

// ── Переключение локали ───────────────────────────────────
public class I18nService {
    private Locale currentLocale;
    private ResourceBundle bundle;

    public I18nService(Locale locale) {
        setLocale(locale);
    }

    public void setLocale(Locale locale) {
        this.currentLocale = locale;
        this.bundle = ResourceBundle.getBundle("messages", locale);
    }

    public String get(String key, Object... args) {
        String template = bundle.getString(key);
        return args.length == 0 ? template : MessageFormat.format(template, args);
    }
}

I18nService i18n = new I18nService(new Locale("ru"));
System.out.println(i18n.get("greeting", "Мария")); // Привет, Мария!

i18n.setLocale(Locale.ENGLISH);
System.out.println(i18n.get("greeting", "Maria")); // Hello, Maria!
```

---

## 📌 Слайд 4: Форматирование чисел и валют

```java
import java.text.*;

Locale ru = new Locale("ru", "RU");
Locale us = Locale.US;
Locale de = Locale.GERMANY;

double price = 1234567.89;

// ── Числа ─────────────────────────────────────────────────
NumberFormat nfRu = NumberFormat.getNumberInstance(ru);
NumberFormat nfUs = NumberFormat.getNumberInstance(us);
NumberFormat nfDe = NumberFormat.getNumberInstance(de);

System.out.println(nfRu.format(price)); // 1 234 567,89
System.out.println(nfUs.format(price)); // 1,234,567.89
System.out.println(nfDe.format(price)); // 1.234.567,89

// ── Валюты ────────────────────────────────────────────────
NumberFormat cfRu = NumberFormat.getCurrencyInstance(ru);
NumberFormat cfUs = NumberFormat.getCurrencyInstance(us);
NumberFormat cfDe = NumberFormat.getCurrencyInstance(de);

System.out.println(cfRu.format(price)); // 1 234 567,89 ₽
System.out.println(cfUs.format(price)); // $1,234,567.89
System.out.println(cfDe.format(price)); // 1.234.567,89 €

// ── Проценты ──────────────────────────────────────────────
NumberFormat pf = NumberFormat.getPercentInstance(ru);
pf.setMaximumFractionDigits(1);
System.out.println(pf.format(0.756)); // 75,6%

// ── Парсинг строк ─────────────────────────────────────────
Number parsed = NumberFormat.getNumberInstance(ru).parse("1 234,56");
System.out.println(parsed.doubleValue()); // 1234.56
```

---

## 📌 Слайд 5: Java Date/Time API (java.time)

> До Java 8: `java.util.Date` и `Calendar` — запутанные и thread-unsafe.
> Java 8+: `java.time` — чистый, иммутабельный, понятный.

```java
import java.time.*;
import java.time.temporal.*;

// ── LocalDate — дата без времени ──────────────────────────
LocalDate today     = LocalDate.now();
LocalDate birthday  = LocalDate.of(2000, 3, 15);
LocalDate tomorrow  = today.plusDays(1);
LocalDate nextMonth = today.plusMonths(1);
LocalDate nextYear  = today.plusYears(1);

System.out.println(today);        // 2026-03-04
System.out.println(birthday);     // 2000-03-15
System.out.println(tomorrow);     // 2026-03-05

// Информация о дате
System.out.println(today.getDayOfWeek());   // WEDNESDAY
System.out.println(today.getMonth());        // MARCH
System.out.println(today.getDayOfYear());    // 63

// Сравнение
System.out.println(today.isAfter(birthday));   // true
System.out.println(today.isBefore(nextYear));  // true
System.out.println(today.isEqual(LocalDate.now())); // true

// ── LocalTime — время без даты ────────────────────────────
LocalTime now      = LocalTime.now();
LocalTime start    = LocalTime.of(9, 0);
LocalTime end      = LocalTime.of(17, 30, 15);
LocalTime midnight = LocalTime.MIDNIGHT;
LocalTime noon     = LocalTime.NOON;

System.out.println(now);   // 14:35:22.123456789
System.out.println(start); // 09:00

// ── LocalDateTime — дата и время ─────────────────────────
LocalDateTime dt = LocalDateTime.now();
LocalDateTime meeting = LocalDateTime.of(2026, 3, 10, 14, 30);
LocalDateTime inTwoHours = dt.plusHours(2);

System.out.println(meeting); // 2026-03-10T14:30
```

---

## 📌 Слайд 6: Period, Duration, ChronoUnit

```java
// ── Period — разница в датах (годы, месяцы, дни) ─────────
LocalDate start = LocalDate.of(2000, 3, 15);
LocalDate end   = LocalDate.of(2026, 3, 4);

Period age = Period.between(start, end);
System.out.printf("Возраст: %d лет %d месяцев %d дней%n",
    age.getYears(), age.getMonths(), age.getDays());
// Возраст: 25 лет 11 месяцев 17 дней

// ── Duration — разница во времени (часы, минуты, секунды) ─
LocalDateTime ldt1 = LocalDateTime.of(2026, 3, 4, 9, 0);
LocalDateTime ldt2 = LocalDateTime.of(2026, 3, 4, 17, 30);

Duration work = Duration.between(ldt1, ldt2);
System.out.println(work.toHours());   // 8
System.out.println(work.toMinutes()); // 510

// ── ChronoUnit — удобный подсчёт ─────────────────────────
long daysUntilNewYear = ChronoUnit.DAYS.between(
    LocalDate.now(), LocalDate.of(2027, 1, 1));
System.out.println("До Нового года: " + daysUntilNewYear + " дней");

long minutesWorked = ChronoUnit.MINUTES.between(ldt1, ldt2);
System.out.println("Отработано минут: " + minutesWorked); // 510
```

---

## 📌 Слайд 7: Форматирование и парсинг дат

```java
import java.time.format.*;

// ── DateTimeFormatter ─────────────────────────────────────
DateTimeFormatter isoDate   = DateTimeFormatter.ISO_LOCAL_DATE;
DateTimeFormatter isoDateTime = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

// Встроенные форматы
System.out.println(LocalDate.now().format(isoDate)); // 2026-03-04

// Свой формат
DateTimeFormatter ruDate = DateTimeFormatter.ofPattern("dd.MM.yyyy");
DateTimeFormatter ruDateTime = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
DateTimeFormatter withDay = DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", new Locale("ru"));

LocalDate date = LocalDate.of(2026, 3, 4);
System.out.println(date.format(ruDate));     // 04.03.2026
System.out.println(date.format(withDay));    // среда, 04 марта 2026

LocalDateTime dateTime = LocalDateTime.of(2026, 3, 4, 14, 30);
System.out.println(dateTime.format(ruDateTime)); // 04.03.2026 14:30

// ── Парсинг строк ─────────────────────────────────────────
LocalDate parsed = LocalDate.parse("04.03.2026", ruDate);
System.out.println(parsed); // 2026-03-04

LocalDate iso = LocalDate.parse("2026-03-04"); // ISO по умолчанию
```

---

## 📌 Слайд 8: Часовые пояса

```java
import java.time.*;

// ── ZonedDateTime — дата/время с часовым поясом ───────────
ZoneId moscow   = ZoneId.of("Europe/Moscow");
ZoneId newYork  = ZoneId.of("America/New_York");
ZoneId tokyo    = ZoneId.of("Asia/Tokyo");

ZonedDateTime nowMoscow = ZonedDateTime.now(moscow);
System.out.println(nowMoscow); // 2026-03-04T17:35:22+03:00[Europe/Moscow]

// Конвертация между часовыми поясами
ZonedDateTime nowNewYork = nowMoscow.withZoneSameInstant(newYork);
ZonedDateTime nowTokyo   = nowMoscow.withZoneSameInstant(tokyo);

System.out.println("Москва:   " + nowMoscow.toLocalTime());
System.out.println("Нью-Йорк: " + nowNewYork.toLocalTime());
System.out.println("Токио:    " + nowTokyo.toLocalTime());

// ── Instant — "момент времени" (UTC) ─────────────────────
Instant now = Instant.now();
System.out.println(now); // 2026-03-04T14:35:22.123Z  (UTC)

// Конвертация Instant ↔ ZonedDateTime
ZonedDateTime zdt = now.atZone(moscow);
Instant back = zdt.toInstant();

// ── Сдвиг (Offset) ────────────────────────────────────────
OffsetDateTime offsetDt = OffsetDateTime.now(ZoneOffset.ofHours(3));
System.out.println(offsetDt); // 2026-03-04T17:35:22+03:00
```

---

## 📌 Слайд 9: Практический пример

```java
public class ScheduleService {
    private static final DateTimeFormatter DISPLAY_FMT =
        DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private static final ZoneId DEFAULT_ZONE = ZoneId.of("Europe/Moscow");

    public String formatEventTime(Instant eventTime) {
        ZonedDateTime zdt = eventTime.atZone(DEFAULT_ZONE);
        return zdt.format(DISPLAY_FMT);
    }

    public boolean isUpcoming(Instant eventTime) {
        return eventTime.isAfter(Instant.now());
    }

    public String timeUntilEvent(Instant eventTime) {
        Duration duration = Duration.between(Instant.now(), eventTime);
        if (duration.isNegative()) return "Событие прошло";

        long days    = duration.toDays();
        long hours   = duration.toHoursPart();   // Java 9+
        long minutes = duration.toMinutesPart();

        if (days > 0) return String.format("через %d д. %d ч.", days, hours);
        if (hours > 0) return String.format("через %d ч. %d мин.", hours, minutes);
        return String.format("через %d мин.", minutes);
    }
}
```

---

## 📌 Слайд 10: Итоги

✅ **Locale** — язык + регион. Используй `Locale.US`, `new Locale("ru", "RU")`.

✅ **ResourceBundle** — внешние строки в `.properties` файлах по локали.

✅ **NumberFormat / MessageFormat** — форматирование чисел, валют, сообщений.

✅ **java.time** — `LocalDate`, `LocalTime`, `LocalDateTime` — иммутабельны, thread-safe.

✅ **Period** — разница в датах. **Duration** — разница во времени.

✅ **ZonedDateTime / Instant** — для работы с часовыми поясами.

✅ **DateTimeFormatter** — форматирование и парсинг дат.

---

## 📌 Слайд 11: Домашнее задание

```java
// 1. Создать файлы локализации для EN и RU:
// Приложение "Трекер задач":
// - task.status.todo, task.status.done, task.status.inprogress
// - task.created, task.deadline, task.priority.high/medium/low

// 2. Работа с датами:
// - Найти все задачи с дедлайном в ближайшие 7 дней
// - Вычислить "просроченные" задачи (deadline < now)
// - Отсортировать задачи по дедлайну
// - Вывести дедлайн в форматах: "dd.MM.yyyy" и "EEEE, dd MMMM yyyy"

// 3. Часовые пояса:
// Создать EventScheduler — хранит события с ZonedDateTime
// Метод: getEventsInTimezone(ZoneId) — конвертирует время всех событий
```

---

## ❓ Вопросы для самопроверки

1. В чём разница между `LocalDate` и `ZonedDateTime`?
2. Что такое `Instant`?
3. Чем `Period` отличается от `Duration`?
4. Как задать формат даты `04.03.2026 17:30`?
5. Почему `java.util.Date` не рекомендуется использовать?

---

*Лекция 15 из 19 | Курс: Введение в ООП на Java | Семестр 2*

