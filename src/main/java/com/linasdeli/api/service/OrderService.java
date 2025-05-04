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

    // ✅ 주문 생성 (Create) - DTO 활용 및 이메일 발송
    public OrderDTO createOrder(OrderRequestDTO orderRequestDTO) {
        Platter platterEntity = platterRepository.findByPlatterName(orderRequestDTO.getPlatterName() + " BOX")
                .orElseThrow(() -> new IllegalArgumentException("Unavailable Platter.: " + orderRequestDTO.getPlatterName()));

        Order order = modelMapper.map(orderRequestDTO, Order.class);
        order.setPlatter(platterEntity);
        order.setStatus("in progress");
        order.setDate(orderRequestDTO.getDate());
        order.setTime(orderRequestDTO.getTime().toLocalTime());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);
        OrderDTO createdOrderDTO = modelMapper.map(savedOrder, OrderDTO.class);

        // 관리자에게 이메일 발송
        String subject = "[Linas Deli] New Order was added.";
        String content = generateAdminOrderEmailContent(createdOrderDTO);
        emailService.sendEmail(adminEmail, subject, content);
        log.info("Admin email sent for order ID: {}", createdOrderDTO.getOid());

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
                    order.setTime(orderRequestDTO.getTime().toLocalTime());
                    order.setUpdatedAt(LocalDateTime.now());

                    Order updatedOrder = orderRepository.save(order);
                    return new OrderDTO(updatedOrder);
                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 ID의 주문을 찾을 수 없습니다."));
    }

    // ✅ 주문 상태 업데이트
    public void updateOrderStatus(Long orderId, String newStatus) {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            if (isValidStatus(newStatus.toUpperCase())) {
                order.setStatus(newStatus.toLowerCase());
                order.setUpdatedAt(LocalDateTime.now());
                orderRepository.save(order);
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
}