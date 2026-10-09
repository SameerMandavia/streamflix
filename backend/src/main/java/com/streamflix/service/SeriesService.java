package com.streamflix.service;

import com.streamflix.dto.FeatureDtos;
import com.streamflix.entity.Series;
import com.streamflix.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class SeriesService {
    private final SeriesRepository series;
    private final SeasonRepository seasons;
    private final EpisodeRepository episodes;

    public SeriesService(SeriesRepository series, SeasonRepository seasons, EpisodeRepository episodes) {
        this.series = series;
        this.seasons = seasons;
        this.episodes = episodes;
    }

    public List<FeatureDtos.SeriesResponse> all() {
        return series.findAll().stream().map(this::response).toList();
    }

    public FeatureDtos.SeriesResponse byId(Long id) {
        return series.findById(id).map(this::response)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Series not found"));
    }

    private FeatureDtos.SeriesResponse response(Series s) {
        var seasonResponses = seasons.findBySeriesIdOrderBySeasonNumber(s.getId()).stream()
                .map(season -> new FeatureDtos.SeasonResponse(season.getId(), season.getSeasonNumber(),
                        season.getTitle(), episodes.findBySeasonIdOrderByEpisodeNumber(season.getId()).stream()
                                .map(FeatureDtos::episode).toList()))
                .toList();
        return new FeatureDtos.SeriesResponse(s.getId(), s.getTitle(), s.getDescription(), s.getReleaseYear(),
                s.getGenre(), s.getPosterUrl(), seasonResponses);
    }
}
