package com.streamflix.entity;

import jakarta.persistence.*;

@Entity @Table(name = "subtitles", uniqueConstraints = @UniqueConstraint(name = "uq_subtitle_asset_language", columnNames = {"video_asset_id", "language_code"}))
public class Subtitle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "video_asset_id") private VideoAsset videoAsset;
    @Column(name = "language_code", nullable = false) private String languageCode;
    @Column(nullable = false) private String label;
    @Column(name = "file_path", nullable = false) private String filePath;
    protected Subtitle() {}
    public Subtitle(VideoAsset videoAsset, String languageCode, String label, String filePath) { this.videoAsset = videoAsset; this.languageCode = languageCode; this.label = label; this.filePath = filePath; }
    public Long getId() { return id; } public String getLanguageCode() { return languageCode; } public String getLabel() { return label; } public String getFilePath() { return filePath; }
}
