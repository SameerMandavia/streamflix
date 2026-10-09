package com.streamflix.config;
import com.streamflix.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
public class AdminBootstrap {
    private final UserRepository users; private final String adminEmail;
    public AdminBootstrap(UserRepository users, @Value("${app.admin.email:}") String adminEmail) { this.users = users; this.adminEmail = adminEmail; }
    @PostConstruct void promoteConfiguredAdmin() { if (adminEmail != null && !adminEmail.isBlank()) users.findByEmailIgnoreCase(adminEmail.trim()).ifPresent(user -> { if (!"ADMIN".equals(user.getRole())) { user.setRole("ADMIN"); users.save(user); } }); }
}
