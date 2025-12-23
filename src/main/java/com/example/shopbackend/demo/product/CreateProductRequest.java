package com.example.shopbackend.demo.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateProductRequest(
        @NotBlank String name,
        @Min(1) int price,
        @Min(1) int stock) {
}