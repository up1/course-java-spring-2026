package com.example.db.order;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcOrderRepository {

    private final JdbcClient jdbcClient;

    public JdbcOrderRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public long insert(
            String customerName,
            String status,
            BigDecimal totalAmount,
            LocalDateTime createdAt
    ) {
        return jdbcClient.sql("""
                    INSERT INTO orders (
                        customer_name,
                        status,
                        total_amount,
                        created_at
                    )
                    VALUES (
                        :customerName,
                        :status,
                        :totalAmount,
                        :createdAt
                    )
                    """)
                .param("customerName", customerName)
                .param("status", status)
                .param("totalAmount", totalAmount)
                .param("createdAt", createdAt)
                .update();
    }

    public Optional<Order> findById(long id) {
        return jdbcClient.sql("""
                    SELECT
                        id,
                        customer_name,
                        status,
                        total_amount,
                        created_at
                    FROM orders
                    WHERE id = :id
                    """)
                .param("id", id)
                .query((rs, rowNum) -> new Order(
                        rs.getLong("id"),
                        rs.getString("customer_name"),
                        rs.getString("status"),
                        rs.getBigDecimal("total_amount"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                ))
                .optional();
    }

    public List<Order> findAll() {
        return jdbcClient.sql("""
                    SELECT
                        id,
                        customer_name,
                        status,
                        total_amount,
                        created_at
                    FROM orders
                    ORDER BY created_at DESC, id DESC
                    """)
                .query((rs, rowNum) -> new Order(
                        rs.getLong("id"),
                        rs.getString("customer_name"),
                        rs.getString("status"),
                        rs.getBigDecimal("total_amount"),
                        rs.getTimestamp("created_at").toLocalDateTime()
                ))
                .list();
    }
}
