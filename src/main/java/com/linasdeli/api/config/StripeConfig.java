package com.linasdeli.api.config;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;


@Component
public class StripeConfig {

    @PostConstruct
    public void init() {
        Stripe.apiKey = "sk_test_..."; // 테스트 Secret Key
    }
}
