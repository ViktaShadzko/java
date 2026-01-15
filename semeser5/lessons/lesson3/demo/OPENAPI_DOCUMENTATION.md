# OpenAPI Documentation

## Overview
This project now includes comprehensive OpenAPI (Swagger) documentation for all REST endpoints.

## Configuration
The OpenAPI configuration is located in `org.example.configuration.OpenApiConfig` and includes:
- API title: "SpaceShip REST API"
- Version: 1.0
- Description of the API functionality
- Contact information
- License information (Apache 2.0)
- Server configurations (Development and Production)

## Accessing the OpenAPI Documentation

Once the application is running, you can access the OpenAPI documentation at:

### Swagger UI (Interactive Documentation)
```
http://localhost:8080/swagger-ui.html
```
or
```
http://localhost:8080/swagger-ui/index.html
```

### OpenAPI JSON Specification
```
http://localhost:8080/v3/api-docs
```

### OpenAPI YAML Specification
```
http://localhost:8080/v3/api-docs.yaml
```

## API Endpoints Documentation

### SpaceShip Controller (`/api/v1/space/`)

All endpoints include comprehensive OpenAPI annotations with:
- Operation summaries and descriptions
- Request/Response schemas
- HTTP status codes
- Parameter descriptions

#### Available Operations:

1. **GET /api/v1/space/spaceShip**
   - Summary: Get all spaceships
   - Returns: List of all spaceships

2. **GET /api/v1/space/spaceShip/{id}**
   - Summary: Get spaceship by ID
   - Parameters: `id` (path parameter) - ID of the spaceship to retrieve
   - Returns: Single spaceship object

3. **POST /api/v1/space/spaceShip**
   - Summary: Create a new spaceship
   - Request Body: SpaceShip object
   - Returns: Created spaceship

4. **PATCH /api/v1/space/spaceShip/{id}**
   - Summary: Partially update spaceship
   - Parameters: `id` (path parameter) - ID of the spaceship to update
   - Request Body: SpaceShip object with fields to update
   - Returns: Updated spaceship

5. **PUT /api/v1/space/spaceShip/{id}**
   - Summary: Update or replace spaceship
   - Parameters: `id` (path parameter) - ID of the spaceship to replace
   - Request Body: Complete SpaceShip object
   - Returns: Replaced spaceship

6. **DELETE /api/v1/space/spaceShip/{id}**
   - Summary: Delete spaceship
   - Parameters: `id` (path parameter) - ID of the spaceship to delete
   - Returns: No content

### Welcome Controller (`/welcome`)

1. **GET /welcome**
   - Summary: Display welcome page
   - Parameters: `user` (query parameter, optional) - User name for personalized greeting (default: "Guest")
   - Returns: HTML welcome page

## Entity Documentation

The `SpaceShip` entity includes Schema annotations describing:
- `id`: Unique identifier of the spaceship
- `guns`: List of guns mounted on the spaceship
- `shieldGenerator`: Shield generator protecting the spaceship
- `engines`: Engine system powering the spaceship
- `name`: Name of the spaceship (e.g., "USS Enterprise")

## How to Generate OpenAPI Spec File

### Option 1: During Runtime
Access the JSON or YAML endpoints mentioned above while the application is running.

### Option 2: Using Maven Plugin (Optional)
You can add the springdoc-openapi-maven-plugin to generate the spec during build:

```xml
<plugin>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-maven-plugin</artifactId>
    <version>1.4</version>
    <executions>
        <execution>
            <goals>
                <goal>generate</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

### Option 3: Export from Swagger UI
1. Start the application
2. Navigate to http://localhost:8080/swagger-ui.html
3. Click on the `/v3/api-docs` link at the top
4. Save the JSON content to a file

## Features

### Interactive API Testing
Swagger UI allows you to:
- View all available endpoints
- See request/response schemas
- Test endpoints directly from the browser
- View example values
- See all possible response codes

### Response Codes
All endpoints document their possible HTTP response codes:
- 200: Success
- 400: Bad Request (Invalid input data)
- 404: Not Found (Resource not found)

### Tags and Grouping
Controllers are organized with tags:
- **SpaceShip**: All spaceship management operations
- **Welcome**: Welcome page operations

## Dependencies

The project uses SpringDoc OpenAPI v3:
```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>3.0.0</version>
</dependency>
```

## Notes

- OpenAPI annotations are following the OpenAPI 3.0 specification
- All REST endpoints return JSON (application/json)
- The Welcome controller returns HTML (text/html)
- Schema descriptions help with code generation tools
- The API documentation is automatically kept in sync with the code

## Integration with Third-Party Tools

The generated OpenAPI spec can be imported into:
- **Postman**: For API testing and collection generation
- **Insomnia**: For REST API development
- **API Gateways**: For automatic API proxy configuration
- **Code Generators**: To generate client SDKs in various languages
- **Documentation Tools**: To create custom API documentation

