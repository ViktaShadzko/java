# Secure Coffee - Spring Security with JWT

This is the Lesson 09 Security implementation based on the Lesson 5 coffee application.

## Features Implemented

### 1. JWT-based Authentication
- **JwtService**: Generates and validates JWT tokens with HMAC-SHA256
  - Token contains username and roles
  - 1 hour expiration time
  - Base64-encoded secret key

### 2. Security Configuration
- **Stateless sessions**: No server-side session storage
- **CSRF disabled**: For REST API usage
- **Method-based access control**:
  - `GET /**` - Public access
  - `POST /login` - Public access
  - `POST /register` - Public access
  - `POST /**` - Requires authentication
  - `PUT /**` - Requires authentication
  - `DELETE /**` - Requires authentication

### 3. User Management
- **InMemoryUserService**: Simple in-memory user store with ConcurrentHashMap
  - Default users: `user/password` (ROLE_USER), `admin/password` (ROLE_ADMIN)
  - Password hashing with BCryptPasswordEncoder
  
- **CustomUserDetailsService**: Implements Spring Security's UserDetailsService
  - Loads users for authentication

### 4. Authentication Endpoints
- **POST /register**: Register new user
  ```json
  {
    "username": "alice",
    "password": "secret",
    "roles": ["USER"]  // optional, defaults to ["USER"]
  }
  ```

- **POST /login**: Authenticate and receive JWT
  ```json
  {
    "username": "alice",
    "password": "secret"
  }
  ```
  Response:
  ```json
  {
    "token": "eyJhbGciOiJIUzI1NiJ9..."
  }
  ```

### 5. JWT Filter
- **JwtAuthenticationFilter**: Processes Authorization header
  - Extracts Bearer token
  - Validates token
  - Sets SecurityContext with user and authorities

## How to Build and Run

### Build
```powershell
mvn clean package -DskipTests
```

### Run
```powershell
mvn spring-boot:run
```

## Testing with cURL

### 1. Register a new user
```powershell
curl -X POST http://localhost:8080/register `
  -H "Content-Type: application/json" `
  -d '{"username":"alice","password":"secret","roles":["USER"]}'
```

### 2. Login and get JWT token
```powershell
$response = curl -X POST http://localhost:8080/login `
  -H "Content-Type: application/json" `
  -d '{"username":"alice","password":"secret"}' | ConvertFrom-Json

$token = $response.token
```

### 3. Test public GET endpoint (no token needed)
```powershell
curl http://localhost:8080/welcome
```

### 4. Test protected POST endpoint (token required)
```powershell
curl -X POST http://localhost:8080/beverages `
  -H "Authorization: Bearer $token" `
  -H "Content-Type: application/json" `
  -d '{"name":"Espresso","price":2.50}'
```

### 5. Test without token (should get 401)
```powershell
curl -X POST http://localhost:8080/beverages `
  -H "Content-Type: application/json" `
  -d '{"name":"Espresso","price":2.50}'
```

## Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                      HTTP Request                            │
└───────────────────────────┬─────────────────────────────────┘
                            │
                            ▼
                ┌───────────────────────┐
                │ JwtAuthenticationFilter│
                │ - Extract Bearer token │
                │ - Validate JWT         │
                │ - Set SecurityContext  │
                └───────────┬───────────┘
                            │
                            ▼
                ┌───────────────────────┐
                │  SecurityFilterChain   │
                │ - Check access rules   │
                │ - Permit/Deny request  │
                └───────────┬───────────┘
                            │
                            ▼
                ┌───────────────────────┐
                │    @RestController     │
                │ - AuthController       │
                │ - BeverageController   │
                │ - WelcomeController    │
                └───────────────────────┘
```

## Topics Covered

1. ✅ Spring Security basics (filters, authentication, authorization, stateless)
2. ✅ UserDetailsService and UserDetails for user lookup
3. ✅ Password hashing with BCryptPasswordEncoder
4. ✅ JWT structure, signing, validation, expiration
5. ✅ Token-based auth flow (register, login, Authorization header)
6. ✅ SecurityFilterChain configuration (permit GET, protect POST/PUT/DELETE)
7. ✅ Custom JWT filter (extract/validate token, set SecurityContext)
8. ✅ Role-based access control (ROLE_USER, ROLE_ADMIN in tokens)
9. ✅ In-memory user storage (ConcurrentHashMap)
10. ✅ AuthenticationManager for login validation
11. ✅ Maven dependencies (spring-boot-starter-security, jjwt)
12. ✅ Controller design for /login and /register endpoints

## Security Notes

⚠️ **For Production**:
- Move JWT secret to environment variable or secure vault
- Use proper key length (256+ bits)
- Implement refresh tokens
- Add rate limiting on login endpoint
- Use HTTPS only
- Add token blacklist for logout
- Implement proper exception handling
- Add authentication event logging
- Consider using Spring Security OAuth2 Resource Server for JWT validation

## Next Steps (Optional Enhancements)

- [ ] Add refresh token endpoint
- [ ] Implement role-based method security with `@PreAuthorize("hasRole('ADMIN')")`
- [ ] Add AuthenticationEventPublisher for login success/failure events
- [ ] Externalize JWT configuration to application.properties
- [ ] Add integration tests for security
- [ ] Implement token blacklist for logout
- [ ] Add password strength validation
- [ ] Implement account lockout after failed attempts

