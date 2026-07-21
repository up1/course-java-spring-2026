package com.example.db.order.jdbc;


import com.example.db.order.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class JdbcOrderService {
    private final JdbcOrderRepository orderRepository;
    private final JdbcOrderItemRepository orderItemRepository;

    public JdbcOrderService(
            JdbcOrderRepository orderRepository,
            JdbcOrderItemRepository orderItemRepository
    ) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    @Transactional
    public OrderResponse create(CreateOrderRequest request) {
        BigDecimal totalAmount = request.items()
                .stream()
                .map(this::calculateLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        long orderId = orderRepository.insert(
                request.customerName(),
                "CREATED",
                totalAmount,
                LocalDateTime.now()
        );

        for (CreateOrderItemRequest item : request.items()) {
            orderItemRepository.insert(
                    orderId,
                    item.productName(),
                    item.quantity(),
                    item.unitPrice()
            );
        }

        return findById(orderId);
    }

    @Transactional(readOnly = true)
    public OrderResponse findById(long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException(id)
                );

        List<OrderItem> items =
                orderItemRepository.findByOrderId(id);

        return mapResponse(order, items);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(order -> mapResponse(
                        order,
                        orderItemRepository.findByOrderId(order.id())
                ))
                .toList();
    }

    private BigDecimal calculateLineTotal(
            CreateOrderItemRequest item
    ) {
        return item.unitPrice()
                .multiply(BigDecimal.valueOf(item.quantity()));
    }

    private OrderResponse mapResponse(
            Order order,
            List<OrderItem> items
    ) {
        List<OrderItemResponse> itemResponses = items.stream()
                .map(item -> new OrderItemResponse(
                        item.id(),
                        item.productName(),
                        item.quantity(),
                        item.unitPrice(),
                        item.lineTotal()
                ))
                .toList();

        return new OrderResponse(
                order.id(),
                order.customerName(),
                order.status(),
                order.totalAmount(),
                order.createdAt(),
                itemResponses
        );
    }
}
