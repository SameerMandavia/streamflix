package com.streamflix.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "episodes")
public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "season_id")
    private Season season;
    @Column(name = "episode_number", nullable = false)
    private Integer episodeNumber;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false, length = 2000)
    private String description;
    @Column(name = "duration_minutes", nullable = false)
    private Integer durationMinutes;

    protected Episode() {
    }

    public Long getId() {
        return id;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Integer getDurationMinutes() {
        return durationMinutes;
    }
}
