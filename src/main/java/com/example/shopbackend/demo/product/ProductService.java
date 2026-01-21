package com.example.shopbackend.demo.product;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.shopbackend.demo.common.ImageTooLargeException;
import com.example.shopbackend.demo.common.InvalidImageTypeException;
import com.example.shopbackend.demo.common.InvalidPriceRangeException;
import com.example.shopbackend.demo.common.NotFoundException;
import com.example.shopbackend.demo.common.OutOfStockException;
import com.example.shopbackend.demo.storage.ImageStorage;

import jakarta.transaction.Transactional;

@Service
public class ProductService {
    private final ProductRepository repository;
    private final ProductStockGateway productStockGateway;
    private final ImageStorage imageStorage;

    private static final Logger log = LoggerFactory.getLogger(ProductService.class);

    public ProductService(final ProductRepository repository, final ProductStockGateway productStockGateway,
            final ImageStorage imageStorage) {
        this.repository = repository;
        this.productStockGateway = productStockGateway;
        this.imageStorage = imageStorage;
    }

    public Product getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product", id));
    }

    public List<Product> getByName(String query) {
        return repository.findByNameContainingIgnoreCase(query);
    }

    public List<Product> getInStock(boolean inStock) {
        if (inStock)
            return repository.findByStockGreaterThan(0);

        return repository.findByStock(0);
    }

    @Transactional
    public Product reserveStockOrThrow(Long id, int quantity) {
        boolean ok = productStockGateway.tryDecreaseStock(id, quantity);
        if (!ok) {
            int availableStock = repository.findById(id)
                    .map(Product::getStock)
                    .orElse(0);
            throw new OutOfStockException(id, quantity, availableStock);
        }

        return getById(id);
    }

    public List<Product> getPriceBetween(int from, int to) {
        if (from > to)
            throw new InvalidPriceRangeException(from, to);
        return repository.findByPriceBetween(from, to);
    }

    public List<Product> getAll() {
        return repository.findAll();
    }

    public Product create(final String name, int price, int stock) {
        return repository.save(new Product(name, price, stock));
    }

    @Transactional
    public void delete(Long id) {
        Product product = getById(id);
        deleteImage(id);
        repository.delete(product);
    }

    @Transactional
    public Product update(Long id, UpdateProductRequest req) {
        Product product = getById(id);

        product.setName(req.name());
        product.setPrice(req.price());
        product.setStock(req.stock());

        repository.save(product);

        return product;
    }

    public Product uploadImage(long id, MultipartFile file) {
        if (file.isEmpty())
            throw new IllegalArgumentException("Uploaded file is empty");
        long maxBytes = 5L * 1024 * 1024;
        long actualBytes = file.getSize();

        if (actualBytes > maxBytes) {
            throw new ImageTooLargeException(maxBytes, actualBytes);
        }

        String contentType = file.getContentType();
        if (contentType == null)
            throw new InvalidImageTypeException(contentType,
                    List.of("image/png", "image/jpeg", "image/jpg", "image/webp"));

        String extension = switch (contentType) {
            case "image/png" -> ".png";
            case "image/jpeg" -> ".jpeg";
            case "image/jpg" -> ".jpg";
            case "image/webp" -> ".webp";
            default -> throw new InvalidImageTypeException(contentType,
                    List.of("image/png", "image/jpeg", "image/jpg", "image/webp"));
        };

        Product product = getById(id);

        String imageKey = "products/" + id + "/main" + extension;

        if (product.getImageKey() != null && !product.getImageKey().equals(imageKey))
            deleteImage(id);

        imageStorage.save(file, imageKey);

        product.setImageKey(imageKey);

        return repository.save(product);
    }

    @Transactional
    public Product deleteImage(long id) {
        Product product = getById(id);
        String oldKey = product.getImageKey();

        if (oldKey == null)
            return product;

        product.setImageKey(null);
        repository.save(product);

        try {
            imageStorage.delete(oldKey);
        } catch (Exception e) {
            log.warn(
                    "Failed to delete image from storage. productId={}, imageKey={}",
                    id,
                    oldKey,
                    e);
        }
        return product;
    }
}
