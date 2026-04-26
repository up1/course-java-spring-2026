package com.example.demodb.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/api/process1")
    public Product process1(@RequestBody Product product) {
        return productService.createNewProduct(product);
    }

    @PostMapping("/api/process2")
    public Product process2(@RequestBody Product product) {
        return productService.processWithTransaction(product);
    }

}
