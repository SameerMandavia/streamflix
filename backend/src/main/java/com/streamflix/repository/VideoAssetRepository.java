package com.streamflix.repository;
import com.streamflix.entity.VideoAsset;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface VideoAssetRepository extends JpaRepository<VideoAsset, Long> { Optional<VideoAsset> findByMovieId(Long movieId); }
