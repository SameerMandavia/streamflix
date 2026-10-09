package com.streamflix.service;

import com.streamflix.dto.*;
import com.streamflix.entity.*;
import com.streamflix.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class UserFeatureService {
    private final AuthService auth;
    private final MovieRepository movies;
    private final WatchlistRepository watchlist;
    private final WatchHistoryRepository history;

    public UserFeatureService(AuthService auth, MovieRepository movies, WatchlistRepository watchlist,
            WatchHistoryRepository history) {
        this.auth = auth;
        this.movies = movies;
        this.watchlist = watchlist;
        this.history = history;
    }

    public List<MovieResponse> list(User user) {
        return watchlist.findByUserIdOrderByIdDesc(user.getId()).stream()
                .map(item -> MovieResponse.from(item.getMovie())).toList();
    }

    public void add(User user, Long movieId) {
        if (watchlist.findByUserIdAndMovieId(user.getId(), movieId).isPresent())
            return;
        watchlist.save(new WatchlistItem(user, movie(movieId)));
    }

    public void remove(User user, Long movieId) {
        watchlist.findByUserIdAndMovieId(user.getId(), movieId).ifPresent(watchlist::delete);
    }

    public List<FeatureDtos.WatchProgressResponse> continueWatching(User user) {
        return history.findByUserIdAndCompletedFalseOrderByLastWatchedAtDesc(user.getId()).stream().map(this::progress)
                .toList();
    }

    public List<FeatureDtos.WatchProgressResponse> history(User user) {
        return history.findByUserIdOrderByLastWatchedAtDesc(user.getId()).stream().map(this::progress).toList();
    }

    public FeatureDtos.WatchProgressResponse record(User user, FeatureDtos.WatchProgressRequest request) {
        if (request.movieId() == null)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "movieId is required");
        var item = history.findByUserIdAndMovieId(user.getId(), request.movieId())
                .orElseGet(() -> new WatchHistory(user, movie(request.movieId()), 0, false));
        item.update(Math.max(0, request.progressSeconds() == null ? 0 : request.progressSeconds()),
                Boolean.TRUE.equals(request.completed()));
        return progress(history.save(item));
    }

    private FeatureDtos.WatchProgressResponse progress(WatchHistory item) {
        return new FeatureDtos.WatchProgressResponse(MovieResponse.from(item.getMovie()), item.getProgressSeconds(),
                item.isCompleted());
    }

    private Movie movie(Long id) {
        return movies.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Movie not found"));
    }
}
