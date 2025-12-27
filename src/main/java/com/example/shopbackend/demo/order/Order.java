package com.example.shopbackend.demo.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.shopbackend.demo.common.InvalidStatusException;
import com.example.shopbackend.demo.common.InvalidStatusTransitionException;
import com.example.shopbackend.demo.orderitem.OrderItem;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    private Long id;
    private LocalDateTime createdAt;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> items = new ArrayList<>();

    protected Order() {
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.status = Status.CREATED;
    }

    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }

    public void removeItem(OrderItem item) {
        items.remove(item);
        item.removeOrder();
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

    public void changeStatus(Status newStatus) {
        if (newStatus == null)
            throw new InvalidStatusException("null", Status.allowedValues());
        if (!this.status.canTransitionTo(newStatus))
            throw new InvalidStatusTransitionException(this.status, newStatus, this.status.allowedNext());
        this.status = newStatus;
    }

    public List<OrderItem> getItems() {
        return items;
    }
}
