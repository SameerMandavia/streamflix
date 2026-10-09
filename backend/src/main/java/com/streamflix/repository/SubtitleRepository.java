package com.streamflix.repository;
import com.streamflix.entity.Subtitle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface SubtitleRepository extends JpaRepository<Subtitle, Long> { List<Subtitle> findByVideoAssetIdOrderByLanguageCode(Long videoAssetId); }
