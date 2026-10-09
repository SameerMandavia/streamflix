package com.streamflix.controller;

import com.streamflix.dto.AuthDtos;
import com.streamflix.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {
    private final AuthService service;
    public ProfileController(AuthService service) { this.service = service; }
    @GetMapping public List<AuthDtos.ProfileResponse> profiles(Authentication authentication) { return service.profiles(service.user(authentication.getName())); }
    @PostMapping public AuthDtos.ProfileResponse create(Authentication authentication, @Valid @RequestBody AuthDtos.CreateProfileRequest request) { return service.createProfile(service.user(authentication.getName()), request); }
}
