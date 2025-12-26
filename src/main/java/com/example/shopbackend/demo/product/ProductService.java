package com.example.shopbackend.demo.product;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.shopbackend.demo.common.NotFoundException;

@Service
public class ProductService {
    private final ProductRepository repository;

    public ProductService(final ProductRepository repository) {
        this.repository = repository;
    }

    public Product getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product", id));
    }

    public List<Product> getAll() {
        return repository.findAll();
    }

    public Product create(final String name, int price, int stock) {
        return repository.save(new Product(name, price, stock));
    }
}
