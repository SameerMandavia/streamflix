package com.streamflix.service;

import com.streamflix.entity.Movie;
import com.streamflix.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MovieServiceTest {
    @Mock MovieRepository repository;
    @InjectMocks MovieService service;

    @Test
    void publicCatalogueExcludesUnpublishedMovies() {
        Movie published = new Movie("Published", "Description", 2026, 100, "Drama", "/published.jpg");
        Movie draft = new Movie("Draft", "Description", 2026, 100, "Drama", "/draft.jpg");
        draft.setPublished(false);
        when(repository.findAll()).thenReturn(List.of(published, draft));

        assertEquals(List.of("Published"), service.all().stream().map(response -> response.title()).toList());
    }
}
