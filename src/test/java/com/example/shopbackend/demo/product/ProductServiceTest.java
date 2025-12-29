package com.example.shopbackend.demo.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.shopbackend.demo.common.InvalidPriceRangeException;
import com.example.shopbackend.demo.common.NotFoundException;

import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Autowired
    ProductRepository productRepository;

    @Test
    void getById_existingProduct_returnsProduct() {
        Product product = new Product("Shirt", 199, 19);
        Product saved = productRepository.save(product);

        Product result = productService.getById(saved.getId());

        assertEquals(saved.getId(), result.getId());
        assertEquals("Shirt", result.getName());
    }

    @Test
    void getById_missingProduct_throwsNotFound() {
        assertThrows(NotFoundException.class, () -> {
            productService.getById(99999L);
        });
    }

    @Test
    void getByName_returnsMatchingProducts() {
        Product productOne = new Product("Shirt", 10, 10);
        Product productTwo = new Product("Black Shirt", 20, 20);

        productRepository.save(productOne);
        productRepository.save(productTwo);

        String query = "Shirt";
        List<Product> products = productService.getByName(query);

        assertEquals(2, products.size());
        for (Product product : products) {
            assertTrue(
                    product.getName().toLowerCase().contains(query.toLowerCase()),
                    "Product name did not contain '" + query + "': " + product.getName());
        }
    }

    @Test
    void getInStock_true_returnsOnlyProductsInStock() {
        Product productOne = new Product("Shirt", 10, 0);
        Product productTwo = new Product("Shirt", 10, 1);
        Product productThree = new Product("Shirt", 10, 2);

        productRepository.save(productOne);
        productRepository.save(productTwo);
        productRepository.save(productThree);

        List<Product> productsInStock = productService.getInStock(true);
        assertEquals(2, productsInStock.size());
        for (Product product : productsInStock) {
            assertTrue(product.getStock() > 0, "Expected product to be in stock (> 0) but was " + product.getStock());
        }
    }

    @Test
    void getInStock_false_returnsOnlyOutOfStock() {
        Product productOne = new Product("Shirt", 10, 0);
        Product productTwo = new Product("Shirt", 10, 1);
        Product productThree = new Product("Shirt", 10, 0);

        productRepository.save(productOne);
        productRepository.save(productTwo);
        productRepository.save(productThree);

        List<Product> productsOutOfStock = productService.getInStock(false);
        assertEquals(2, productsOutOfStock.size());
        for (Product product : productsOutOfStock) {
            assertEquals(0, product.getStock());
        }
    }

    @Test
    void getPriceBetween_validRange_returnsProducts() {
        Product productOne = new Product("Shirt", 1, 1);
        Product productTwo = new Product("Shirt", 5, 1);
        Product productThree = new Product("Shirt", 10, 1);

        productRepository.save(productOne);
        productRepository.save(productTwo);
        productRepository.save(productThree);

        int from = 2;
        int to = 9;
        List<Product> productsInPriceRange = productService.getPriceBetween(from, to);
        assertEquals(1, productsInPriceRange.size());
        for (Product product : productsInPriceRange) {
            assertTrue(product.getPrice() >= from && product.getPrice() <= to,
                    "Product price is not between " + from + " and " + to);
        }
    }

    @Test
    void getPriceBetween_fromGreaterThanTo_throwsException() {
        int from = 10;
        int to = 1;
        assertThrows(InvalidPriceRangeException.class, () -> {
            productService.getPriceBetween(from, to);
        });
    }

    @Test
    void create_savesProductWithCorrectValues() {
        String productName = "Shirt";
        int productPrice = 10;
        int productStock = 10;

        Product product = new Product(productName, productPrice, productStock);
        Product savedProduct = productService.create(product.getName(), product.getPrice(), product.getStock());
        Long productId = savedProduct.getId();

        Product loadedProduct = productService.getById(savedProduct.getId());

        assertEquals(productId, loadedProduct.getId());
        assertEquals(productName, loadedProduct.getName());
        assertEquals(productPrice, loadedProduct.getPrice());
        assertEquals(productStock, loadedProduct.getStock());
    }

    @Test
    void delete_existingProduct_removesProduct() {
        Product product = new Product("Shirt", 10, 10);
        Product savedProduct = productRepository.save(product);

        productService.delete(savedProduct.getId());

        assertThrows(NotFoundException.class, () -> {
            productService.getById(savedProduct.getId());
        });
    }

    @Test
    void delete_missingProduct_throwsException() {
        assertThrows(NotFoundException.class, () -> {
            productService.delete(899999999L);
        });
    }

    @Test
    void update_existingProduct_updatesFields() {
        Product product = new Product("Shirt", 10, 10);
        Product savedProduct = productRepository.save(product);

        Long id = savedProduct.getId();
        String newName = "New Shirt";
        int newPrice = 20;
        int newStock = 25;

        UpdateProductRequest req = new UpdateProductRequest(newName, newPrice, newStock);
        Product updatedProduct = productService.update(savedProduct.getId(), req);

        assertEquals(id, updatedProduct.getId());
        assertEquals(newName, updatedProduct.getName());
        assertEquals(newPrice, updatedProduct.getPrice());
        assertEquals(newStock, updatedProduct.getStock());
    }

    @Test
    void update_missingProduct_throwsException() {
        UpdateProductRequest req = new UpdateProductRequest("New Shirt", 299, 5);
        assertThrows(NotFoundException.class, () -> {
            productService.update(9999L, req);
        });
    }
}
