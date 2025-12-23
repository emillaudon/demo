package com.example.shopbackend.demo.product;

import java.util.List;

public class ProductService {
    private final ProductRepository repository;

    public ProductService(final ProductRepository repository) {
        this.repository = repository;
    }

    public List<Product> getAll() {
        return repository.findAll();
    }

    public Product create(final String name, int price, int stock) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name is required.");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price must be above 0.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("Stock must be above 0.");
        }

        return repository.save(new Product(name, price, stock));
    }
}
