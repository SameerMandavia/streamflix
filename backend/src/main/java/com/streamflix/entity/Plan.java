package com.streamflix.entity;

import jakarta.persistence.*;

@Entity @Table(name = "plans")
public class Plan {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true) private String code;
    @Column(nullable = false) private String name;
    @Column(name = "price_cents", nullable = false) private Integer priceCents;
    @Column(nullable = false) private String currency;
    @Column(name = "billing_interval", nullable = false) private String billingInterval;
    @Column(name = "stripe_price_id") private String stripePriceId;
    @Column(nullable = false) private boolean premium;
    protected Plan() {}
    public Long getId() { return id; } public String getCode() { return code; } public String getName() { return name; } public Integer getPriceCents() { return priceCents; }
    public String getCurrency() { return currency; } public String getBillingInterval() { return billingInterval; } public String getStripePriceId() { return stripePriceId; } public boolean isPremium() { return premium; }
}
