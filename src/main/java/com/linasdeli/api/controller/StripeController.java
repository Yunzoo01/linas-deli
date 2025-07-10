package com.linasdeli.api.controller;

import com.linasdeli.api.domain.Order;
import com.linasdeli.api.dto.OrderDTO;
import com.linasdeli.api.dto.request.OrderRequestDTO;
import com.linasdeli.api.service.EmailService;
import com.linasdeli.api.service.OrderService;
import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/payments")
public class StripeController {

    private final OrderService orderService;
    private final EmailService emailService;

    @Value("${stripe.api.secret.key}")
    private String stripeApiKey;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeApiKey;
    }

    public StripeController(OrderService orderService, EmailService emailService) {
        this.orderService = orderService;
        this.emailService = emailService;
    }

    private static final Map<String, String> priceIdMap = Map.of(
            "PETITE BOX", "price_1RjCjZDurDSTecKKOwU2T5yl",
            "MEDIUM BOX", "price_1RjCkKDurDSTecKKvJnvBFCp",
            "LARGE BOX", "price_1RjClDDurDSTecKKJQjRL7O4",
            "PETITE", "price_1RjCjZDurDSTecKKOwU2T5yl",
            "MEDIUM", "price_1RjCkKDurDSTecKKvJnvBFCp",
            "LARGE", "price_1RjClDDurDSTecKKJQjRL7O4"
    );

    @PostMapping("/create-checkout-session")
    public ResponseEntity<String> createCheckoutSession(@RequestBody OrderRequestDTO orderRequestDTO) throws Exception {
        OrderDTO savedOrder = orderService.createOrder(orderRequestDTO);
        String priceId = priceIdMap.get(orderRequestDTO.getPlatterName());

        if (priceId == null) {
            log.error("Invalid platter type: {}", orderRequestDTO.getPlatterName());
            return ResponseEntity.badRequest().body("Invalid platter type");
        }

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("https://linas-deli.ca/success?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("https://linas-deli.ca/cancel?session_id={CHECKOUT_SESSION_ID}")
//                .setSuccessUrl("http://localhost:5173/success?session_id={CHECKOUT_SESSION_ID}")
//                .setCancelUrl("http://localhost:5173/cancel?session_id={CHECKOUT_SESSION_ID}") // session_id 추가
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setPrice(priceId)
                        .setQuantity(1L)
                        .build())
                .putMetadata("orderId", String.valueOf(savedOrder.getOid()))
                .putMetadata("email", orderRequestDTO.getEmail())
                .build();

        Session session = Session.create(params);
        log.info("Session created: {}, orderId: {}", session.getId(), savedOrder.getOid());

        return ResponseEntity.ok(session.getUrl());
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload,
                                                      @RequestHeader("Stripe-Signature") String sigHeader) {
        try {
            log.info("🚀 WEBHOOK RECEIVED - " + new java.util.Date());

            Event event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
            log.info("✅ Event type: {}", event.getType());

            if ("checkout.session.completed".equals(event.getType())) {
                log.info("🎯 Processing checkout.session.completed");

                // JSON payload에서 직접 세션 ID 추출
                String sessionId = extractSessionIdFromPayload(payload);
                log.info("🔍 Extracted session ID from payload: {}", sessionId);

                if (sessionId != null) {
                    try {
                        // 세션 ID로 직접 Session 객체 조회
                        Session session = Session.retrieve(sessionId);
                        log.info("✅ Session retrieved: {}", session.getId());
                        log.info("Payment Status: {}", session.getPaymentStatus());
                        log.info("Metadata: {}", session.getMetadata());

                        if ("paid".equalsIgnoreCase(session.getPaymentStatus())) {
                            String orderIdStr = session.getMetadata().get("orderId");
                            log.info("Order ID from metadata: {}", orderIdStr);

                            if (orderIdStr != null) {
                                Long orderId = Long.valueOf(orderIdStr);

                                // ⚠️ 상태 변경하지 않음 - "in progress" 상태 유지
                                log.info("💰 Payment confirmed for order: {} (status remains 'in progress')", orderId);

                                // 이메일만 발송
                                log.info("📧 Sending payment confirmation emails for order: {}", orderId);
                                orderService.sendPaymentConfirmationEmails(orderId);
                                log.info("✅ Email sending process completed");
                            } else {
                                log.error("❌ Order ID is null in metadata!");
                            }
                        } else {
                            log.warn("⚠️ Payment status is not 'paid': {}", session.getPaymentStatus());
                        }
                    } catch (Exception e) {
                        log.error("❌ Error retrieving session: {}", e.getMessage(), e);
                    }
                } else {
                    log.error("❌ Could not extract session ID from payload!");
                    log.debug("Payload: {}", payload);
                }
            } else {
                log.info("ℹ️ Ignoring event type: {}", event.getType());
            }
        } catch (SignatureVerificationException e) {
            log.error("❌ Webhook signature error", e);
            return ResponseEntity.badRequest().body("Invalid signature");
        } catch (Exception e) {
            log.error("❌ Webhook processing error", e);
            return ResponseEntity.internalServerError().body("Webhook error");
        }

        return ResponseEntity.ok("Webhook received");
    }

    // 페이로드에서 세션 ID 직접 추출
    private String extractSessionIdFromPayload(String payload) {
        try {
            // checkout session ID 패턴: cs_xxxxx
            java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("\"id\"\\s*:\\s*\"(cs_[A-Za-z0-9_]+)\"");
            java.util.regex.Matcher matcher = pattern.matcher(payload);
            if (matcher.find()) {
                return matcher.group(1);
            }
        } catch (Exception e) {
            log.error("Error extracting session ID from payload: {}", e.getMessage());
        }
        return null;
    }

    @GetMapping("/webhook")
    public ResponseEntity<String> webhookStatus() {
        return ResponseEntity.ok("Webhook endpoint is running");
    }

    @GetMapping("/order-status")
    public ResponseEntity<OrderDTO> getOrderStatus(@RequestParam String sessionId) {
        try {
            Session session = Session.retrieve(sessionId);
            Long orderId = Long.valueOf(session.getMetadata().get("orderId"));

            OrderDTO order = orderService.getOrderById(orderId);
            return ResponseEntity.ok(order);
        } catch (Exception e) {
            log.error("Error retrieving order status", e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/payment-success")
    public ResponseEntity<String> paymentSuccessDeprecated(@RequestParam String sessionId) {
        log.warn("Deprecated endpoint used: /payment-success");
        return ResponseEntity.ok("Please use /order-status instead.");
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancelOrder(@RequestBody OrderRequestDTO orderRequestDTO) {
        // 주문 정보 저장 (초기 상태는 optional: 예를 들어 "pending")
        OrderDTO order = orderService.createOrder(orderRequestDTO);

        // 상태를 "declined"로 업데이트
        orderService.updateOrderStatus(order.getOid(), "decline");

        // 이메일은 보내지 않음
        return ResponseEntity.ok("Order cancelled and status set to declined.");
    }


    @PostMapping("/payment-failed")
    public ResponseEntity<String> paymentFailed(@RequestParam String sessionId) {
        log.info("Payment failed for session {}", sessionId);
        return ResponseEntity.ok("Payment failure acknowledged");
    }

    // StripeController에 추가할 메서드

    @PostMapping("/cancel-session")
    public ResponseEntity<String> cancelSession(@RequestBody Map<String, String> request) {
        String sessionId = request.get("sessionId");

        try {
            log.info("🚫 Processing session cancellation: {}", sessionId);

            Session session = Session.retrieve(sessionId);
            String orderIdStr = session.getMetadata().get("orderId");

            if (orderIdStr != null) {
                Long orderId = Long.valueOf(orderIdStr);

                log.info("📝 Updating order {} status to declined", orderId);
                orderService.updateOrderStatus(orderId, "decline");
                log.info("✅ Order {} status updated to declined (no email sent)", orderId);

                return ResponseEntity.ok("Order cancelled successfully");
            } else {
                log.warn("⚠️ No orderId found in cancelled session: {}", sessionId);
                return ResponseEntity.ok("Session processed");
            }

        } catch (Exception e) {
            log.error("❌ Error cancelling session: {}", sessionId, e);
            return ResponseEntity.ok("Cancellation processed"); // 사용자에게는 성공으로 표시
        }
    }
}
