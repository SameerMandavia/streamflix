package com.streamflix.service;

import com.streamflix.dto.MovieResponse;
import com.streamflix.entity.Movie;
import com.streamflix.repository.MovieRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MovieService {
    private final MovieRepository repository;

    public MovieService(MovieRepository repository) {
        this.repository = repository;
    }

    public List<MovieResponse> all() {
        return repository.findAll().stream().filter(Movie::isPublished).map(MovieResponse::from).toList();
    }

    public List<MovieResponse> trending() {
        return repository.findTop10ByOrderByCreatedAtDesc().stream().filter(Movie::isPublished).map(MovieResponse::from).toList();
    }

    public List<MovieResponse> search(String query, String genre) {
        var movies = genre == null || genre.isBlank()
                ? repository.findByTitleContainingIgnoreCaseOrGenreContainingIgnoreCase(query, query)
                : query == null || query.isBlank() ? repository.findByGenreIgnoreCase(genre)
                        : repository.findByTitleContainingIgnoreCaseAndGenreContainingIgnoreCase(query, genre);
        return movies.stream().filter(Movie::isPublished).map(MovieResponse::from).toList();
    }

    public MovieResponse byId(Long id) {
        return repository.findById(id).filter(Movie::isPublished).map(MovieResponse::from)
                .orElseThrow(() -> new IllegalArgumentException("Movie not found: " + id));
    }
}
