package com.example.shopbackend.demo.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderSummaryDto(
        Long orderId,
        BigDecimal orderValue,
        int itemCount,
        Status status,
        LocalDateTime created) {

    public static OrderSummaryDto from(Order order) {
        return new OrderSummaryDto(
                order.getId(),
                order.getTotalValue(),
                order.getItems().size(),
                order.getStatus(),
                order.getCreatedAt());
    }
}
