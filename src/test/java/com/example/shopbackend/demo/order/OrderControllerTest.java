package com.example.shopbackend.demo.order;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.shopbackend.demo.orderitem.CreateOrderItemRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.http.MediaType;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @Autowired
    OrderController orderController;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    OrderService orderService;

    @Test
    void getAll_shouldReturn200() throws Exception {
        when(orderService.getAll()).thenReturn(List.of());

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk());
    }

    @Test
    void getById_existingId_shouldReturn200() throws Exception {
        Order order = mock(Order.class);
        when(order.getId()).thenReturn(1L);
        when(orderService.getById(1L)).thenReturn(order);

        mockMvc.perform(get("/orders/1"))
                .andExpect(status().isOk());
    }

    @Test
    void getById_wrongTypeId_shouldReturn400() throws Exception {
        mockMvc.perform(get("/orders/a"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getOrderItems_existingId_shouldReturn200() throws Exception {
        Order order = mock(Order.class);
        when(order.getId()).thenReturn(1L);
        when(orderService.getById(1L)).thenReturn(order);

        mockMvc.perform(get("/orders/1/items"))
                .andExpect(status().isOk());
    }

    @Test
    void getOrderItems_wrongTypeId_shouldReturn400() throws Exception {
        mockMvc.perform(get("/orders/b/items"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getByStatus_existingStatus_shouldReturn200() throws Exception {
        when(orderService.getByStatus("CREATED")).thenReturn(List.of());

        mockMvc.perform(get("/orders")
                .param("status", "CREATED"))
                .andExpect(status().isOk());
    }

    @Test
    void getCreatedBetween_existingValues_shouldReturn200() throws Exception {
        when(orderService.getCreatedBetween(any(LocalDateTime.class), any(LocalDateTime.class)))
                .thenReturn(List.of());

        mockMvc.perform(get("/orders")
                .param("from", "2025-10-10T10:20:11")
                .param("to", "2025-11-11T11:11:11"))
                .andExpect(status().isOk());
    }

    @Test
    void getOrderSummary_existingId_shouldReturn200() throws Exception {
        Order order = mock(Order.class);
        when(orderService.getById(1L)).thenReturn(order);

        mockMvc.perform(get("/orders/1/summary"))
                .andExpect(status().isOk());
    }

    @Test
    void getOrderSummary_wrongTypeId_shouldReturn400() throws Exception {
        Order order = mock(Order.class);
        when(orderService.getById(1L)).thenReturn(order);

        mockMvc.perform(get("/orders/b/summary"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_shouldReturn201() throws Exception {
        CreateOrderItemRequest item = new CreateOrderItemRequest(1L, 10);
        CreateOrderRequest req = new CreateOrderRequest(List.of(item));
        Order order = mock(Order.class);
        when(orderService.create(req)).thenReturn(order);

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());
    }

    @Test
    void create_invalidRequestBody() throws Exception {
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content("f"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void cancel_existingOrder_shouldReturn200() throws Exception {
        Order order = mock(Order.class);
        when(orderService.cancel(1L)).thenReturn(order);

        mockMvc.perform(post("/orders/1/cancel"))
                .andExpect(status().isOk());
    }

    @Test
    void cancel_wrongIdType_shouldReturn400() throws Exception {
        mockMvc.perform(post("/orders/b/cancel"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateStatus_existingOrder_shouldReturn200() throws Exception {
        Order order = mock(Order.class);
        when(orderService.updateStatus(1L, new UpdateOrderStatusRequest("PAID"))).thenReturn(order);
        UpdateOrderStatusRequest req = new UpdateOrderStatusRequest("PAID");
        mockMvc.perform(patch("/orders/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());
    }

    @Test
    void updateStatus_invalidRequest_shouldReturn400() throws Exception {
        mockMvc.perform(patch("/orders/1/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content("\"req\""))
                .andExpect(status().isBadRequest());
    }

}
