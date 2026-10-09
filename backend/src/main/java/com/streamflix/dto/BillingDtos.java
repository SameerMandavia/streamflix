package com.streamflix.dto;
import java.time.Instant;
import java.util.List;
import jakarta.validation.constraints.NotBlank;
public final class BillingDtos {
    private BillingDtos() {}
    public record PlanResponse(String code, String name, Integer priceCents, String currency, String billingInterval, boolean premium) {}
    public record SubscriptionResponse(String planCode, String planName, String status, Instant currentPeriodEnd, boolean cancelAtPeriodEnd, boolean premium) {}
    public record CheckoutResponse(String url) {}
    public record CheckoutRequest(@NotBlank String planCode) {}
}
