package com.streamflix.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

public final class AuthDtos {
    private AuthDtos() {}
    public record RegisterRequest(@NotBlank @Email String email, @NotBlank @Size(min = 8, max = 72) String password, @NotBlank @Size(min = 2, max = 80) String profileName) {}
    public record LoginRequest(@NotBlank @Email String email, @NotBlank String password) {}
    public record ProfileResponse(Long id, String name, String avatarKey) {}
    public record AuthResponse(String token, String email, String role, List<ProfileResponse> profiles) {}
    public record CreateProfileRequest(@NotBlank @Size(min = 2, max = 80) String name, @Size(max = 40) String avatarKey) {}
}
