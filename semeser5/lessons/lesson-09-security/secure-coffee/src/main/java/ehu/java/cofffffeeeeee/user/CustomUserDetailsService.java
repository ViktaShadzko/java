package ehu.java.cofffffeeeeee.user;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final InMemoryUserService userService;

    public CustomUserDetailsService(InMemoryUserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        InMemoryUserService.UserRecord record = userService.find(username);
        if (record == null) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return User.withUsername(record.username)
                .password(record.passwordHash)
                .roles(record.roles.toArray(String[]::new))
                .build();
    }
}

