package com.streamflix.dto;

import com.streamflix.entity.Movie;

public record MovieResponse(Long id, String title, String description, Integer releaseYear,
                            Integer durationMinutes, String genre, String posterUrl, boolean published) {
    public static MovieResponse from(Movie movie) {
        return new MovieResponse(movie.getId(), movie.getTitle(), movie.getDescription(), movie.getReleaseYear(),
                movie.getDurationMinutes(), movie.getGenre(), movie.getPosterUrl(), movie.isPublished());
    }
}
