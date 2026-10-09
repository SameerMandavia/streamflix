package com.streamflix.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    private final JwtService jwt = new JwtService("a-local-test-secret-with-at-least-32-chars", 60_000);

    @Test
    void issuesAndValidatesUserTokens() {
        String token = jwt.issue("user@example.com");
        assertTrue(jwt.isValid(token));
        assertEquals("user@example.com", jwt.subject(token));
        assertFalse(jwt.isPlaybackToken(token));
    }

    @Test
    void scopesPlaybackTokensToTheirMovie() {
        String token = jwt.issuePlaybackToken("user@example.com", 42L);
        assertTrue(jwt.isValid(token));
        assertTrue(jwt.isPlaybackTokenFor(token, 42L));
        assertFalse(jwt.isPlaybackTokenFor(token, 43L));
    }
}
