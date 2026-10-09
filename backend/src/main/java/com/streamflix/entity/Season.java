package com.streamflix.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "seasons")
public class Season {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "series_id")
    private Series series;
    @Column(name = "season_number", nullable = false)
    private Integer seasonNumber;
    @Column(nullable = false)
    private String title;

    protected Season() {
    }
    public Season(Series series, Integer seasonNumber, String title) { this.series = series; this.seasonNumber = seasonNumber; this.title = title; }

    public Long getId() {
        return id;
    }

    public Integer getSeasonNumber() {
        return seasonNumber;
    }

    public String getTitle() {
        return title;
    }
    public Series getSeries() { return series; }
    public void update(Integer seasonNumber, String title) { this.seasonNumber = seasonNumber; this.title = title; }
}
