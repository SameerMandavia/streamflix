package com.streamflix.dto;
import java.time.Instant;
public final class AdminDtos {
    private AdminDtos() {}
    public record MovieRequest(String title, String description, Integer releaseYear, Integer durationMinutes, String genre, String posterUrl) {}
    public record SeriesRequest(String title, String description, Integer releaseYear, String genre, String posterUrl) {}
    public record SeasonRequest(Integer seasonNumber, String title) {}
    public record EpisodeRequest(Integer episodeNumber, String title, String description, Integer durationMinutes) {}
    public record UserSummary(Long id, String email, String role, Instant createdAt) {}
    public record SubscriptionSummary(String email, String planCode, String status, Instant currentPeriodEnd, boolean cancelAtPeriodEnd) {}
    public record Analytics(long movies, long series, long users, long admins, long subscriptions, long activeSubscriptions) {}
}
