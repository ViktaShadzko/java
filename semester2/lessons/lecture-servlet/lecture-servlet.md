# Лекция: Java Servlets — Основы веб-программирования на Java

---

## 🗂️ План лекции

1. Что такое веб-приложение?
2. Протокол HTTP — основы
3. Что такое Servlet?
4. Жизненный цикл сервлета
5. `HttpServlet` — GET и POST
6. `web.xml` и аннотации `@WebServlet`
7. `ServletRequest` и `ServletResponse`
8. `HttpSession` — сессии пользователя
9. `Cookie` — куки
10. `Filter` — фильтры запросов
11. `JSP` — Java Server Pages (обзор)
12. Развёртывание в Tomcat
13. Итоги и домашнее задание

---

## 📌 Слайд 1: Что такое веб-приложение?

### Как работает интернет (упрощённо):

```
Браузер  ──────── HTTP запрос ──────────►  Сервер
         ◄─────── HTTP ответ (HTML) ──────  Сервер
```

### Компоненты:
| Сторона | Название | Технологии |
|---------|----------|-----------|
| Клиент  | Frontend | HTML, CSS, JavaScript |
| Сервер  | Backend  | Java, Python, Node.js ... |

### Java на сервере:
> Java поддерживает серверное программирование через технологию **Servlet API** — стандарт, появившийся в 1997 году.

---

## 📌 Слайд 2: Протокол HTTP

> **HTTP (HyperText Transfer Protocol)** — протокол передачи данных между браузером и сервером.

### Структура HTTP-запроса:
```
GET /users/5 HTTP/1.1
Host: example.com
Accept: text/html
```

### Структура HTTP-ответа:
```
HTTP/1.1 200 OK
Content-Type: text/html

<html>...</html>
```

### Основные HTTP-методы:
| Метод  | Назначение |
|--------|-----------|
| GET    | Получить данные |
| POST   | Отправить данные (создать) |
| PUT    | Обновить данные полностью |
| PATCH  | Обновить данные частично |
| DELETE | Удалить данные |

### Коды ответа:
| Код | Значение |
|-----|---------|
| 200 | OK — успешно |
| 201 | Created — создано |
| 301 | Redirect — перенаправление |
| 400 | Bad Request — ошибка запроса |
| 404 | Not Found — не найдено |
| 500 | Internal Server Error — ошибка сервера |

---

## 📌 Слайд 3: Что такое Servlet?

> **Servlet** — это Java-класс, который обрабатывает HTTP-запросы и формирует HTTP-ответы.

### Место сервлета в системе:

```
Браузер
   │
   │  HTTP Request
   ▼
┌─────────────────────────────────┐
│        Servlet Container        │  ← Tomcat / Jetty / Undertow
│  ┌──────────────────────────┐   │
│  │       DispatcherServlet  │   │
│  │  ┌────────┐ ┌─────────┐  │   │
│  │  │Servlet1│ │Servlet2 │  │   │
│  │  └────────┘ └─────────┘  │   │
│  └──────────────────────────┘   │
└─────────────────────────────────┘
   │
   │  HTTP Response
   ▼
Браузер
```

### Иерархия классов:
```
Servlet (interface)
    └── GenericServlet (abstract)
            └── HttpServlet (abstract)
                    └── Ваш сервлет ✅
```

---

## 📌 Слайд 4: Жизненный цикл сервлета

> Жизненный цикл управляется **Servlet Container** (например, Tomcat).

### Три стадии:

```
1. init()     ──► Инициализация (один раз при запуске)
2. service()  ──► Обработка запросов (каждый запрос)
3. destroy()  ──► Уничтожение (один раз при остановке)
```

### Диаграмма:
```
Контейнер загружает класс
        │
        ▼
   new MyServlet()     ← создание экземпляра
        │
        ▼
    init()             ← инициализация (once)
        │
        ▼
  ┌─ service() ─┐      ← каждый запрос
  │  doGet()    │
  │  doPost()   │
  │  doPut()    │
  └─────────────┘
        │
        ▼
    destroy()          ← завершение (once)
```

