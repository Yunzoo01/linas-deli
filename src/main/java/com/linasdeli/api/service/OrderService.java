package com.linasdeli.api.service;

import com.linasdeli.api.domain.Order;
import com.linasdeli.api.domain.Platter;
import com.linasdeli.api.dto.OrderDTO;
import com.linasdeli.api.dto.OrderStatusCountDTO;
import com.linasdeli.api.dto.request.OrderRequestDTO;
import com.linasdeli.api.repository.OrderRepository;
import com.linasdeli.api.repository.PlatterRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final PlatterRepository platterRepository;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    @Value("${admin.email}")
    private String adminEmail;

    @Autowired
    public OrderService(OrderRepository orderRepository, PlatterRepository platterRepository, ModelMapper modelMapper, EmailService emailService) {
        this.orderRepository = orderRepository;
        this.platterRepository = platterRepository;
        this.modelMapper = modelMapper;
        this.emailService = emailService;
    }

    @Transactional
    public OrderDTO createOrder(OrderRequestDTO orderRequestDTO) {
        log.info("Creating order with DTO: {}", orderRequestDTO);

        String platterName = orderRequestDTO.getPlatterName() + " BOX";
        log.info("Searching platter with name: {}", platterName);

        Platter platterEntity = platterRepository.findByPlatterName(platterName)
                .orElseThrow(() -> new IllegalArgumentException("Unavailable Platter.: " + platterName));

        Order order = modelMapper.map(orderRequestDTO, Order.class);
        order.setPlatter(platterEntity);
        order.setStatus("in progress");
        order.setDate(orderRequestDTO.getDate());
        order.setTime(orderRequestDTO.getTime());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        log.info("Saving order: {}", order);
        Order savedOrder = orderRepository.save(order);

        OrderDTO createdOrderDTO = modelMapper.map(savedOrder, OrderDTO.class);
        log.info("Order saved with ID: {}", createdOrderDTO.getOid());

//        String subject = "[Linas Deli] New Order was added.";
//        String content = generateAdminOrderEmailContent(createdOrderDTO);
//
//        try {
//            emailService.sendEmail(adminEmail, subject, content);
//            emailService.sendEmail(orderRequestDTO.getEmail(), subject, content);
//            log.info("Emails sent for order ID: {}", createdOrderDTO.getOid());
//        } catch (Exception e) {
//            log.error("Email sending failed for order ID {}: {}", createdOrderDTO.getOid(), e.getMessage());
//        }

        return createdOrderDTO;
    }


    // ✅ Order 목록 페이징 및 검색 (DTO 변환)
    public Page<OrderDTO> getOrders(Pageable pageable, String keyword, String status) {
        Page<Order> orders;
        if ((keyword == null || keyword.isEmpty()) && (status == null || status.isEmpty() || status.equalsIgnoreCase("All"))) {
            orders = orderRepository.findAllOrders(pageable);
        } else if (status == null || status.isEmpty()) {
            orders = orderRepository.findByEmailContainingIgnoreCaseOrderByOidDesc(keyword, pageable);
        } else if (keyword == null || keyword.isEmpty()) {
            orders = orderRepository.findByStatusContainingIgnoreCaseOrderByOidDesc(status.toLowerCase(), pageable);
        } else {
            orders = orderRepository.findByEmailContainingIgnoreCaseAndStatusOrderByOidDesc(keyword, status.toLowerCase(), pageable);
        }
        return orders.map(OrderDTO::new);
    }

    // ✅ Status별 오더 개수
    public List<OrderStatusCountDTO> countOrdersByStatus() {
        List<OrderStatusCountDTO> statusCounts = orderRepository.countOrdersByStatus();
        long totalOrders = orderRepository.countTotalOrders();
        statusCounts.add(0, new OrderStatusCountDTO("All", totalOrders));
        return statusCounts;
    }

    // ✅ 주문 수정 (Update) - DTO 활용
    public OrderDTO updateOrder(Long id, OrderRequestDTO orderRequestDTO) {
        return orderRepository.findById(id)
                .map(order -> {
                    Platter platterEntity = platterRepository.findByPlatterName(orderRequestDTO.getPlatterName())
                            .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 플래터입니다.: " + orderRequestDTO.getPlatterName()));
                    order.setPlatter(platterEntity);
                    order.setCustomerName(orderRequestDTO.getCustomerName());
                    order.setEmail(orderRequestDTO.getEmail());
                    order.setPhone(orderRequestDTO.getPhone());
                    order.setAllergy(orderRequestDTO.getAllergy());
                    order.setMessage(orderRequestDTO.getMessage());
                    order.setDate(orderRequestDTO.getDate());
                    order.setTime(orderRequestDTO.getTime());
                    order.setUpdatedAt(LocalDateTime.now());

                    Order updatedOrder = orderRepository.save(order);
                    return new OrderDTO(updatedOrder);
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 ID의 주문을 찾을 수 없습니다."));
    }

    public OrderDTO updateOrderStatus(Long orderId, String newStatus) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            if (isValidStatus(newStatus.toUpperCase())) {
                order.setStatus(newStatus.toLowerCase());
                order.setUpdatedAt(LocalDateTime.now());
                Order updatedOrder = orderRepository.save(order);
                return new OrderDTO(updatedOrder); // ← OrderDTO 반환 추가
            } else {
                throw new IllegalArgumentException("유효하지 않은 주문 상태입니다: " + newStatus);
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 ID의 주문을 찾을 수 없습니다: " + orderId);
        }
    }

    private boolean isValidStatus(String status) {
        return status.equals("IN PROGRESS") || status.equals("COMPLETED") || status.equals("DECLINE") || status.equals("COMPLETED_DECLINE");
    }

    // 기존 관리자용 이메일 템플릿은 그대로 유지
    private String generateAdminOrderEmailContent(OrderDTO orderDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String createdAtFormatted = (orderDTO.getCreatedAt() != null) ? orderDTO.getCreatedAt().format(formatter) : "Information not available";
        return "<h1>New order received</h1>" +
                "<p><b>Order Number:</b> " + orderDTO.getOid() + "</p>" +
                "<p><b>Customer Name:</b> " + orderDTO.getCustomerName() + "</p>" +
                "<p><b>Email:</b> " + orderDTO.getEmail() + "</p>" +
                "<p><b>Phone Number:</b> " + orderDTO.getPhone() + "</p>" +
                "<p><b>Product:</b> " + (orderDTO.getPlatterName() != null ? orderDTO.getPlatterName() : "Information not available") + "</p>" +
                "<p><b>Date:</b> " + orderDTO.getDate() + "</p>" +
                "<p><b>Time:</b> " + orderDTO.getTime() + "</p>" +
                "<p><b>Allergy Information:</b> " + orderDTO.getAllergy() + "</p>" +
                "<p><b>Message:</b> " + orderDTO.getMessage() + "</p>" +
                "<p><b>Order Creation Time:</b> " + createdAtFormatted + "</p>";
    }

    // OrderService.java에 추가할 메서드
    public OrderDTO getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(order -> new OrderDTO(order))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "해당 ID의 주문을 찾을 수 없습니다: " + orderId));
    }

    // OrderService.java에 추가할 메서드들

    // 결제 완료 후 이메일 발송 (웹훅에서 호출용)
    public void sendPaymentConfirmationEmails(Long orderId) {
        try {
            OrderDTO orderDTO = getOrderById(orderId);

            String adminSubject = "[Linas Deli] New Order was added.";
            String customerSubject = "[Linas Deli] Payment Confirmed - Order #" + orderId;

            String adminContent = generateAdminOrderEmailContent(orderDTO);
            String customerContent = generateCustomerOrderEmailContent(orderDTO);

            // 관리자 이메일 발송
            emailService.sendEmail(adminEmail, adminSubject, adminContent);
            log.info("Admin email sent for order ID: {}", orderId);

            // 고객 이메일 발송
            emailService.sendEmail(orderDTO.getEmail(), customerSubject, customerContent);
            log.info("Customer email sent for order ID: {}", orderId);

        } catch (Exception e) {
            log.error("Email sending failed for order ID {}: {}", orderId, e.getMessage());
        }
    }

    // 고객용 이메일 템플릿 (결제 확인)
    private String generateCustomerOrderEmailContent(OrderDTO orderDTO) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String createdAtFormatted = (orderDTO.getCreatedAt() != null) ? orderDTO.getCreatedAt().format(formatter) : "Information not available";

        return "<h1>Thank you for your order!</h1>" +
                "<p>Your payment has been successfully processed.</p>" +
                "<h2>Order Details:</h2>" +
                "<p><b>Order Number:</b> " + orderDTO.getOid() + "</p>" +
                "<p><b>Customer Name:</b> " + orderDTO.getCustomerName() + "</p>" +
                "<p><b>Product:</b> " + (orderDTO.getPlatterName() != null ? orderDTO.getPlatterName() : "Information not available") + "</p>" +
                "<p><b>Pickup Date:</b> " + orderDTO.getDate() + "</p>" +
                "<p><b>Pickup Time:</b> " + orderDTO.getTime() + "</p>" +
                "<p><b>Phone:</b> " + orderDTO.getPhone() + "</p>" +
                (orderDTO.getAllergy() != null && !orderDTO.getAllergy().isEmpty() ?
                        "<p><b>Allergy Information:</b> " + orderDTO.getAllergy() + "</p>" : "") +
                (orderDTO.getMessage() != null && !orderDTO.getMessage().isEmpty() ?
                        "<p><b>Special Message:</b> " + orderDTO.getMessage() + "</p>" : "") +
                "<p><b>Order Status:</b> " + (orderDTO.getStatus() != null ? orderDTO.getStatus().toUpperCase() : "CONFIRMED") + "</p>" +
                "<p><b>Order Creation Time:</b> " + createdAtFormatted + "</p>" +
                "<br><p>Thank you for choosing Linas Deli!</p>";
    }


}