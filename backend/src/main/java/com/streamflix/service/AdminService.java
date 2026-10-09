package com.streamflix.service;

import com.streamflix.dto.*;
import com.streamflix.entity.*;
import com.streamflix.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service @Transactional
public class AdminService {
    private final MovieRepository movies; private final SeriesRepository series; private final SeasonRepository seasons; private final EpisodeRepository episodes; private final UserRepository users; private final SubscriptionRepository subscriptions;
    public AdminService(MovieRepository movies, SeriesRepository series, SeasonRepository seasons, EpisodeRepository episodes, UserRepository users, SubscriptionRepository subscriptions) { this.movies = movies; this.series = series; this.seasons = seasons; this.episodes = episodes; this.users = users; this.subscriptions = subscriptions; }
    public List<MovieResponse> movies() { return movies.findAll().stream().map(MovieResponse::from).toList(); }
    public MovieResponse createMovie(AdminDtos.MovieRequest r) { return MovieResponse.from(movies.save(new Movie(r.title(), r.description(), r.releaseYear(), r.durationMinutes(), r.genre(), r.posterUrl()))); }
    public MovieResponse updateMovie(Long id, AdminDtos.MovieRequest r) { Movie m = movie(id); m.update(r.title(), r.description(), r.releaseYear(), r.durationMinutes(), r.genre(), r.posterUrl()); return MovieResponse.from(m); }
    public Movie currentMovie(Long id) { return movie(id); }
    public MovieResponse publishMovie(Long id, boolean published) { Movie m = movie(id); m.setPublished(published); return MovieResponse.from(m); }
    public List<FeatureDtos.SeriesResponse> allSeries() { return series.findAll().stream().map(this::seriesResponse).toList(); }
    public FeatureDtos.SeriesResponse createSeries(AdminDtos.SeriesRequest r) { Series s = series.save(new Series(r.title(), r.description(), r.releaseYear(), r.genre(), r.posterUrl())); return seriesResponse(s); }
    public FeatureDtos.SeriesResponse updateSeries(Long id, AdminDtos.SeriesRequest r) { Series s = series.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Series not found")); s.update(r.title(), r.description(), r.releaseYear(), r.genre(), r.posterUrl()); return seriesResponse(s); }
    public FeatureDtos.SeriesResponse publishSeries(Long id, boolean published) { Series s = series.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Series not found")); s.setPublished(published); return seriesResponse(s); }
    public FeatureDtos.EpisodeResponse createEpisode(Long seasonId, AdminDtos.EpisodeRequest r) { Season season = seasons.findById(seasonId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Season not found")); return FeatureDtos.episode(episodes.save(new Episode(season, r.episodeNumber(), r.title(), r.description(), r.durationMinutes()))); }
    public List<AdminDtos.UserSummary> users() { return users.findAll().stream().map(u -> new AdminDtos.UserSummary(u.getId(), u.getEmail(), u.getRole(), u.getCreatedAt())).toList(); }
    public List<AdminDtos.SubscriptionSummary> subscriptions() { return subscriptions.findAll().stream().map(s -> new AdminDtos.SubscriptionSummary(s.getUser().getEmail(), s.getPlan().getCode(), s.getStatus(), s.getCurrentPeriodEnd(), s.isCancelAtPeriodEnd())).toList(); }
    public AdminDtos.Analytics analytics() { return new AdminDtos.Analytics(movies.count(), series.count(), users.count(), users.countByRole("ADMIN"), subscriptions.count(), subscriptions.countByStatus("active")); }
    private FeatureDtos.SeriesResponse seriesResponse(Series s) { List<FeatureDtos.SeasonResponse> seasonsResponse = seasons.findBySeriesIdOrderBySeasonNumber(s.getId()).stream().map(season -> new FeatureDtos.SeasonResponse(season.getId(), season.getSeasonNumber(), season.getTitle(), episodes.findBySeasonIdOrderByEpisodeNumber(season.getId()).stream().map(FeatureDtos::episode).toList())).toList(); return new FeatureDtos.SeriesResponse(s.getId(), s.getTitle(), s.getDescription(), s.getReleaseYear(), s.getGenre(), s.getPosterUrl(), seasonsResponse, s.isPublished()); }
    private Movie movie(Long id) { return movies.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found")); }
}
