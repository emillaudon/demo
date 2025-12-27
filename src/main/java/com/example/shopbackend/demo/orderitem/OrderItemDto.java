package com.example.shopbackend.demo.orderitem;

public record OrderItemDto(
        Long productId,
        int quantity,
        int unitPrice) {
    public static OrderItemDto from(OrderItem item) {
        return new OrderItemDto(
                item.getProduct().getId(),
                item.getQuantity(),
                item.getUnitPrice());
    }
}
