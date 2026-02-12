package ehu.java.cofffffeeeeee.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class InMemoryUserService {
    public static class UserRecord {
        public final String username;
        public final String passwordHash;
        public final List<String> roles;
        public UserRecord(String username, String passwordHash, List<String> roles) {
            this.username = username;
            this.passwordHash = passwordHash;
            this.roles = roles;
        }
    }

    private final Map<String, UserRecord> users = new ConcurrentHashMap<>();
    private final PasswordEncoder encoder;

    public InMemoryUserService(PasswordEncoder encoder) {
        this.encoder = encoder;
        register("user", "password", List.of("USER"));
        register("admin", "password", List.of("ADMIN"));
    }

    public boolean exists(String username) {
        return users.containsKey(username);
    }

    public void register(String username, String rawPassword, List<String> roles) {
        String hash = encoder.encode(rawPassword);
        users.put(username, new UserRecord(username, hash, new ArrayList<>(roles)));
    }

    public UserRecord find(String username) {
        return users.get(username);
    }
}