### Пример:
```java
@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        System.out.println("Сервлет инициализирован!");
        // Здесь можно открыть соединение с БД, загрузить конфиги
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("<h1>Привет, мир!</h1>");
    }

    @Override
    public void destroy() {
        System.out.println("Сервлет уничтожен!");
        // Здесь закрываем ресурсы
    }
}
```

---

## 📌 Слайд 5: HttpServlet — GET и POST

### doGet — обработка GET-запросов:
```java
@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Читаем параметр из URL: /products?id=5
        String idParam = req.getParameter("id");

        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();

        if (idParam != null) {
            out.println("<h2>Товар #" + idParam + "</h2>");
        } else {
            out.println("<h2>Список всех товаров</h2>");
        }
    }
}
```

### doPost — обработка POST-запросов:
```java
@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        // Читаем данные из тела формы
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if ("admin".equals(username) && "1234".equals(password)) {
            resp.sendRedirect("/dashboard");
        } else {
            resp.sendRedirect("/login?error=true");
        }
    }
}
```

### HTML форма для POST:
```html
<form action="/login" method="POST">
    <input type="text"     name="username" placeholder="Логин">
    <input type="password" name="password" placeholder="Пароль">
    <button type="submit">Войти</button>
</form>
```

---

## 📌 Слайд 6: Регистрация сервлета

### Способ 1 — `web.xml` (старый способ):
```xml
<!-- WEB-INF/web.xml -->
<web-app>
    <servlet>
        <servlet-name>HelloServlet</servlet-name>
        <servlet-class>com.example.HelloServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>HelloServlet</servlet-name>
        <url-pattern>/hello</url-pattern>
    </servlet-mapping>
</web-app>
```

### Способ 2 — аннотация `@WebServlet` (современный):
```java
@WebServlet(
    name = "HelloServlet",
    urlPatterns = {"/hello", "/hi"}
)
public class HelloServlet extends HttpServlet {
    // ...
}
```

### URL-паттерны:
| Паттерн | Что обрабатывает |
|---------|-----------------|
| `/hello` | Точный путь |
| `/api/*` | Всё под `/api/` |
| `*.do`   | Все URL с суффиксом `.do` |
| `/`      | Все запросы (default servlet) |

---

## 📌 Слайд 7: HttpServletRequest — запрос

> `HttpServletRequest` содержит **всю информацию о входящем запросе**.

```java
protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

    // Метод и путь
    String method = req.getMethod();             // "GET"
    String uri    = req.getRequestURI();         // "/products/5"
    String query  = req.getQueryString();        // "sort=price&order=asc"

    // Параметры запроса (?key=value)
    String id     = req.getParameter("id");
    String[] tags = req.getParameterValues("tag"); // несколько значений

    // Заголовки
    String accept   = req.getHeader("Accept");
    String userAgent = req.getHeader("User-Agent");

    // Атрибуты (внутренние данные приложения)
    Object user = req.getAttribute("currentUser");

    // Информация о клиенте
    String ip   = req.getRemoteAddr();
    String host = req.getServerName();
    int port    = req.getServerPort();
}
```

---

## 📌 Слайд 8: HttpServletResponse — ответ

> `HttpServletResponse` используется для **формирования ответа** клиенту.

```java
protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws IOException {

    // Установить статус
    resp.setStatus(200);
    // или
    resp.setStatus(HttpServletResponse.SC_OK);

    // Установить Content-Type
    resp.setContentType("application/json; charset=UTF-8");

    // Установить заголовок
    resp.setHeader("X-Custom-Header", "my-value");

    // Перенаправление
    resp.sendRedirect("/new-page");

    // Ошибка
    resp.sendError(404, "Страница не найдена");

    // Записать тело ответа
    PrintWriter out = resp.getWriter();
    out.println("{\"status\": \"ok\"}");

    // Или бинарные данные (например, картинку)
    OutputStream os = resp.getOutputStream();
    os.write(imageBytes);
}
```

---

## 📌 Слайд 9: HttpSession — сессии

> HTTP — **stateless** протокол. Каждый запрос независим.  
> **Session** позволяет хранить данные пользователя между запросами.

