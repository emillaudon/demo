package com.example.shopbackend.demo.product;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getById(@PathVariable Long id) {
        Product product = productService.getById(id);
        ProductDto dto = ProductDto.from(product);

        return ResponseEntity.ok(dto);
    }

    @GetMapping(params = "query")
    public List<ProductDto> getByName(@RequestParam String query) {
        return productService.getByName(query)
                .stream()
                .map(ProductDto::from)
                .toList();
    }

    @GetMapping(params = "inStock")
    public List<ProductDto> getInStock(@RequestParam boolean inStock) {
        return productService.getInStock(inStock)
                .stream()
                .map(ProductDto::from)
                .toList();
    }

    @GetMapping(params = { "from", "to" })
    public List<ProductDto> getPriceBetween(@RequestParam int from, @RequestParam int to) {
        return productService.getPriceBetween(from, to)
                .stream()
                .map(ProductDto::from)
                .toList();
    }

    @GetMapping(params = { "!inStock", "!from", "!to", "!query" })
    public List<ProductDto> getAll() {
        return productService.getAll()
                .stream()
                .map(ProductDto::from)
                .toList();
    }

    @PostMapping
    public ResponseEntity<ProductDto> create(@Valid @RequestBody CreateProductRequest request) {
        Product created = productService.create(request.name(), request.price(), request.stock());

        URI location = URI.create("/products/" + created.getId());
        return ResponseEntity
                .created(location)
                .body(ProductDto.from(created));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> update(@PathVariable Long id, @Valid @RequestBody UpdateProductRequest request) {
        Product updated = productService.update(id, request);
        return ResponseEntity.ok(ProductDto.from(updated));
    }

    @PostMapping("{id}/image")
    public ResponseEntity<ProductDto> addImage(@PathVariable long id, @RequestPart("file") MultipartFile file) {
        Product updated = productService.uploadImage(id, file);
        
        return ResponseEntity.ok(ProductDto.from(updated));
    }
    
    @DeleteMapping("/{id}/image")
    public ResponseEntity<ProductDto> deleteImage(@PathVariable Long id) {
        Product updated = productService.deleteImage(id);
        return ResponseEntity.ok(ProductDto.from(updated));
    }

}
