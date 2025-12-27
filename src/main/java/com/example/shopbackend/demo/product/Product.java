package com.example.shopbackend.demo.product;

import com.example.shopbackend.demo.common.OutOfStockException;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Product {
    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private int price;
    private int stock;

    public Product(final String name, final int price, final int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    protected Product() {

    }

    public void decreaseStock(int amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Amount must be > 0");
        if (this.stock < amount)
            throw new OutOfStockException(this.id, amount, this.stock);
        this.stock -= amount;
    }

    public void increaseStock(int amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Amount must be > 0");
        this.stock += amount;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
