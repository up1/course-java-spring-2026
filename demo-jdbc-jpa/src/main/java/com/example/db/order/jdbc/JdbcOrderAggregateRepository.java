package com.example.db.order.jdbc;

import com.example.db.order.OrderAggregate;
import com.example.db.order.OrderItem;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.*;

@Repository
public class JdbcOrderAggregateRepository {

    private final JdbcClient jdbcClient;

    public JdbcOrderAggregateRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public Optional<OrderAggregate> findById(long id) {
        List<OrderRow> rows = jdbcClient.sql("""
                    SELECT
                        o.id                AS order_id,
                        o.customer_name     AS customer_name,
                        o.status            AS status,
                        o.total_amount      AS total_amount,
                        o.created_at        AS created_at,
                        i.id                AS item_id,
                        i.order_id          AS item_order_id,
                        i.product_name      AS product_name,
                        i.quantity          AS quantity,
                        i.unit_price        AS unit_price
                    FROM orders o
                    LEFT JOIN order_items i ON i.order_id = o.id
                    WHERE o.id = :id
                    ORDER BY i.id
                    """)
                .param("id", id)
                .query((rs, rowNum) -> new OrderRow(
                        rs.getLong("order_id"),
                        rs.getString("customer_name"),
                        rs.getString("status"),
                        rs.getBigDecimal("total_amount"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        (Long) rs.getObject("item_id"),
                        (Long) rs.getObject("item_order_id"),
                        rs.getString("product_name"),
                        (Integer) rs.getObject("quantity"),
                        rs.getBigDecimal("unit_price")
                ))
                .list();

        if (rows.isEmpty()) {
            return Optional.empty();
        }

        Map<Long, OrderAggregate> aggregates = new LinkedHashMap<>();
        Map<Long, List<OrderItem>> itemsByOrderId = new LinkedHashMap<>();

        for (OrderRow row : rows) {
            List<OrderItem> items = itemsByOrderId
                    .computeIfAbsent(row.orderId(), key -> new ArrayList<>());

            if (row.itemId() != null) {
                items.add(new OrderItem(
                        row.itemId(),
                        row.itemOrderId(),
                        row.productName(),
                        row.quantity(),
                        row.unitPrice()
                ));
            }

            aggregates.putIfAbsent(row.orderId(), new OrderAggregate(
                    row.orderId(),
                    row.customerName(),
                    row.status(),
                    row.totalAmount(),
                    row.createdAt(),
                    items
            ));
        }

        return aggregates.values().stream().findFirst();
    }

    private record OrderRow(
            Long orderId,
            String customerName,
            String status,
            BigDecimal totalAmount,
            java.time.LocalDateTime createdAt,
            Long itemId,
            Long itemOrderId,
            String productName,
            Integer quantity,
            BigDecimal unitPrice
    ) {
    }
}
