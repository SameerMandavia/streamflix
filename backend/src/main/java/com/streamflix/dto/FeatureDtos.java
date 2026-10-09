package com.streamflix.dto;

import com.streamflix.entity.*;
import java.util.List;

public final class FeatureDtos {
    private FeatureDtos() {
    }

    public record WatchProgressRequest(Long movieId, Integer progressSeconds, Boolean completed) {
    }

    public record WatchProgressResponse(MovieResponse movie, Integer progressSeconds, boolean completed) {
    }

    public record EpisodeResponse(Long id, Integer episodeNumber, String title, String description,
            Integer durationMinutes) {
    }

    public record SeasonResponse(Long id, Integer seasonNumber, String title, List<EpisodeResponse> episodes) {
    }

    public record SeriesResponse(Long id, String title, String description, Integer releaseYear, String genre,
            String posterUrl, List<SeasonResponse> seasons, boolean published) {
    }

    public static EpisodeResponse episode(Episode e) {
        return new EpisodeResponse(e.getId(), e.getEpisodeNumber(), e.getTitle(), e.getDescription(),
                e.getDurationMinutes());
    }
}
