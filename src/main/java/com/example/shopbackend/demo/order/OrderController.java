package com.example.shopbackend.demo.order;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.shopbackend.demo.orderitem.OrderItemDto;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(params = { "!status", "!from", "!to" })
    public List<OrderDto> getAll() {
        return orderService.getAll()
                .stream()
                .map(OrderDto::from)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getById(@PathVariable Long id) {
        Order order = orderService.getById(id);
        return ResponseEntity.ok(OrderDto.from(order));
    }

    @GetMapping("/{id}/items")
    public List<OrderItemDto> getOrderItems(@PathVariable Long id) {
        Order order = orderService.getById(id);
        return order.getItems()
                .stream()
                .map(OrderItemDto::from)
                .toList();
    }

    @GetMapping(params = "status")
    public List<OrderDto> getByStatus(@RequestParam(required = false) String status) {
        System.out.println(orderService.getByStatus(status));
        return orderService.getByStatus(status)
                .stream()
                .map(OrderDto::from)
                .toList();
    }

    @GetMapping(params = { "from", "to" })
    public List<OrderDto> getCreatedBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to) {
        return orderService.getCreatedBetween(from, to)
                .stream()
                .map(OrderDto::from)
                .toList();
    }

    @PostMapping
    public ResponseEntity<OrderDto> create(@Valid @RequestBody CreateOrderRequest request) {
        Order savedOrder = orderService.create(request);

        URI location = URI.create("/orders/" + savedOrder.getId());
        return ResponseEntity.created(location).body(OrderDto.from(savedOrder));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderDto> updateStatus(@PathVariable Long id,
            @Valid @RequestBody UpdateOrderStatusRequest request) {
        Order updated = orderService.updateStatus(id, request);
        return ResponseEntity.ok(OrderDto.from(updated));
    }

}
