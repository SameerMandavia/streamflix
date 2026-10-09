package com.streamflix.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey key;
    private final long expirationMs;
    public JwtService(@Value("${app.jwt.secret}") String secret, @Value("${app.jwt.expiration-ms:86400000}") long expirationMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)); this.expirationMs = expirationMs;
    }
    public String issue(String email) { Date now = new Date(); return Jwts.builder().subject(email).issuedAt(now).expiration(new Date(now.getTime() + expirationMs)).signWith(key).compact(); }
    public String issuePlaybackToken(String email, Long movieId) { Date now = new Date(); return Jwts.builder().subject(email).claim("tokenType", "playback").claim("movieId", movieId).issuedAt(now).expiration(new Date(now.getTime() + 300000)).signWith(key).compact(); }
    public String subject(String token) { return claims(token).getSubject(); }
    public boolean isValid(String token) { try { return claims(token).getExpiration().after(new Date()); } catch (Exception ex) { return false; } }
    public boolean isPlaybackToken(String token) { try { return "playback".equals(claims(token).get("tokenType", String.class)); } catch (Exception ex) { return false; } }
    public boolean isPlaybackTokenFor(String token, Long movieId) { try { return isPlaybackToken(token) && movieId.equals(claims(token).get("movieId", Long.class)); } catch (Exception ex) { return false; } }
    private Claims claims(String token) { return Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload(); }
}
