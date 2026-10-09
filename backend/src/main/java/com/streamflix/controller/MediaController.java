package com.streamflix.controller;

import com.streamflix.dto.MediaDtos;
import com.streamflix.service.MediaStorageService;
import com.streamflix.service.AuthService;
import com.streamflix.service.SubscriptionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController @RequestMapping("/api/media/movies")
public class MediaController {
    private final MediaStorageService media; private final AuthService auth; private final SubscriptionService subscriptions;
    public MediaController(MediaStorageService media, AuthService auth, SubscriptionService subscriptions) { this.media = media; this.auth = auth; this.subscriptions = subscriptions; }
    @PostMapping("/{movieId}/upload") @PreAuthorize("hasRole('ADMIN')") public MediaDtos.VideoAssetResponse upload(@PathVariable Long movieId, @RequestPart MultipartFile file) { return media.upload(movieId, file); }
    @PostMapping("/{movieId}/subtitles") @PreAuthorize("hasRole('ADMIN')") public MediaDtos.SubtitleResponse subtitle(@PathVariable Long movieId, @RequestParam String language, @RequestParam(required = false) String label, @RequestPart MultipartFile file, HttpServletRequest request) { return media.uploadSubtitle(movieId, language, label, file, baseUrl(request)); }
    @GetMapping("/posters/{filename:.+}") public ResponseEntity<?> poster(@PathVariable String filename) { return media.poster(filename); }
    @GetMapping("/{movieId}/playback-token") @PreAuthorize("isAuthenticated()") public MediaDtos.PlaybackResponse playback(@PathVariable Long movieId, Authentication authentication, HttpServletRequest request) { if (!subscriptions.hasPremium(auth.user(authentication.getName()))) throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.PAYMENT_REQUIRED, "An active subscription is required for playback"); return media.playback(movieId, authentication.getName(), baseUrl(request)); }
    @GetMapping("/{movieId}/hls/{*path}") public ResponseEntity<?> hls(@PathVariable Long movieId, @PathVariable String path, @RequestParam String token) { return media.hls(movieId, path, token); }
    @GetMapping("/{movieId}/subtitles/{language}") public ResponseEntity<?> subtitles(@PathVariable Long movieId, @PathVariable String language, @RequestParam String token) { return media.subtitle(movieId, language, token); }
    private String baseUrl(HttpServletRequest request) { return request.getScheme() + "://" + request.getServerName() + (request.getServerPort() == 80 || request.getServerPort() == 443 ? "" : ":" + request.getServerPort()); }
}
