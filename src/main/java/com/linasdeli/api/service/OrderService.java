package com.linasdeli.api.service;

import com.linasdeli.api.domain.Order;
import com.linasdeli.api.domain.Platter;
import com.linasdeli.api.dto.OrderStatusCountDTO;
import com.linasdeli.api.dto.request.OrderRequestDTO;
import com.linasdeli.api.dto.response.OrderDTO;
import com.linasdeli.api.dto.response.OrderResponseDTO;
import com.linasdeli.api.repository.OrderRepository;
import com.linasdeli.api.repository.PlatterRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final PlatterRepository platterRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, PlatterRepository platterRepository) {
        this.orderRepository = orderRepository;
        this.platterRepository = platterRepository;
    }


    // ✅ 주문 생성 (Create) - DTO 활용
    public OrderDTO createOrder(OrderRequestDTO orderRequestDTO) {
        Order order = new Order();
        log.info(orderRequestDTO.getPlatter());
        Platter platterEntity = platterRepository.findByPlatterName(orderRequestDTO.getPlatter()+" BOX")
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 플래터입니다.:"));

        order.setPlatter(platterEntity);
        order.setCustomerName(orderRequestDTO.getCustomerName());
        order.setEmail(orderRequestDTO.getEmail());
        order.setPhone(orderRequestDTO.getPhone());
        order.setAllergy(orderRequestDTO.getAllergy());
        order.setMessage(orderRequestDTO.getMessage());
        order.setStatus("in progress");
        order.setDate(orderRequestDTO.getDate().atStartOfDay());
        order.setTime(orderRequestDTO.getTime().toLocalTime());
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);
        return new OrderDTO(savedOrder);
    }

    // ✅ Order 목록 페이징 및 검색 (DTO 변환)
    public Page<OrderDTO> getOrders(Pageable pageable, String keyword, String status) {
        Page<Order> orders;
        //status에 따라 select하기
        if ((keyword == null || keyword.isEmpty()) && (status == null || status.isEmpty() || status.equals("All"))) {
            // 키워드도 없고 상태도 없거나 "all"이면 전체 검색
            orders = orderRepository.findAllOrders(pageable);
        } else if (status == null || status.isEmpty()) {
            // 키워드만 검색 (상태 무시)
            orders = orderRepository.findByEmailContainingIgnoreCaseOrderByOidDesc(keyword, pageable);
        } else if( keyword == null || keyword.isEmpty()) {
            // status만 검색(키워드 무시)
            orders = orderRepository.findByStatusContainingIgnoreCaseOrderByOidDesc(status, pageable);
        }
        else {
            // 키워드 + 특정 상태 검색
            orders = orderRepository.findByEmailContainingIgnoreCaseAndStatusOrderByOidDesc(keyword, status, pageable);
        }
        return orders.map(OrderDTO::new);
    }

    // ✅ Status별 오더 개수
    public List<OrderStatusCountDTO> countOrdersByStatus() {
        List<OrderStatusCountDTO> statusCounts = orderRepository.countOrdersByStatus();
        long totalOrders = orderRepository.countTotalOrders(); // 전체 개수 가져오기

        // "All"을 추가하여 리스트에 포함
        statusCounts.add(0, new OrderStatusCountDTO("All", totalOrders));
        return statusCounts;
    }

    // ✅ 주문 조회 (Read) - DTO 활용
//    public Optional<OrderResponseDTO> getOrderById(Long id) {
//        return orderRepository.findById(id).map(OrderResponseDTO::new);
//    }

    // ✅ 주문 수정 (Update) - DTO 활용
    public OrderDTO updateOrder(Long id, OrderRequestDTO orderRequestDTO) {
        return orderRepository.findById(id)
                .map(order -> {
                    order.setCustomerName(orderRequestDTO.getCustomerName());
                    order.setEmail(orderRequestDTO.getEmail());
                    order.setAllergy(orderRequestDTO.getAllergy());
                    order.setCustomerName(orderRequestDTO.getCustomerName());
                    order.setMessage(orderRequestDTO.getMessage());
                    //time, date 추가
                    //status 수정가능하도록 추가
                    //platter 수정가능하도록 추가

                    Order updatedOrder = orderRepository.save(order);
                    return new OrderDTO(updatedOrder);
                }).orElseThrow(() -> new RuntimeException("Order not found"));
    }
//
//    // ✅ 주문 삭제 (Delete)
//    public void deleteOrder(Long id) {
//        orderRepository.deleteById(id);
//    }

}