### Как работает сессия:
```
Запрос 1: браузер → сервер
    Сервер создаёт сессию: session_id = "abc123"
    Ответ содержит Cookie: JSESSIONID=abc123

Запрос 2: браузер → сервер
    Браузер автоматически отправляет Cookie: JSESSIONID=abc123
    Сервер находит сессию по id и восстанавливает данные
```

### Работа с сессией:
```java
@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        // Получить или создать сессию
        HttpSession session = req.getSession();
        // req.getSession(false) — только получить (не создавать)

        String productId = req.getParameter("productId");

        // Получить корзину из сессии
        List<String> cart = (List<String>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }

        cart.add(productId);
        System.out.println("ID сессии: " + session.getId());

        // Время жизни (секунды)
        session.setMaxInactiveInterval(30 * 60); // 30 минут

        resp.sendRedirect("/cart");
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        // Уничтожить сессию (выход из системы)
        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        resp.setStatus(200);
    }
}
```

---

## 📌 Слайд 10: Cookie — куки

> **Cookie** — небольшой фрагмент данных, который сервер отправляет браузеру, а браузер хранит и автоматически возвращает при следующих запросах.

### Разница Session vs Cookie:
| | Session | Cookie |
|---|---------|--------|
| Хранение | На сервере | В браузере |
| Размер | Неограничен | ~4 KB |
| Безопасность | Более безопасна | Менее безопасна |
| Время жизни | До закрытия/таймаута | Можно задать явно |

### Работа с куки:
```java
@WebServlet("/theme")
public class ThemeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String theme = req.getParameter("theme"); // "dark" или "light"

        // Создать куки
        Cookie cookie = new Cookie("theme", theme);
        cookie.setMaxAge(30 * 24 * 60 * 60); // 30 дней в секундах
        cookie.setPath("/");                   // Для всего сайта
        cookie.setHttpOnly(true);              // Не доступна из JS
        cookie.setSecure(true);                // Только HTTPS

        resp.addCookie(cookie);
        resp.sendRedirect("/");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        // Читать куки из запроса
        Cookie[] cookies = req.getCookies();
        String theme = "light"; // по умолчанию

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("theme".equals(cookie.getName())) {
                    theme = cookie.getValue();
                }
            }
        }

        resp.getWriter().println("Текущая тема: " + theme);
    }
}
```

---

## 📌 Слайд 11: Filter — фильтры запросов

> **Filter** — компонент, который обрабатывает запросы **до** и **после** сервлета.  
> Используется для: логирования, авторизации, сжатия, кодировки и т.д.

### Цепочка фильтров:
```
Запрос → [Filter1] → [Filter2] → [Servlet] → [Filter2] → [Filter1] → Ответ
```

### Пример — фильтр авторизации:
```java
@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request   = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;

        String path = request.getRequestURI();

        // Публичные пути — пропускаем без проверки
        if (path.startsWith("/login") || path.startsWith("/register")) {
            chain.doFilter(req, resp); // передать дальше
            return;
        }

        // Проверяем сессию
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("/login");
            return;
        }

        chain.doFilter(req, resp); // авторизован — пропускаем
    }
}
```

### Пример — фильтр логирования:
```java
@WebFilter("/*")
public class LoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        long start = System.currentTimeMillis();

        System.out.printf("[%s] %s%n",
            request.getMethod(),
            request.getRequestURI());

        chain.doFilter(req, resp); // выполняем сервлет

        long duration = System.currentTimeMillis() - start;
        System.out.printf("  → %d мс%n", duration);
    }
}
```

---

## 📌 Слайд 12: ServletContext — контекст приложения

> **ServletContext** — общий объект для **всего приложения**.  
> Хранит данные, доступные всем сервлетам.

```java
@WebServlet("/stats")
public class StatsServlet extends HttpServlet {

    @Override
    public void init() throws ServletException {
        // Получить контекст приложения
        ServletContext ctx = getServletContext();

        // Записать общие данные при старте
        ctx.setAttribute("appStartTime", System.currentTimeMillis());
        ctx.setAttribute("visitCount", 0);

        // Читать параметры из web.xml
        String dbUrl = ctx.getInitParameter("db.url");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        ServletContext ctx = req.getServletContext();

        // Увеличить счётчик посещений (упрощённо, не потокобезопасно!)
        int visits = (int) ctx.getAttribute("visitCount");
        ctx.setAttribute("visitCount", ++visits);

        resp.getWriter().println("Посещений: " + visits);
    }
}
```

