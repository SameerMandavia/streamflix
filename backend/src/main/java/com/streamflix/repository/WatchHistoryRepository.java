package com.streamflix.repository;

import com.streamflix.entity.WatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WatchHistoryRepository extends JpaRepository<WatchHistory, Long> {
    List<WatchHistory> findByUserIdAndCompletedFalseOrderByLastWatchedAtDesc(Long userId);

    List<WatchHistory> findByUserIdOrderByLastWatchedAtDesc(Long userId);

    Optional<WatchHistory> findByUserIdAndMovieId(Long userId, Long movieId);
}
