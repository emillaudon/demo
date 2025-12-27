package com.example.shopbackend.demo.order;

import java.time.LocalDateTime;
import java.util.List;

import com.example.shopbackend.demo.orderitem.OrderItemDto;

public record OrderDto(
        Long id,
        LocalDateTime createdAt,
        Status status,
        List<OrderItemDto> items) {

    public static OrderDto from(Order order) {
        return new OrderDto(
                order.getId(),
                order.getCreatedAt(),
                order.getStatus(),
                order.getItems().stream().map(OrderItemDto::from).toList());
    }
}
