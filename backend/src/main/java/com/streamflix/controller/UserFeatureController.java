package com.streamflix.controller;

import com.streamflix.dto.*;
import com.streamflix.service.*;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/me")
public class UserFeatureController {
    private final AuthService auth;
    private final UserFeatureService features;

    public UserFeatureController(AuthService auth, UserFeatureService features) {
        this.auth = auth;
        this.features = features;
    }

    @GetMapping("/watchlist")
    public List<MovieResponse> watchlist(Authentication a) {
        return features.list(auth.user(a.getName()));
    }

    @PostMapping("/watchlist/{movieId}")
    public void add(Authentication a, @PathVariable Long movieId) {
        features.add(auth.user(a.getName()), movieId);
    }

    @DeleteMapping("/watchlist/{movieId}")
    public void remove(Authentication a, @PathVariable Long movieId) {
        features.remove(auth.user(a.getName()), movieId);
    }

    @GetMapping("/continue-watching")
    public List<FeatureDtos.WatchProgressResponse> continueWatching(Authentication a) {
        return features.continueWatching(auth.user(a.getName()));
    }

    @GetMapping("/history")
    public List<FeatureDtos.WatchProgressResponse> history(Authentication a) {
        return features.history(auth.user(a.getName()));
    }

    @PostMapping("/history")
    public FeatureDtos.WatchProgressResponse record(Authentication a,
            @Valid @RequestBody FeatureDtos.WatchProgressRequest request) {
        return features.record(auth.user(a.getName()), request);
    }
}
