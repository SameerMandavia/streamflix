package com.streamflix.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "app_users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true, length = 320) private String email;
    @Column(name = "password_hash", nullable = false) private String passwordHash;
    @Column(nullable = false, length = 20) private String role = "USER";
    @Column(nullable = false, updatable = false) private Instant createdAt;

    protected User() {}
    public User(String email, String passwordHash) { this.email = email; this.passwordHash = passwordHash; }
    @PrePersist void prePersist() { if (createdAt == null) createdAt = Instant.now(); }
    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public String getRole() { return role; }
    public Instant getCreatedAt() { return createdAt; }
    public void setRole(String role) { this.role = role; }
}
