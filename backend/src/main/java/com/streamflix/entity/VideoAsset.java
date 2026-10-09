package com.streamflix.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity @Table(name = "video_assets")
public class VideoAsset {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @OneToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "movie_id", nullable = false, unique = true) private Movie movie;
    @Column(name = "source_path", nullable = false) private String sourcePath;
    @Column(name = "hls_path") private String hlsPath;
    @Column(nullable = false) private String status;
    @Column(name = "error_message") private String errorMessage;
    @Column(nullable = false, updatable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;
    protected VideoAsset() {}
    public VideoAsset(Movie movie, String sourcePath) { this.movie = movie; this.sourcePath = sourcePath; this.status = "UPLOADED"; }
    @PrePersist @PreUpdate void touch() { if (createdAt == null) createdAt = Instant.now(); updatedAt = Instant.now(); }
    public Long getId() { return id; } public Movie getMovie() { return movie; } public String getSourcePath() { return sourcePath; }
    public String getHlsPath() { return hlsPath; } public String getStatus() { return status; } public String getErrorMessage() { return errorMessage; }
    public void processing() { status = "PROCESSING"; errorMessage = null; } public void ready(String path) { status = "READY"; hlsPath = path; errorMessage = null; }
    public void failed(String error) { status = "FAILED"; errorMessage = error; }
}
