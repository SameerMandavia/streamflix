package com.streamflix.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "movies")
public class Movie {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private String title;
    @Column(nullable = false, length = 2000) private String description;
    @Column(nullable = false) private Integer releaseYear;
    @Column(nullable = false) private Integer durationMinutes;
    @Column(nullable = false) private String genre;
    @Column(nullable = false) private String posterUrl;
    @Column(nullable = false) private boolean published = true;
    @Column(nullable = false, updatable = false) private Instant createdAt;

    protected Movie() {}
    public Movie(String title, String description, Integer releaseYear, Integer durationMinutes, String genre, String posterUrl) {
        this.title = title; this.description = description; this.releaseYear = releaseYear;
        this.durationMinutes = durationMinutes; this.genre = genre; this.posterUrl = posterUrl;
    }
    @PrePersist void prePersist() { if (createdAt == null) createdAt = Instant.now(); }
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public Integer getReleaseYear() { return releaseYear; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public String getGenre() { return genre; }
    public String getPosterUrl() { return posterUrl; }
    public boolean isPublished() { return published; }
    public void update(String title, String description, Integer releaseYear, Integer durationMinutes, String genre, String posterUrl) { this.title = title; this.description = description; this.releaseYear = releaseYear; this.durationMinutes = durationMinutes; this.genre = genre; if (posterUrl != null && !posterUrl.isBlank()) this.posterUrl = posterUrl; }
    public void setPublished(boolean published) { this.published = published; }
}
