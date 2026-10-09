package com.streamflix.repository;

import com.streamflix.entity.WatchHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface WatchHistoryRepository extends JpaRepository<WatchHistory, Long> {
    @Query("select h from WatchHistory h join fetch h.movie where h.user.id = :userId and h.completed = false order by h.lastWatchedAt desc")
    List<WatchHistory> findByUserIdAndCompletedFalseOrderByLastWatchedAtDesc(@Param("userId") Long userId);

    @Query("select h from WatchHistory h join fetch h.movie where h.user.id = :userId order by h.lastWatchedAt desc")
    List<WatchHistory> findByUserIdOrderByLastWatchedAtDesc(@Param("userId") Long userId);

    Optional<WatchHistory> findByUserIdAndMovieId(Long userId, Long movieId);
}
