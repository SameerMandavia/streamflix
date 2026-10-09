package com.streamflix.controller;

import com.streamflix.dto.MovieResponse;
import com.streamflix.service.MovieService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/movies")
@CrossOrigin(origins = "http://localhost:4200")
public class MovieController {
    private final MovieService service;

    public MovieController(MovieService service) {
        this.service = service;
    }

    @GetMapping
    public List<MovieResponse> all() {
        return service.all();
    }

    @GetMapping("/trending")
    public List<MovieResponse> trending() {
        return service.trending();
    }

    @GetMapping("/popular")
    public List<MovieResponse> popular() {
        return service.trending();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieResponse> byId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(service.byId(id));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    public List<MovieResponse> search(@RequestParam(required = false, defaultValue = "") String q,
            @RequestParam(required = false) String genre) {
        return service.search(q, genre);
    }
}
