package com.streamflix.controller;

import com.streamflix.dto.FeatureDtos;
import com.streamflix.service.SeriesService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/series")
public class SeriesController {
    private final SeriesService service;

    public SeriesController(SeriesService service) {
        this.service = service;
    }

    @GetMapping
    public List<FeatureDtos.SeriesResponse> all() {
        return service.all();
    }

    @GetMapping("/{id}")
    public FeatureDtos.SeriesResponse byId(@PathVariable Long id) {
        return service.byId(id);
    }
}
