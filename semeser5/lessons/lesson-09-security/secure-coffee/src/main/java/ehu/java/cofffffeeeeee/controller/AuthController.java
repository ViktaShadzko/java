package ehu.java.cofffffeeeeee.controller;

import ehu.java.cofffffeeeeee.dto.JwtResponse;
import ehu.java.cofffffeeeeee.dto.LoginRequest;
import ehu.java.cofffffeeeeee.dto.RegisterRequest;
import ehu.java.cofffffeeeeee.security.JwtService;
import ehu.java.cofffffeeeeee.user.InMemoryUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuthController {
    private final InMemoryUserService users;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(InMemoryUserService users,
                          AuthenticationManager authenticationManager,
                          JwtService jwtService) {
        this.users = users;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (request.username == null || request.username.isBlank() || request.password == null || request.password.isBlank()) {
            return ResponseEntity.badRequest().body("username and password are required");
        }
        if (users.exists(request.username)) {
            return ResponseEntity.status(409).body("user already exists");
        }
        List<String> roles = (request.roles == null || request.roles.isEmpty()) ? List.of("USER") : request.roles;
        users.register(request.username, request.password, roles);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (request.username == null || request.username.isBlank() || request.password == null || request.password.isBlank()) {
            return ResponseEntity.badRequest().body("username and password are required");
        }
        // Authenticate using AuthenticationManager, which delegates to UserDetailsService and PasswordEncoder
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username, request.password));
        if (auth.isAuthenticated()) {
            InMemoryUserService.UserRecord record = users.find(request.username);
            String token = jwtService.generateToken(record.username, record.roles);
            return ResponseEntity.ok(new JwtResponse(token));
        }
        return ResponseEntity.status(401).body("invalid credentials");
    }
}

