package com.example.shopbackend.demo.product;

public record CreateProductRequest(
        String name,
        int price,
        int stock) {
}