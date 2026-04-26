package com.example.demodb.product;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}
