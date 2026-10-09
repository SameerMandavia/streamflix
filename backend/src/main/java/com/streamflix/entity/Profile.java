package com.streamflix.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "profiles", uniqueConstraints = @UniqueConstraint(name = "uq_profiles_user_name", columnNames = {"user_id", "name"}))
public class Profile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "user_id", nullable = false) private User user;
    @Column(nullable = false, length = 80) private String name;
    @Column(name = "avatar_key", nullable = false, length = 40) private String avatarKey;
    @Column(nullable = false, updatable = false) private Instant createdAt;

    protected Profile() {}
    public Profile(User user, String name, String avatarKey) { this.user = user; this.name = name; this.avatarKey = avatarKey; }
    @PrePersist void prePersist() { if (createdAt == null) createdAt = Instant.now(); }
    public Long getId() { return id; }
    public User getUser() { return user; }
    public String getName() { return name; }
    public String getAvatarKey() { return avatarKey; }
}
