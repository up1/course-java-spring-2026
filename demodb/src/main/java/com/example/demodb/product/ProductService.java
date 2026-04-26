package com.example.demodb.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public Product createNewProduct(Product product) {
        // Transaction 1
        productRepository.save(product);
        // Transaction 2
        productRepository.findAll();
        return product;
    }

    @Transactional
    public Product processWithTransaction(Product product) {
        productRepository.save(product);
        productRepository.findAll();
        return product;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, isolation = Isolation.READ_COMMITTED)
    public void updateStock(Integer productId, Integer quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found: " + productId));
        if (product.getStock() == null || product.getStock() < quantity) {
            throw new RuntimeException("Insufficient stock for product: " + productId);
        }
        product.setStock(product.getStock() - quantity);
        productRepository.save(product);
    }

}
