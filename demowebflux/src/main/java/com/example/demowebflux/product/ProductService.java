package com.example.demowebflux.product;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProductService {

    private final ProductRepository repository;

    public ProductService(ProductRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Flux<Product> findAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Mono<Product> findById(Long id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new ProductNotFoundException(id)));
    }

    @Transactional
    public Mono<Product> create(Product product) {
        product.setId(null);
        return repository.save(product);
    }
}
