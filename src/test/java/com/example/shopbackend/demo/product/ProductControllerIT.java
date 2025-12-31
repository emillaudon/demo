package com.example.shopbackend.demo.product;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductControllerIT {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    void getById_missingId_shouldReturn404() throws Exception {
        mockMvc.perform(get("/products/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void delete_invalidTypeId_shouldReturn400() throws Exception {
        mockMvc.perform(delete("/products/b"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void delete_missingProduct_shouldReturn404() throws Exception {
        mockMvc.perform(delete("/products/999999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_missingProduct_shouldReturn404() throws Exception {
        UpdateProductRequest req = new UpdateProductRequest("name", 1, 1);
        mockMvc.perform(put("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());
    }

    @Test
    void update_invalidTypeId_shouldReturn400() throws Exception {
        UpdateProductRequest req = new UpdateProductRequest("name", 1, 1);
        mockMvc.perform(put("/products/b")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void update_invalidRequest_shouldReturn400() throws Exception {
        String req = "r";
        mockMvc.perform(put("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
}
