# OpenAPI Quick Reference Card

## 🚀 Quick Start

### Access Swagger UI
```
http://localhost:8080/swagger-ui.html
```

### Get OpenAPI Spec
```
http://localhost:8080/v3/api-docs          (JSON)
http://localhost:8080/v3/api-docs.yaml     (YAML)
```

---

## 📝 Key Annotations Used

### Controller Level
```java
@Tag(name = "SpaceShip", description = "SpaceShip management APIs")
```
Groups endpoints under a tag in Swagger UI.

### Method Level
```java
@Operation(
    summary = "Get all spaceships",
    description = "Returns a list of all spaceships in the system"
)
```
Describes what the endpoint does.

### Response Documentation
```java
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200",
        description = "Successfully retrieved",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = SpaceShip.class)
        )
    ),
    @ApiResponse(
        responseCode = "404",
        description = "Not found"
    )
})
```
Documents possible HTTP responses.

### Parameter Documentation
```java
@Parameter(description = "ID of the spaceship to retrieve", required = true)
@PathVariable int id
```
Describes path variables, query params, and request bodies.

### Entity Schema
```java
@Schema(description = "SpaceShip entity", example = "USS Enterprise")
private String name;
```
Documents entity fields.

---

## 🧪 Testing Endpoints

### Using Swagger UI
1. Open `http://localhost:8080/swagger-ui.html`
2. Click on endpoint → "Try it out"
3. Fill in parameters
4. Click "Execute"
5. View response

### Using curl (Bash)
```bash
# GET all spaceships
curl http://localhost:8080/api/v1/space/spaceShip

# GET by ID
curl http://localhost:8080/api/v1/space/spaceShip/1

# POST create
curl -X POST http://localhost:8080/api/v1/space/spaceShip \
  -H "Content-Type: application/json" \
  -d '{"id":1,"name":"Enterprise"}'

# DELETE
curl -X DELETE http://localhost:8080/api/v1/space/spaceShip/1
```

### Using PowerShell
```powershell
# GET all spaceships
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/space/spaceShip"

# POST create
$body = @{id=1; name="Enterprise"} | ConvertTo-Json
Invoke-RestMethod -Uri "http://localhost:8080/api/v1/space/spaceShip" `
  -Method Post -Body $body -ContentType "application/json"
```

---

## 📦 Project Structure

```
src/main/java/org/example/
├── configuration/
│   ├── OpenApiConfig.java       ← OpenAPI configuration
│   └── WebConfig.java
├── controller/
│   ├── SpaceShipController.java ← REST endpoints with @Operation
│   └── WelcomeController.java   ← MVC endpoint with @Operation
├── entity/
│   └── SpaceShip.java           ← Entity with @Schema
└── service/
    └── SpaceShipService.java
```

---

## 🎯 HTTP Methods & Endpoints

| Method | Endpoint | Purpose |
|--------|----------|---------|
| GET | `/api/v1/space/spaceShip` | Get all spaceships |
| GET | `/api/v1/space/spaceShip/{id}` | Get one by ID |
| POST | `/api/v1/space/spaceShip` | Create new |
| PATCH | `/api/v1/space/spaceShip/{id}` | Partial update |
| PUT | `/api/v1/space/spaceShip/{id}` | Full replace |
| DELETE | `/api/v1/space/spaceShip/{id}` | Delete |

---

## 🔧 Configuration

### OpenApiConfig.java
```java
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("SpaceShip REST API")
                .version("1.0")
                .description("...")
                .contact(new Contact()...)
                .license(new License()...))
            .servers(List.of(
                new Server().url("http://localhost:8080")...
            ));
    }
}
```

### Required Dependency (pom.xml)
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>3.0.0</version>
</dependency>
```

---

## 💡 Common Use Cases

### Export Spec for Postman
1. Download: `curl http://localhost:8080/v3/api-docs -o openapi.json`
2. Postman → Import → Upload `openapi.json`

### Generate Client SDK
```bash
# Download spec
curl http://localhost:8080/v3/api-docs -o openapi.json

# Generate TypeScript client
openapi-generator generate -i openapi.json -g typescript-axios -o ./client
```

### Share API Documentation
Send team members the Swagger UI link when app is running:
```
http://localhost:8080/swagger-ui.html
```

---

## 📚 Documentation Files

- `OPENAPI_SUMMARY.md` - Complete overview of changes
- `OPENAPI_DOCUMENTATION.md` - Detailed documentation guide  
- `API_TESTING_EXAMPLES.md` - curl & PowerShell examples
- `QUICK_REFERENCE.md` - This file

---

## ✅ Checklist

- [x] Added OpenAPI annotations to controllers
- [x] Added @Schema annotations to entities
- [x] Created OpenApiConfig configuration
- [x] Documented all endpoints
- [x] Added request/response examples
- [x] Documented HTTP response codes
- [x] Tagged endpoints for organization
- [x] Created documentation files

---

## 🆘 Troubleshooting

**Swagger UI not loading?**
- Check app is running on port 8080
- Try: `http://localhost:8080/swagger-ui/index.html`

**Endpoints not showing?**
- Verify controllers have `@RestController` or `@Controller`
- Check component scan includes controller packages

**Schema not rendering?**
- Ensure entities have `@Schema` annotations
- Check Lombok @Getter is present

---

## 🎓 Learning Resources

- OpenAPI Spec: https://swagger.io/specification/
- SpringDoc: https://springdoc.org/
- Swagger UI: https://swagger.io/tools/swagger-ui/

