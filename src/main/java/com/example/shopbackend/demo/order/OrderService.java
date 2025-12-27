package com.example.shopbackend.demo.order;

import org.springframework.stereotype.Service;

import com.example.shopbackend.demo.orderitem.CreateOrderItemRequest;
import com.example.shopbackend.demo.orderitem.OrderItem;
import com.example.shopbackend.demo.product.Product;
import com.example.shopbackend.demo.product.ProductService;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

    private final ProductService productService;
    private final OrderRepository repository;

    public OrderService(ProductService productService, OrderRepository repository) {
        this.productService = productService;
        this.repository = repository;
    }

    @Transactional
    public Order create(CreateOrderRequest request) {
        Order order = new Order();
        for (CreateOrderItemRequest requestItem : request.items()) {
            Product product = productService.getById(requestItem.productId());

            product.decreaseStock(requestItem.quantity());
            OrderItem orderItem = new OrderItem(
                    product, requestItem.quantity(), product.getPrice());
            order.addItem(orderItem);
        }

        return repository.save(order);
    }
}
