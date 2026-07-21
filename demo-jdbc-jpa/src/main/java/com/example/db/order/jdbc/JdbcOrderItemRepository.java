package com.example.db.order.jdbc;

import com.example.db.order.OrderItem;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class JdbcOrderItemRepository {

    private final JdbcClient jdbcClient;

    public JdbcOrderItemRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public long insert(
            long orderId,
            String productName,
            int quantity,
            BigDecimal unitPrice
    ) {
        return jdbcClient.sql("""
                    INSERT INTO order_items (
                        order_id,
                        product_name,
                        quantity,
                        unit_price
                    )
                    VALUES (
                        :orderId,
                        :productName,
                        :quantity,
                        :unitPrice
                    )
                    """)
                .param("orderId", orderId)
                .param("productName", productName)
                .param("quantity", quantity)
                .param("unitPrice", unitPrice)
                .update();
    }

    public List<OrderItem> findByOrderId(long orderId) {
        return jdbcClient.sql("""
                    SELECT
                        id,
                        order_id,
                        product_name,
                        quantity,
                        unit_price
                    FROM order_items
                    WHERE order_id = :orderId
                    ORDER BY id
                    """)
                .param("orderId", orderId)
                .query((rs, rowNum) -> new OrderItem(
                        rs.getLong("id"),
                        rs.getLong("order_id"),
                        rs.getString("product_name"),
                        rs.getInt("quantity"),
                        rs.getBigDecimal("unit_price")
                ))
                .list();
    }
}
