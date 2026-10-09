package com.streamflix.controller;

import com.streamflix.dto.AuthDtos;
import com.streamflix.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService service;
    public AuthController(AuthService service) { this.service = service; }
    @PostMapping("/register") public AuthDtos.AuthResponse register(@Valid @RequestBody AuthDtos.RegisterRequest request) { return service.register(request); }
    @PostMapping("/login") public AuthDtos.AuthResponse login(@Valid @RequestBody AuthDtos.LoginRequest request) { return service.login(request); }
}
