package com.example.demodb.order;

import com.example.demodb.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    /**
     * Creates an order and updates the product stock.
     *
     * - Propagation.REQUIRED: joins an existing transaction or creates a new one.
     * - Isolation.READ_COMMITTED: reads only committed data, preventing dirty reads.
     *
     * The updateStock call inside runs in its own REQUIRES_NEW transaction so that
     * the stock deduction commits independently regardless of this transaction outcome.
     */
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public Order createOrder(Integer productId, Integer quantity) {
        Order order = new Order();
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setStatus("PENDING");
        orderRepository.save(order);

        // Runs in a separate REQUIRES_NEW transaction (see ProductService.updateStock)
        productService.updateStock(productId, quantity);

        order.setStatus("COMPLETED");
        return orderRepository.save(order);
    }
}
