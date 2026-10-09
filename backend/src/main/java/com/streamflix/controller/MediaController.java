package com.streamflix.controller;

import com.streamflix.dto.MediaDtos;
import com.streamflix.service.MediaStorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController @RequestMapping("/api/media/movies")
public class MediaController {
    private final MediaStorageService media;
    public MediaController(MediaStorageService media) { this.media = media; }
    @PostMapping("/{movieId}/upload") @PreAuthorize("isAuthenticated()") public MediaDtos.VideoAssetResponse upload(@PathVariable Long movieId, @RequestPart MultipartFile file) { return media.upload(movieId, file); }
    @PostMapping("/{movieId}/subtitles") @PreAuthorize("isAuthenticated()") public MediaDtos.SubtitleResponse subtitle(@PathVariable Long movieId, @RequestParam String language, @RequestParam(required = false) String label, @RequestPart MultipartFile file, HttpServletRequest request) { return media.uploadSubtitle(movieId, language, label, file, baseUrl(request)); }
    @GetMapping("/{movieId}/playback-token") @PreAuthorize("isAuthenticated()") public MediaDtos.PlaybackResponse playback(@PathVariable Long movieId, Authentication authentication, HttpServletRequest request) { return media.playback(movieId, authentication.getName(), baseUrl(request)); }
    @GetMapping("/{movieId}/hls/{*path}") public ResponseEntity<?> hls(@PathVariable Long movieId, @PathVariable String path, @RequestParam String token) { return media.hls(movieId, path, token); }
    @GetMapping("/{movieId}/subtitles/{language}") public ResponseEntity<?> subtitles(@PathVariable Long movieId, @PathVariable String language, @RequestParam String token) { return media.subtitle(movieId, language, token); }
    private String baseUrl(HttpServletRequest request) { return request.getScheme() + "://" + request.getServerName() + (request.getServerPort() == 80 || request.getServerPort() == 443 ? "" : ":" + request.getServerPort()); }
}
