# Task 3: Add Authentication to Application

## Objective

Implement JWT-based authentication and secure REST API endpoints.

## Tasks

1. Add a user service storing users in memory (using `List` or `Map`)
2. Implement `UserDetailsService` interface
3. Implement the following endpoints:
   - `POST /login` - authenticate user (returns JWT token)
   - `POST /register` - register new user
4. Save user to memory on registration
5. On login, verify user exists and return JWT token
6. Use Spring Security to protect POST/PUT/DELETE endpoints (only authenticated users)
7. Configure `SecurityFilterChain` for stateless JWT authentication

## Deliverables

- `UserDetailsService` implementation
- JWT token generation and validation service
- Security configuration with `SecurityFilterChain`
- Protected endpoints requiring authentication

## Acceptance Criteria

- Unauthenticated users can only access GET endpoints
- Registration creates a new user in memory
- Login returns a valid JWT token
- Protected endpoints require valid JWT in Authorization header
- Invalid/expired tokens return 401 Unauthorized

## Extra Tasks

- Add JWT refresh token endpoint
- Implement role-based access (ADMIN and USER roles)
- Protect POST and DELETE currency/item endpoints for ADMIN only
- Use `AuthenticationEventPublisher` for login event tracking

---

## Grading (10 points)

| Criteria | Points |
|----------|--------|
| UserDetailsService implementation | 2 |
| JWT generation and validation | 2 |
| Login/Register endpoints work | 2 |
| SecurityFilterChain configured | 2 |
| Protected endpoints require auth | 2 |
| Extra tasks (bonus) | +2 |
