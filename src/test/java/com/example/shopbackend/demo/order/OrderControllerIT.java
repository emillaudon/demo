package com.example.shopbackend.demo.order;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Test
    void getById_missingId_shouldReturn404() throws Exception {
        mockMvc.perform(get("/orders/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getOrderItems_missingId_shouldReturn404() throws Exception {
        mockMvc.perform(get("/orders/1/items"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getOrderItems_wrongTypeId_shouldReturn404() throws Exception {
        mockMvc.perform(get("/orders/b/items"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getByStatus_invalidStatus_shouldReturn400() throws Exception {
        mockMvc.perform(get("/orders")
                .param("status", "CREATE"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCreatedBetween_invalidDate_shouldReturn400() throws Exception {
        mockMvc.perform(get("/orders")
                .param("from", "2025-10-10T10:20:11")
                .param("to", "2025-11-11T11:"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCreatedBetween_fromAfterTo_shouldReturn400() throws Exception {
        mockMvc.perform(get("/orders")
                .param("from", "2025-10-10T10:20:11")
                .param("to", "2025-09-11T11:11:11"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getOrderSummary_missingId_shouldReturn404() throws Exception {
        mockMvc.perform(get("/orders/1/summary"))
                .andExpect(status().isNotFound());
    }

    @Test
    void cancel_missingOrder_shouldReturn404() throws Exception {
        mockMvc.perform(post("/orders/1/cancel"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateStatus_missingOrder_shouldReturn404() throws Exception {
        mockMvc.perform(patch("/orders/1/updates"))
                .andExpect(status().isNotFound());
    }
}
