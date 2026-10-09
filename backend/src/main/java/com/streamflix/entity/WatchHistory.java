package com.streamflix.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "watch_history", uniqueConstraints = @UniqueConstraint(name = "uq_history_user_movie", columnNames = {
        "user_id", "movie_id" }))
public class WatchHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movie_id")
    private Movie movie;
    @Column(nullable = false)
    private Integer progressSeconds;
    @Column(nullable = false)
    private boolean completed;
    @Column(nullable = false)
    private Instant lastWatchedAt;

    protected WatchHistory() {
    }

    public WatchHistory(User user, Movie movie, Integer progressSeconds, boolean completed) {
        this.user = user;
        this.movie = movie;
        this.progressSeconds = progressSeconds;
        this.completed = completed;
    }

    @PrePersist
    @PreUpdate
    void touch() {
        lastWatchedAt = Instant.now();
    }

    public Movie getMovie() {
        return movie;
    }

    public Integer getProgressSeconds() {
        return progressSeconds;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void update(Integer progressSeconds, boolean completed) {
        this.progressSeconds = progressSeconds;
        this.completed = completed;
    }
}