### web.xml — параметры контекста:
```xml
<web-app>
    <context-param>
        <param-name>db.url</param-name>
        <param-value>jdbc:postgresql://localhost:5432/mydb</param-value>
    </context-param>
</web-app>
```

---

## 📌 Слайд 13: RequestDispatcher — пересылка запросов

> **RequestDispatcher** позволяет **перенаправить** запрос внутри сервера  
> (не отправляя редирект клиенту).

### forward vs redirect:
| | forward | redirect |
|--|---------|---------|
| Кто перенаправляет | Сервер | Браузер |
| URL в браузере | Не меняется | Меняется |
| Запросов к серверу | 1 | 2 |
| Передача атрибутов | ✅ Да | ❌ Нет |

```java
@WebServlet("/profile")
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Добавить данные как атрибуты запроса
        req.setAttribute("username", "Иван");
        req.setAttribute("role", "admin");

        // Перенаправить на JSP для отображения
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/profile.jsp");
        dispatcher.forward(req, resp);

        // Или включить содержимое другого ресурса
        // dispatcher.include(req, resp);
    }
}
```

---

## 📌 Слайд 14: JSP — Java Server Pages (обзор)

> **JSP** — технология для написания HTML с вкраплениями Java-кода.  
> В современной разработке JSP заменяют шаблонизаторы (Thymeleaf, FreeMarker).

### Пример JSP страницы (`profile.jsp`):
```jsp
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head><title>Профиль</title></head>
<body>
    <h1>Привет, ${username}!</h1>
    <p>Роль: ${role}</p>

    <c:if test="${role == 'admin'}">
        <a href="/admin">Панель администратора</a>
    </c:if>

    <ul>
    <c:forEach var="item" items="${items}">
        <li>${item.name} — ${item.price} ₽</li>
    </c:forEach>
    </ul>
</body>
</html>
```

### Паттерн MVC с Servlet + JSP:
```
┌────────────────────────────────────────────────────┐
│  Браузер  ──GET /products──►  ProductServlet        │  Controller
│           ◄──HTML──          ProfileServlet         │
│                              (forward → JSP)        │
│                                   │                 │
│                               products.jsp          │  View
│                                   │                 │
│                             ProductService          │  Service
│                             ProductRepository       │  Model
└────────────────────────────────────────────────────┘
```

---

## 📌 Слайд 15: Структура проекта

### Maven-проект для веб-приложения:
```
my-webapp/
├── pom.xml
└── src/
    └── main/
        ├── java/
        │   └── com/example/
        │       ├── servlet/
        │       │   ├── ProductServlet.java
        │       │   └── LoginServlet.java
        │       ├── filter/
        │       │   ├── AuthFilter.java
        │       │   └── LoggingFilter.java
        │       ├── service/
        │       │   └── ProductService.java
        │       └── model/
        │           └── Product.java
        └── webapp/
            ├── WEB-INF/
            │   ├── web.xml
            │   └── views/
            │       ├── products.jsp
            │       └── login.jsp
            ├── css/
            │   └── style.css
            └── index.html
```

### `pom.xml` — зависимости:
```xml
<dependencies>
    <!-- Servlet API -->
    <dependency>
        <groupId>jakarta.servlet</groupId>
        <artifactId>jakarta.servlet-api</artifactId>
        <version>6.0.0</version>
        <scope>provided</scope>  <!-- предоставляется Tomcat -->
    </dependency>

    <!-- JSTL для JSP -->
    <dependency>
        <groupId>jakarta.servlet.jsp.jstl</groupId>
        <artifactId>jakarta.servlet.jsp.jstl-api</artifactId>
        <version>3.0.0</version>
    </dependency>
</dependencies>

<packaging>war</packaging>  <!-- Web ARchive -->
```

---

## 📌 Слайд 16: Развёртывание в Tomcat

