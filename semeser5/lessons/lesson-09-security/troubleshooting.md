# Troubleshooting: Spring Security

## 403 Forbidden (Access Denied)

**Cause:** User is authenticated but lacks required role/permission.

**Check:**
1. User has the correct role (with `ROLE_` prefix)
2. Security configuration matches your endpoint patterns
3. Role check: `hasRole("ADMIN")` expects `ROLE_ADMIN` authority

**Debug:**
```java
@GetMapping("/debug")
public String debug(Authentication auth) {
    return "User: " + auth.getName() +
           ", Roles: " + auth.getAuthorities();
}
```

---

## 401 Unauthorized

**Cause:** Authentication failed or missing.

**Check:**
1. JWT token is present in `Authorization: Bearer <token>` header
2. Token is not expired
3. Token signature is valid (same secret key)
4. `UserDetailsService` returns correct user

---

## CORS Errors

**Error in browser:**
```
Access to XMLHttpRequest blocked by CORS policy
```

**Solution:** Configure CORS in SecurityFilterChain:
```java
@Bean
public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http
        .cors(cors -> cors.configurationSource(corsConfigurationSource()))
        // ... other config
        .build();
}

@Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(List.of("http://localhost:3000"));
    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
    config.setAllowedHeaders(List.of("*"));
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
}
```

---

## JWT Token Invalid

**Error:**
```
io.jsonwebtoken.security.SignatureException: JWT signature does not match
```

**Check:**
1. Same secret key used for signing and validation
2. Secret key is Base64 encoded if using `Decoders.BASE64.decode()`
3. Token wasn't modified after generation

---

## Password Not Matching

**Problem:** Login fails even with correct password.

**Check:**
1. Password is encoded with `PasswordEncoder` before saving
2. Same encoder used for verification
3. Don't encode password twice

```java
// Registration
String encoded = passwordEncoder.encode(rawPassword);
user.setPassword(encoded);

// DON'T do this
user.setPassword(passwordEncoder.encode(passwordEncoder.encode(raw)));
```

---

## Circular Dependency with Security

**Error:**
```
The dependencies of some of the beans form a cycle
```

**Solution:** Use `@Lazy` on one of the dependencies:
```java
public SecurityConfig(@Lazy JwtAuthenticationFilter jwtFilter) {
    this.jwtFilter = jwtFilter;
}
```
