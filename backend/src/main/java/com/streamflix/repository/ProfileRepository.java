package com.streamflix.repository;

import com.streamflix.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProfileRepository extends JpaRepository<Profile, Long> {
    List<Profile> findByUserIdOrderById(Long userId);
    boolean existsByUserIdAndNameIgnoreCase(Long userId, String name);
}