### Установка Tomcat:
1. Скачать с [tomcat.apache.org](https://tomcat.apache.org)
2. Распаковать в папку
3. `bin/startup.bat` — запуск (Windows)
4. Открыть [http://localhost:8080](http://localhost:8080)

### Развёртывание WAR-файла:
```bash
# Собрать WAR
mvn clean package

# Скопировать в папку webapps Tomcat
cp target/my-webapp.war /tomcat/webapps/

# Tomcat автоматически разворачивает WAR
```

### Или через Maven-плагин Tomcat:
```xml
<plugin>
    <groupId>org.apache.tomcat.maven</groupId>
    <artifactId>tomcat7-maven-plugin</artifactId>
    <version>2.2</version>
    <configuration>
        <port>8080</port>
        <path>/</path>
    </configuration>
</plugin>
```
```bash
mvn tomcat7:run
```

### Или встроенный Tomcat (Embedded):
```java
// Запуск без установки Tomcat отдельно
public class Main {
    public static void main(String[] args) throws Exception {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        tomcat.addWebapp("/", new File("src/main/webapp").getAbsolutePath());
        tomcat.start();
        tomcat.getServer().await();
    }
}
```

---

## 📌 Слайд 17: Полный пример — ToDo приложение

### Модель:
```java
public class Task {
    private int id;
    private String title;
    private boolean done;

    // геттеры, сеттеры, конструкторы
}
```

### "Репозиторий" (хранение в памяти):
```java
public class TaskRepository {
    private static final List<Task> tasks = new ArrayList<>();
    private static int nextId = 1;

    public List<Task> findAll() {
        return Collections.unmodifiableList(tasks);
    }

    public void save(Task task) {
        task.setId(nextId++);
        tasks.add(task);
    }

    public boolean delete(int id) {
        return tasks.removeIf(t -> t.getId() == id);
    }

    public Optional<Task> findById(int id) {
        return tasks.stream().filter(t -> t.getId() == id).findFirst();
    }
}
```

### Сервлет:
```java
@WebServlet("/tasks")
public class TaskServlet extends HttpServlet {

    private final TaskRepository repo = new TaskRepository();

    // GET /tasks — показать все задачи
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("tasks", repo.findAll());
        req.getRequestDispatcher("/WEB-INF/views/tasks.jsp")
           .forward(req, resp);
    }

    // POST /tasks — создать задачу
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        req.setCharacterEncoding("UTF-8");
        String title = req.getParameter("title");

        if (title != null && !title.isBlank()) {
            Task task = new Task();
            task.setTitle(title.trim());
            task.setDone(false);
            repo.save(task);
        }

        resp.sendRedirect("/tasks");
    }
}

@WebServlet("/tasks/delete")
public class TaskDeleteServlet extends HttpServlet {

    private final TaskRepository repo = new TaskRepository();

    // POST /tasks/delete?id=3
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String idParam = req.getParameter("id");
        if (idParam != null) {
            repo.delete(Integer.parseInt(idParam));
        }
        resp.sendRedirect("/tasks");
    }
}
```

### JSP (`tasks.jsp`):
```jsp
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head><title>Задачи</title></head>
<body>
    <h1>Список задач</h1>

    <form action="/tasks" method="POST">
        <input type="text" name="title" placeholder="Новая задача" required>
        <button>Добавить</button>
    </form>

    <ul>
    <c:forEach var="task" items="${tasks}">
        <li>
            ${task.title}
            <form action="/tasks/delete" method="POST" style="display:inline">
                <input type="hidden" name="id" value="${task.id}">
                <button>Удалить</button>
            </form>
        </li>
    </c:forEach>
    </ul>
</body>
</html>
```

---

## 📌 Слайд 18: Сравнение Servlet vs Spring MVC

> Spring MVC — это **надстройка над сервлетами**.  
> Spring использует один `DispatcherServlet`, который сам маршрутизирует запросы.

| Возможность | Servlet API | Spring MVC |
|-------------|-------------|-----------|
| Маршрутизация | `@WebServlet("/path")` | `@GetMapping("/path")` |
| Параметры | `req.getParameter("id")` | `@RequestParam String id` |
| Тело запроса | Вручную читать `InputStream` | `@RequestBody` автоматически |
| Ответ | `resp.getWriter().println()` | `return "viewName"` или `@ResponseBody` |
| Сессия | `req.getSession()` | `@SessionAttributes`, `HttpSession` |
| Исключения | try/catch везде | `@ExceptionHandler` |
| DI зависимостей | Вручную (`new`) | `@Autowired` / `@Inject` |

### Вывод:
> 🏗️ Понимание сервлетов — это **фундамент** для понимания Spring MVC.  
> Spring MVC решает те же задачи, но гораздо удобнее и с меньшим количеством кода.

---

## 📌 Слайд 19: Типичные ошибки новичков

### ❌ 1. Забыть установить кодировку
```java
// ❌ Плохо — кириллица превращается в кракозябры
String name = req.getParameter("name");

// ✅ Хорошо
req.setCharacterEncoding("UTF-8");
resp.setContentType("text/html; charset=UTF-8");
String name = req.getParameter("name");
```

### ❌ 2. Состояние в полях сервлета (не потокобезопасно!)
```java
// ❌ Плохо — сервлет существует в одном экземпляре,
//           несколько потоков обращаются одновременно!
@WebServlet("/bad")
public class BadServlet extends HttpServlet {
    private int counter = 0; // ← ОПАСНО!

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        counter++; // ← состояние гонки (race condition)
        resp.getWriter().println(counter);
    }
}

// ✅ Хорошо — используем потокобезопасные типы
private final AtomicInteger counter = new AtomicInteger(0);
```

### ❌ 3. Писать HTML прямо в Java
```java
// ❌ Плохо — нечитаемо и неподдерживаемо
out.println("<html><body><table><tr><td>...");

// ✅ Хорошо — использовать JSP или шаблонизатор
req.getRequestDispatcher("/views/page.jsp").forward(req, resp);
```

### ❌ 4. Не закрывать ресурсы
```java
// ❌ Плохо
Connection conn = dataSource.getConnection();
// ...забыл закрыть

// ✅ Хорошо
try (Connection conn = dataSource.getConnection()) {
    // ...
}
```

---

## 📌 Слайд 20: Итоги

### Что мы изучили:

| Тема | Описание |
|------|---------|
| HTTP | Протокол запрос-ответ, методы, коды |
| Servlet | Java-класс для обработки HTTP |
| Жизненный цикл | `init()` → `service()` → `destroy()` |
| HttpServlet | `doGet()`, `doPost()` и другие методы |
| Request/Response | Параметры, заголовки, тело, статус |
| Session | Состояние пользователя между запросами |
| Cookie | Данные в браузере |
| Filter | Перехват запросов до/после сервлета |
| JSP | HTML + Java = шаблон страницы |
| Tomcat | Servlet Container |

### Servlet API → Spring MVC:
```
Servlet API  ──► Spring MVC  ──► Spring Boot
  (основа)       (удобнее)       (ещё удобнее)
```

> 💡 **Главная мысль**: Spring не заменяет сервлеты — он использует их под капотом.  
> Понимая сервлеты, вы понимаете, как работает Spring.

---

## 📝 Домашнее задание

### Задание 1 (базовое):
Создать веб-приложение **"Записная книжка"**:
- `GET /contacts` — показать список контактов (имя + телефон)
- `POST /contacts` — добавить новый контакт
- `POST /contacts/delete?id=N` — удалить контакт

Хранить данные в `ArrayList` (в памяти).

### Задание 2 (усложнённое):
Добавить к приложению из Задания 1:
- **Авторизацию** через `Filter` — неавторизованных редиректить на `/login`
- **Сессию** — хранить информацию о залогиненном пользователе
- **Cookie** — запоминать тему оформления (светлая/тёмная)

### Вопросы для самопроверки:
1. В чём разница между `forward()` и `sendRedirect()`?
2. Почему нельзя хранить изменяемое состояние в полях сервлета?
3. Что такое Servlet Container и зачем он нужен?
4. Чем отличается `HttpSession` от `Cookie`?
5. Что делает `FilterChain.doFilter()`?

---

*Лектор: Виктор | Преподаватель практических: Многоуважаемая Анастасия Юрьевна*

