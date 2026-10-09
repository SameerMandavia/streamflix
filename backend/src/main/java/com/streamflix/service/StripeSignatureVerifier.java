package com.streamflix.service;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
public final class StripeSignatureVerifier {
    private StripeSignatureVerifier() {}
    public static boolean verify(String payload, String header, String secret, long toleranceSeconds) {
        try { long timestamp = Long.parseLong(value(header, "t")); if (Math.abs(System.currentTimeMillis() / 1000 - timestamp) > toleranceSeconds) return false; String signed = timestamp + "." + payload; Mac mac = Mac.getInstance("HmacSHA256"); mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256")); String expected = hex(mac.doFinal(signed.getBytes(StandardCharsets.UTF_8))); for (String signature : header.split(",")) if (signature.startsWith("v1=") && MessageDigest.isEqual(expected.getBytes(StandardCharsets.UTF_8), signature.substring(3).getBytes(StandardCharsets.UTF_8))) return true; return false; } catch (Exception ex) { return false; }
    }
    private static String value(String header, String key) { for (String part : header.split(",")) if (part.startsWith(key + "=")) return part.substring(key.length() + 1); throw new IllegalArgumentException("Missing signature value"); }
    private static String hex(byte[] bytes) { StringBuilder result = new StringBuilder(); for (byte b : bytes) result.append(String.format("%02x", b)); return result.toString(); }
}
