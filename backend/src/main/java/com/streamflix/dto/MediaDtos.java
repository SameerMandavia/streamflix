package com.streamflix.dto;
import java.util.List;
public final class MediaDtos {
    private MediaDtos() {}
    public record VideoAssetResponse(Long movieId, String status, String errorMessage) {}
    public record SubtitleResponse(String languageCode, String label, String url) {}
    public record PlaybackResponse(String manifestUrl, List<SubtitleResponse> subtitles) {}
}
