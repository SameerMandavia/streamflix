package com.streamflix.controller;
import com.streamflix.service.SubscriptionService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/webhooks")
public class StripeWebhookController {
    private final SubscriptionService subscriptions;
    public StripeWebhookController(SubscriptionService subscriptions) { this.subscriptions = subscriptions; }
    @PostMapping(value = "/stripe", consumes = MediaType.APPLICATION_JSON_VALUE) public void stripe(@RequestBody String payload, @RequestHeader("Stripe-Signature") String signature) { subscriptions.webhook(payload, signature); }
}
