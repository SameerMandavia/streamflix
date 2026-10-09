package com.streamflix.service;

import org.junit.jupiter.api.Test;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class StripeSignatureVerifierTest {
    @Test
    void acceptsARecentValidSignature() throws Exception {
        String payload = "{\"id\":\"evt_test\"}";
        String secret = "whsec_test";
        long timestamp = System.currentTimeMillis() / 1000;
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
        byte[] digest = mac.doFinal((timestamp + "." + payload).getBytes(StandardCharsets.UTF_8));
        StringBuilder signature = new StringBuilder();
        for (byte value : digest) signature.append(String.format("%02x", value));

        assertTrue(StripeSignatureVerifier.verify(payload, "t=" + timestamp + ",v1=" + signature, secret, 300));
    }

    @Test
    void rejectsAnInvalidOrExpiredSignature() {
        assertFalse(StripeSignatureVerifier.verify("payload", "t=1,v1=bad", "secret", 300));
        assertFalse(StripeSignatureVerifier.verify("payload", "t=1,v1=bad", "secret", 0));
    }
}
