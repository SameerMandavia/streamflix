package com.streamflix.service;

import com.streamflix.dto.MovieResponse;
import com.streamflix.repository.MovieRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MovieService {
    private final MovieRepository repository;
    public MovieService(MovieRepository repository) { this.repository = repository; }
    public List<MovieResponse> all() { return repository.findAll().stream().map(MovieResponse::from).toList(); }
    public List<MovieResponse> trending() { return repository.findTop10ByOrderByCreatedAtDesc().stream().map(MovieResponse::from).toList(); }
    public List<MovieResponse> search(String query) { return repository.findByTitleContainingIgnoreCaseOrGenreContainingIgnoreCase(query, query).stream().map(MovieResponse::from).toList(); }
    public MovieResponse byId(Long id) { return repository.findById(id).map(MovieResponse::from).orElseThrow(() -> new IllegalArgumentException("Movie not found: " + id)); }
}
