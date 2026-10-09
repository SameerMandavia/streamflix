package com.streamflix.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity @Table(name = "subscriptions")
public class Subscription {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "user_id") private User user;
    @ManyToOne(fetch = FetchType.LAZY, optional = false) @JoinColumn(name = "plan_id") private Plan plan;
    @Column(name = "stripe_customer_id") private String stripeCustomerId;
    @Column(name = "stripe_subscription_id", unique = true) private String stripeSubscriptionId;
    @Column(nullable = false) private String status;
    @Column(name = "current_period_end") private Instant currentPeriodEnd;
    @Column(name = "cancel_at_period_end", nullable = false) private boolean cancelAtPeriodEnd;
    @Column(nullable = false, updatable = false) private Instant createdAt;
    @Column(nullable = false) private Instant updatedAt;
    protected Subscription() {}
    public Subscription(User user, Plan plan, String status) { this.user = user; this.plan = plan; this.status = status; }
    @PrePersist @PreUpdate void touch() { if (createdAt == null) createdAt = Instant.now(); updatedAt = Instant.now(); }
    public User getUser() { return user; } public Plan getPlan() { return plan; } public String getStripeCustomerId() { return stripeCustomerId; } public String getStripeSubscriptionId() { return stripeSubscriptionId; }
    public String getStatus() { return status; } public Instant getCurrentPeriodEnd() { return currentPeriodEnd; } public boolean isCancelAtPeriodEnd() { return cancelAtPeriodEnd; }
    public void billing(String customerId, String subscriptionId, String status, Instant periodEnd, boolean cancelAtPeriodEnd) { this.stripeCustomerId = customerId; this.stripeSubscriptionId = subscriptionId; this.status = status; this.currentPeriodEnd = periodEnd; this.cancelAtPeriodEnd = cancelAtPeriodEnd; }
}
