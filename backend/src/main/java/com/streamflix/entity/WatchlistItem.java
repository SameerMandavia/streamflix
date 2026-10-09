package com.streamflix.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "watchlist_items", uniqueConstraints = @UniqueConstraint(name = "uq_watchlist_user_movie", columnNames = {
        "user_id", "movie_id" }))
public class WatchlistItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movie_id")
    private Movie movie;
    @Column(nullable = false, updatable = false)
    private Instant addedAt;

    protected WatchlistItem() {
    }

    public WatchlistItem(User user, Movie movie) {
        this.user = user;
        this.movie = movie;
    }

    @PrePersist
    void prePersist() {
        if (addedAt == null)
            addedAt = Instant.now();
    }

    public Movie getMovie() {
        return movie;
    }
}
