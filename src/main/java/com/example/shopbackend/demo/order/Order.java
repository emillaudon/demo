package com.example.shopbackend.demo.order;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class Order {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDateTime createdAt;
    private Status status;

    protected Order() {
        this.createdAt = LocalDateTime.now();
        this.status = Status.CREATED;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Status getStatus() {
        return status;
    }
}
