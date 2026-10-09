package com.streamflix.repository;

import com.streamflix.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    List<Movie> findTop10ByOrderByCreatedAtDesc();

    List<Movie> findByTitleContainingIgnoreCaseOrGenreContainingIgnoreCase(String title, String genre);

    List<Movie> findByTitleContainingIgnoreCaseAndGenreContainingIgnoreCase(String title, String genre);

    List<Movie> findByGenreIgnoreCase(String genre);
}
