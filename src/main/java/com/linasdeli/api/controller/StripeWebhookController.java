package com.linasdeli.api.controller;

import com.linasdeli.api.service.EmailService;
import com.linasdeli.api.service.OrderService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/api/stripe/webhook")
public class StripeWebhookController {

    private final OrderService orderService;
    private final EmailService emailService;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    public StripeWebhookController(OrderService orderService, EmailService emailService) {
        this.orderService = orderService;
        this.emailService = emailService;
    }

    @PostMapping
    public ResponseEntity<String> handleStripeWebhook(HttpServletRequest request) throws IOException {
        String payload = getBody(request);
        String sigHeader = request.getHeader("Stripe-Signature");

        log.debug("Received Stripe webhook request");
        log.debug("Payload: {}", payload);
        log.debug("Stripe-Signature header: {}", sigHeader);

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
            log.debug("Stripe event type: {}", event.getType());
        } catch (SignatureVerificationException e) {
            log.error("⚠️ Webhook signature verification failed.", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Signature verification failed");
        }

        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);
            if (session == null) {
                log.error("Session object is null in event data");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Session object null");
            }
            log.info("Checkout session completed: {}", session.getId());

            String orderIdStr = session.getMetadata().get("orderId");
            String email = session.getMetadata().get("email");

            log.debug("Metadata - orderId: {}, email: {}", orderIdStr, email);

            if (orderIdStr == null) {
                log.error("Order ID is missing in metadata!");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Order ID missing");
            }

            try {
                Long orderId = Long.valueOf(orderIdStr);

                orderService.updateOrderStatus(orderId, "completed");
                log.info("Order status updated to 'completed' for orderId: {}", orderId);

                emailService.sendEmail(
                        email,
                        "[Linas Deli] Order Confirmation",
                        "Thank you for your order! Your payment has been confirmed."
                );

            } catch (NumberFormatException e) {
                log.error("Invalid orderId format: {}", orderIdStr, e);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid order ID");
            }
        }

        return ResponseEntity.ok("Webhook handled");
    }

    private String getBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        BufferedReader reader = request.getReader();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }

    @GetMapping
    public ResponseEntity<String> handleGet() {
        return ResponseEntity.ok("Webhook endpoint is up");
    }
}
