package com.streamflix.controller;
import com.streamflix.dto.BillingDtos;
import com.streamflix.service.AuthService;
import com.streamflix.service.SubscriptionService;
import org.springframework.security.core.Authentication;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/subscriptions")
public class SubscriptionController {
    private final AuthService auth; private final SubscriptionService subscriptions;
    public SubscriptionController(AuthService auth, SubscriptionService subscriptions) { this.auth = auth; this.subscriptions = subscriptions; }
    @GetMapping("/plans") public List<BillingDtos.PlanResponse> plans() { return subscriptions.planList(); }
    @GetMapping("/me") public BillingDtos.SubscriptionResponse current(Authentication a) { return subscriptions.current(auth.user(a.getName())); }
    @PostMapping("/checkout") public BillingDtos.CheckoutResponse checkout(Authentication a, @Valid @RequestBody BillingDtos.CheckoutRequest request) { return subscriptions.checkout(auth.user(a.getName()), request.planCode()); }
    @PostMapping("/cancel") public void cancel(Authentication a) { subscriptions.cancel(auth.user(a.getName())); }
}
