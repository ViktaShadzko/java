# Разница между REST и не-REST архитектурами

## REST (Representational State Transfer)

REST - это архитектурный стиль для разработки веб-сервисов, основанный на HTTP протоколе и использующий его возможности в полной мере.

### Основные принципы REST:

1. **Stateless (Без состояния)** - каждый запрос содержит всю необходимую информацию для его обработки
2. **Client-Server** - разделение клиента и сервера
3. **Uniform Interface** - единообразный интерфейс
4. **Resource-based** - работа с ресурсами через URI
5. **HTTP методы** - использование HTTP методов по назначению (GET, POST, PUT, DELETE, PATCH)

### Пример REST API:

```java
@RestController
@RequestMapping("/api/users")
public class UserRestController {
    
    @GetMapping
    public List<User> getAllUsers() {
        // GET /api/users - получить всех пользователей
        return userService.findAll();
    }
    
    @GetMapping("/{id}")
    public User getUserById(@PathVariable Long id) {
        // GET /api/users/5 - получить пользователя с id=5
        return userService.findById(id);
    }
    
    @PostMapping
    public User createUser(@RequestBody User user) {
        // POST /api/users - создать нового пользователя
        return userService.save(user);
    }
    
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        // PUT /api/users/5 - обновить пользователя с id=5
        user.setId(id);
        return userService.update(user);
    }
    
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        // DELETE /api/users/5 - удалить пользователя с id=5
        userService.delete(id);
    }
}
```

### Характеристики REST запросов:

```http
GET /api/users/5 HTTP/1.1
Host: example.com
Accept: application/json

Response:
HTTP/1.1 200 OK
Content-Type: application/json

{
  "id": 5,
  "name": "John Doe",
  "email": "john@example.com"
}
```

---

## Не-REST архитектуры

### 1. RPC (Remote Procedure Call)

RPC фокусируется на вызове удаленных методов, а не на манипулировании ресурсами.

```java
@Controller
public class UserRpcController {
    
    @PostMapping("/getUserById")
    public User getUserById(@RequestParam Long id) {
        // POST /getUserById?id=5
        return userService.findById(id);
    }
    
    @PostMapping("/createUser")
    public User createUser(@RequestParam String name, @RequestParam String email) {
        // POST /createUser?name=John&email=john@example.com
        return userService.create(name, email);
    }
    
    @PostMapping("/deleteUser")
    public void deleteUser(@RequestParam Long id) {
        // POST /deleteUser?id=5
        userService.delete(id);
    }
    
    @PostMapping("/updateUserEmail")
    public User updateUserEmail(@RequestParam Long id, @RequestParam String email) {
        // POST /updateUserEmail?id=5&email=newemail@example.com
        return userService.updateEmail(id, email);
    }
}
```

### 2. SOAP (Simple Object Access Protocol)

SOAP использует XML для обмена структурированными сообщениями.

```xml
<!-- SOAP запрос -->
POST /UserService HTTP/1.1
Host: example.com
Content-Type: text/xml; charset=utf-8

<?xml version="1.0"?>
<soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope">
  <soap:Body>
    <m:GetUser xmlns:m="http://example.com/user">
      <m:UserId>5</m:UserId>
    </m:GetUser>
  </soap:Body>
</soap:Envelope>

<!-- SOAP ответ -->
<?xml version="1.0"?>
<soap:Envelope xmlns:soap="http://www.w3.org/2003/05/soap-envelope">
  <soap:Body>
    <m:GetUserResponse xmlns:m="http://example.com/user">
      <m:User>
        <m:Id>5</m:Id>
        <m:Name>John Doe</m:Name>
        <m:Email>john@example.com</m:Email>
      </m:User>
    </m:GetUserResponse>
  </soap:Body>
</soap:Envelope>
```

### 3. GraphQL

GraphQL позволяет клиенту запрашивать только нужные данные.

```graphql
# GraphQL запрос
query {
  user(id: 5) {
    name
    email
  }
}

# GraphQL ответ
{
  "data": {
    "user": {
      "name": "John Doe",
      "email": "john@example.com"
    }
  }
}
```

---

## Сравнительная таблица

| Характеристика | REST | RPC | SOAP | GraphQL |
|----------------|------|-----|------|---------|
| **Подход** | Ресурсо-ориентированный | Процедурно-ориентированный | Контракто-ориентированный | Запросо-ориентированный |
| **HTTP методы** | GET, POST, PUT, DELETE, PATCH | Обычно только POST | POST | POST |
| **URL структура** | `/api/users/5` | `/getUserById` | `/UserService` | `/graphql` |
| **Формат данных** | JSON, XML | JSON, XML, Binary | XML | JSON |
| **Кеширование** | Встроенное (HTTP) | Сложное | Сложное | Сложное |
| **Версионирование** | `/api/v1/users` | `/v1/getUserById` | Namespace в WSDL | Schema evolution |
| **Гибкость запросов** | Фиксированные endpoints | Фиксированные методы | Фиксированные операции | Гибкие запросы |

