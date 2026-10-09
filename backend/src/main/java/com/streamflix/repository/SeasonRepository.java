package com.streamflix.repository;

import com.streamflix.entity.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SeasonRepository extends JpaRepository<Season, Long> {
    List<Season> findBySeriesIdOrderBySeasonNumber(Long seriesId);
}
