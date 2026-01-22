package com.example.shopbackend.demo.order;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.shopbackend.demo.common.InvalidDateRangeException;
import com.example.shopbackend.demo.common.NotFoundException;
import com.example.shopbackend.demo.orderitem.CreateOrderItemRequest;
import com.example.shopbackend.demo.orderitem.OrderItem;
import com.example.shopbackend.demo.product.Product;
import com.example.shopbackend.demo.product.ProductService;
import com.example.shopbackend.demo.user.User;
import com.example.shopbackend.demo.user.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class OrderService {

    private final ProductService productService;
    private final OrderRepository repository;
    private final UserRepository userRepository;

    public OrderService(ProductService productService, OrderRepository repository, UserRepository userRepository) {
        this.productService = productService;
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public List<Order> getAll() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));
    }

    public List<Order> getMine() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        long userId = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED))
                .getId();

        return repository.findByUserId(userId);
    }

    public Order getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Order", id));
    }

    public List<Order> getByStatus(String rawStatus) {
        Status status = Status.parseStatus(rawStatus);
        return repository.findByStatus(status);
    }

    public List<Order> getCreatedBetween(LocalDateTime from, LocalDateTime to) {
        if (from.isAfter(to))
            throw new InvalidDateRangeException(from, to);

        return repository.findByCreatedAtBetween(from, to);
    }

    @Transactional
    public Order updateStatus(Long id, UpdateOrderStatusRequest request) {
        Order order = getById(id);

        Status status = Status.parseStatus(request.status());

        order.changeStatus(status);

        if (status == Status.CANCELLED)
            for (OrderItem orderItem : order.getItems()) {
                Product product = orderItem.getProduct();
                product.increaseStock(orderItem.getQuantity());
            }

        return order;
    }

    @Transactional
    public Order cancel(Long id) {
        Order order = getById(id);

        order.changeStatus(Status.CANCELLED);

        for (OrderItem orderItem : order.getItems()) {
            Product product = orderItem.getProduct();
            product.increaseStock(orderItem.getQuantity());
        }
        return order;
    }

    @Transactional
    public Order create(CreateOrderRequest request) {
        Order order = new Order();

        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        order.setUser(user);

        for (CreateOrderItemRequest requestItem : request.items()) {
            Product product = productService.getById(requestItem.productId());

            productService.reserveStockOrThrow(product.getId(), requestItem.quantity());
            OrderItem orderItem = new OrderItem(
                    product, requestItem.quantity(), product.getPrice());
            order.addItem(orderItem);
        }

        return repository.save(order);
    }
}
