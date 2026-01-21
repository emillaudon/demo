package com.example.shopbackend.demo.orderitem;

public record OrderItemDto(
        Long productId,
        int quantity,
        int unitPrice,
        String imageUrl) {
    public static OrderItemDto from(OrderItem item) {
        String imageUrl = item.getProduct().getImageKey() == null
                ? null
                : "/api/images/" + item.getProduct().getImageKey();
        return new OrderItemDto(
                item.getProduct().getId(),
                item.getQuantity(),
                item.getUnitPrice(),
                imageUrl);
    }
}
