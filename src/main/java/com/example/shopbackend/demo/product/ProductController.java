package com.example.shopbackend.demo.product;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDto> getAll() {
        return productService.getAll()
                .stream()
                .map(ProductDto::from)
                .toList();
    }

    public ProductDto create(@RequestBody CreateProductRequest request) {
        Product created = productService.create(request.name(), request.price(), request.stock());
        return ProductDto.from(created);
    }
}
