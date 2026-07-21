package com.example.db.order.jpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JpaOrderRepository
        extends JpaRepository<OrderEntity, Long> {

    // Requests the items together with the order
    // and helps prevent accessing a lazy collection after the transaction has closed
    @EntityGraph(attributePaths = "items")
    @Query("""
            SELECT o
            FROM OrderEntity o
            WHERE o.id = :id
            """)
    Optional<OrderEntity> findWithItemsById(Long id);

    @EntityGraph(attributePaths = "items")
    @Query("""
            SELECT DISTINCT o
            FROM OrderEntity o
            ORDER BY o.createdAt DESC, o.id DESC
            """)
    List<OrderEntity> findAllWithItems();
}