---

## Практический пример: Управление книгами

### REST подход:

```java
@RestController
@RequestMapping("/api/books")
public class BookRestController {
    
    // Получить все книги
    // GET /api/books
    @GetMapping
    public List<Book> getBooks() {
        return bookService.findAll();
    }
    
    // Получить книгу по ID
    // GET /api/books/10
    @GetMapping("/{id}")
    public Book getBook(@PathVariable Long id) {
        return bookService.findById(id);
    }
    
    // Создать книгу
    // POST /api/books
    // Body: {"title": "Java Programming", "author": "John Smith"}
    @PostMapping
    public Book createBook(@RequestBody Book book) {
        return bookService.save(book);
    }
    
    // Обновить книгу
    // PUT /api/books/10
    // Body: {"title": "Advanced Java", "author": "John Smith"}
    @PutMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book book) {
        return bookService.update(id, book);
    }
    
    // Удалить книгу
    // DELETE /api/books/10
    @DeleteMapping("/{id}")
    public void deleteBook(@PathVariable Long id) {
        bookService.delete(id);
    }
    
    // Получить книги автора
    // GET /api/books?author=John+Smith
    @GetMapping(params = "author")
    public List<Book> getBooksByAuthor(@RequestParam String author) {
        return bookService.findByAuthor(author);
    }
}
```

### RPC подход:

```java
@Controller
public class BookRpcController {
    
    // POST /getAllBooks
    @PostMapping("/getAllBooks")
    public List<Book> getAllBooks() {
        return bookService.findAll();
    }
    
    // POST /getBookById?id=10
    @PostMapping("/getBookById")
    public Book getBookById(@RequestParam Long id) {
        return bookService.findById(id);
    }
    
    // POST /createBook?title=Java+Programming&author=John+Smith
    @PostMapping("/createBook")
    public Book createBook(@RequestParam String title, @RequestParam String author) {
        return bookService.create(title, author);
    }
    
    // POST /updateBook?id=10&title=Advanced+Java&author=John+Smith
    @PostMapping("/updateBook")
    public Book updateBook(@RequestParam Long id, 
                          @RequestParam String title, 
                          @RequestParam String author) {
        return bookService.update(id, title, author);
    }
    
    // POST /deleteBook?id=10
    @PostMapping("/deleteBook")
    public void deleteBook(@RequestParam Long id) {
        bookService.delete(id);
    }
    
    // POST /getBooksByAuthor?author=John+Smith
    @PostMapping("/getBooksByAuthor")
    public List<Book> getBooksByAuthor(@RequestParam String author) {
        return bookService.findByAuthor(author);
    }
}
```

---

## Основные отличия

### REST:
✅ **Преимущества:**
- Использует HTTP по назначению
- Легко кешируется
- Понятная семантика (GET = чтение, POST = создание)
- Stateless - легко масштабируется
- Широко распространен и понятен

❌ **Недостатки:**
- Over-fetching или Under-fetching данных
- Множество запросов для связанных данных
- Нет стандарта для некоторых операций

### RPC:
✅ **Преимущества:**
- Простой и понятный
- Гибкость в именовании операций
- Может быть более производительным для специфичных операций

❌ **Недостатки:**
- Не использует HTTP семантику
- Сложно кешировать
- Нет единого стандарта
- Обычно использует только POST

---

## Когда использовать REST:

1. **CRUD операции** - создание, чтение, обновление, удаление
2. **Публичные API** - для сторонних разработчиков
3. **Микросервисная архитектура**
4. **Когда важно кеширование**
5. **Стандартные веб-приложения**

## Когда использовать не-REST:

1. **RPC** - внутренние коммуникации между сервисами, где производительность критична
2. **SOAP** - корпоративные системы с жесткими требованиями к контракту
3. **GraphQL** - когда клиенту нужна гибкость в выборе данных (mobile apps, SPA)

---

## Пример миграции с RPC на REST

### Было (RPC-style):
```
POST /getUserById?id=5
POST /createUser
POST /updateUser
POST /deleteUser
POST /getUsersByRole?role=admin
```

### Стало (REST):
```
GET    /api/users/5
POST   /api/users
PUT    /api/users/5
DELETE /api/users/5
GET    /api/users?role=admin
```

## Вывод

REST - это не просто набор правил, а философия проектирования API, которая использует возможности HTTP протокола. Он делает API более предсказуемым, кешируемым и легким для понимания. 

Однако выбор между REST и другими подходами зависит от конкретных требований проекта:
- **REST** для стандартных веб-API
- **RPC** для высокопроизводительных внутренних коммуникаций
- **GraphQL** для гибких клиентских запросов
- **SOAP** для систем требующих строгого контракта

