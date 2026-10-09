package com.streamflix.repository;

import com.streamflix.entity.WatchlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WatchlistRepository extends JpaRepository<WatchlistItem, Long> {
    List<WatchlistItem> findByUserIdOrderByIdDesc(Long userId);

    Optional<WatchlistItem> findByUserIdAndMovieId(Long userId, Long movieId);
}
