package com.streamflix.controller;

import com.streamflix.dto.*;
import com.streamflix.service.AdminService;
import com.streamflix.service.MediaStorageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController @RequestMapping("/api/admin") @PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AdminService admin; private final MediaStorageService media;
    public AdminController(AdminService admin, MediaStorageService media) { this.admin = admin; this.media = media; }
    @GetMapping("/analytics") public AdminDtos.Analytics analytics() { return admin.analytics(); }
    @GetMapping("/movies") public List<MovieResponse> movies() { return admin.movies(); }
    @PostMapping("/movies") public MovieResponse createMovie(@Valid @RequestBody AdminDtos.MovieRequest request) { return admin.createMovie(request); }
    @PutMapping("/movies/{id}") public MovieResponse updateMovie(@PathVariable Long id, @Valid @RequestBody AdminDtos.MovieRequest request) { return admin.updateMovie(id, request); }
    @PatchMapping("/movies/{id}/published") public MovieResponse publishMovie(@PathVariable Long id, @RequestParam boolean value) { return admin.publishMovie(id, value); }
    @PostMapping(value = "/movies/{id}/poster", consumes = MediaType.MULTIPART_FORM_DATA_VALUE) public MovieResponse poster(@PathVariable Long id, @RequestPart MultipartFile file, HttpServletRequest request) { media.uploadPoster(id, file, baseUrl(request)); var movie = admin.currentMovie(id); return admin.updateMovie(id, new AdminDtos.MovieRequest(movie.getTitle(), movie.getDescription(), movie.getReleaseYear(), movie.getDurationMinutes(), movie.getGenre(), movie.getPosterUrl())); }
    @GetMapping("/series") public List<FeatureDtos.SeriesResponse> series() { return admin.allSeries(); }
    @PostMapping("/series") public FeatureDtos.SeriesResponse createSeries(@Valid @RequestBody AdminDtos.SeriesRequest request) { return admin.createSeries(request); }
    @PutMapping("/series/{id}") public FeatureDtos.SeriesResponse updateSeries(@PathVariable Long id, @Valid @RequestBody AdminDtos.SeriesRequest request) { return admin.updateSeries(id, request); }
    @PatchMapping("/series/{id}/published") public FeatureDtos.SeriesResponse publishSeries(@PathVariable Long id, @RequestParam boolean value) { return admin.publishSeries(id, value); }
    @PostMapping("/seasons/{seasonId}/episodes") public FeatureDtos.EpisodeResponse createEpisode(@PathVariable Long seasonId, @Valid @RequestBody AdminDtos.EpisodeRequest request) { return admin.createEpisode(seasonId, request); }
    @GetMapping("/users") public List<AdminDtos.UserSummary> users() { return admin.users(); }
    @GetMapping("/subscriptions") public List<AdminDtos.SubscriptionSummary> subscriptions() { return admin.subscriptions(); }
    private String baseUrl(HttpServletRequest request) { return request.getScheme() + "://" + request.getServerName() + (request.getServerPort() == 80 || request.getServerPort() == 443 ? "" : ":" + request.getServerPort()); }
}
