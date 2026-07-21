package com.example.db.order.jpa;

import com.example.db.order.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class JpaOrderService {

    private final JpaOrderRepository orderRepository;

    public JpaOrderService(
            JpaOrderRepository orderRepository
    ) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderResponse create(CreateOrderRequest request) {
        BigDecimal totalAmount = request.items()
                .stream()
                .map(this::calculateLineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        OrderEntity order = new OrderEntity(
                request.customerName(),
                "CREATED",
                totalAmount,
                LocalDateTime.now()
        );

        request.items().forEach(item ->
                order.addItem(new OrderItemEntity(
                        item.productName(),
                        item.quantity(),
                        item.unitPrice()
                ))
        );

        /*
         * CascadeType.ALL causes the OrderItem entities
         * to be inserted when the Order entity is saved
         */
        OrderEntity savedOrder =
                orderRepository.save(order);

        return mapResponse(savedOrder);
    }

    @Transactional(readOnly = true)
    public OrderResponse findById(long id) {
        OrderEntity order =
                orderRepository.findWithItemsById(id)
                        .orElseThrow(() ->
                                new OrderNotFoundException(id)
                        );

        return mapResponse(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findAll() {
        return orderRepository.findAllWithItems()
                .stream()
                .map(this::mapResponse)
                .toList();
    }

    private BigDecimal calculateLineTotal(
            CreateOrderItemRequest item
    ) {
        return item.unitPrice()
                .multiply(BigDecimal.valueOf(item.quantity()));
    }

    private OrderResponse mapResponse(OrderEntity order) {
        List<OrderItemResponse> items =
                order.getItems()
                        .stream()
                        .map(item -> new OrderItemResponse(
                                item.getId(),
                                item.getProductName(),
                                item.getQuantity(),
                                item.getUnitPrice(),
                                item.calculateLineTotal()
                        ))
                        .toList();

        return new OrderResponse(
                order.getId(),
                order.getCustomerName(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                items
        );
    }
}
