package com.streamflix.service;

import com.streamflix.dto.AuthDtos;
import com.streamflix.entity.Profile;
import com.streamflix.entity.User;
import com.streamflix.repository.ProfileRepository;
import com.streamflix.repository.UserRepository;
import com.streamflix.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class AuthService {
    private final UserRepository users; private final ProfileRepository profiles; private final PasswordEncoder encoder; private final JwtService jwt;
    public AuthService(UserRepository users, ProfileRepository profiles, PasswordEncoder encoder, JwtService jwt) { this.users = users; this.profiles = profiles; this.encoder = encoder; this.jwt = jwt; }
    public AuthDtos.AuthResponse register(AuthDtos.RegisterRequest request) {
        String email = request.email().trim().toLowerCase();
        if (users.existsByEmailIgnoreCase(email)) throw new ResponseStatusException(HttpStatus.CONFLICT, "An account with that email already exists");
        User user = users.save(new User(email, encoder.encode(request.password())));
        profiles.save(new Profile(user, request.profileName().trim(), "sunset"));
        return response(user);
    }
    public AuthDtos.AuthResponse login(AuthDtos.LoginRequest request) {
        User user = users.findByEmailIgnoreCase(request.email().trim()).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
        if (!encoder.matches(request.password(), user.getPasswordHash())) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        return response(user);
    }
    public User user(String email) { return users.findByEmailIgnoreCase(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found")); }
    public AuthDtos.ProfileResponse profile(Profile profile) { return new AuthDtos.ProfileResponse(profile.getId(), profile.getName(), profile.getAvatarKey()); }
    public AuthDtos.AuthResponse response(User user) { return new AuthDtos.AuthResponse(jwt.issue(user.getEmail()), user.getEmail(), user.getRole(), profiles.findByUserIdOrderById(user.getId()).stream().map(this::profile).toList()); }
    public List<AuthDtos.ProfileResponse> profiles(User user) { return profiles.findByUserIdOrderById(user.getId()).stream().map(this::profile).toList(); }
    public AuthDtos.ProfileResponse createProfile(User user, AuthDtos.CreateProfileRequest request) {
        if (profiles.existsByUserIdAndNameIgnoreCase(user.getId(), request.name().trim())) throw new ResponseStatusException(HttpStatus.CONFLICT, "That profile name already exists");
        return profile(profiles.save(new Profile(user, request.name().trim(), request.avatarKey() == null || request.avatarKey().isBlank() ? "sunset" : request.avatarKey().trim())));
    }
}
