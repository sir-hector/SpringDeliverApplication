package com.kurier.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kurier.model.Courier;
import com.kurier.model.Customer;
import com.kurier.model.Package;
import com.kurier.model.enums.PackageState;
import com.kurier.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerService customerService;


    @Test
    void shouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/customer/findAll"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void shouldAdd() throws Exception {
        Customer customer = new Customer(1L,"Karol", "Kraus","aaaa", List.of());
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(customer);
        mockMvc.perform(post("/customer/add").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"id\":1,\"name\":\"Karol\",\"surname\":\"Kraus\",\"address\":\"aaaa\",\"packageList\":[]}")));
    }

    @Test
    void shouldNotFoundId() throws Exception{
        mockMvc.perform(get("/customer/find/3"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    @Test
    void shoulfoundId() throws Exception{
        Customer customer = new Customer(1L,"Karol", "Kraus","aaaa", List.of());
        customerService.save(customer);
        mockMvc.perform(get("/customer/find/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"id\":1,\"name\":\"Karol\",\"surname\":\"Kraus\",\"address\":\"aaaa\",\"packageList\":[]}")));
    }

    @Test
    void shouldSendPackage() throws Exception {
        customerService.save(new Customer(1L,"Karol", "Kraus","aaaa", List.of()));
        Package pack = new Package();
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(pack);
        mockMvc.perform(post("/customer/send/1").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk());
    }
}