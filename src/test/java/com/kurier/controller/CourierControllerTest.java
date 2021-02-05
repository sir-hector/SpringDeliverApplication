package com.kurier.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kurier.model.Courier;
import com.kurier.model.Customer;
import com.kurier.model.Package;
import com.kurier.model.enums.CourierState;
import com.kurier.model.enums.PackageState;
import com.kurier.service.CourierService;
import com.kurier.service.CustomerService;
import com.kurier.service.PackageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc

class CourierControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private CourierService courierService;
    @Autowired
    private PackageService packageService;
    @Autowired
    private CustomerService customerService;

    @Test
    void shouldReturnEmptyList() throws Exception {
        mockMvc.perform(get("/courier/findAll"))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    void shouldAdd() throws Exception {
        Courier courier = new Courier("Karol", "Kraus");
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(courier);
        mockMvc.perform(post("/courier/add").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"id\":1,\"name\":\"Karol\",\"surname\":\"Kraus\",\"state\":\"OUT\",\"packageList\":[]}")));
    }

    @Test
    void shouldNotFoundId() throws Exception{
        mockMvc.perform(get("/courier/find/3"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    @Test
    void shouldoundId() throws Exception{
        Courier courier = new Courier("Karol", "Kraus");
        courierService.save(courier);
        mockMvc.perform(get("/courier/find/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"id\":1,\"name\":\"Karol\",\"surname\":\"Kraus\",\"state\":\"OUT\",\"packageList\":[]}")));
    }
    @Test
    void shouldLoginCourier() throws Exception{
        Courier courier = new Courier("Karol", "Kraus");
        courierService.save(courier);
        mockMvc.perform(post("/courier/login/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo("{\"id\":1,\"name\":\"Karol\",\"surname\":\"Kraus\",\"state\":\"FREE\",\"packageList\":[]}")));
    }

    @Test
    void shouldLogoutCourier() throws Exception{
        Courier courier = new Courier(1L,"Karol", " Kraus",CourierState.FREE, List.of());
        courierService.save(courier);
        mockMvc.perform(post("/courier/logout/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    void shouldNotLogoutCourier() throws Exception{
        Courier courier = new Courier(1L,"Karol", " Kraus",CourierState.FREE, List.of());
        courierService.save(courier);
        mockMvc.perform(post("/courier/logout/3"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    @Test
    void shouldPickUp() throws Exception{
        Customer customer = customerService.save(new Customer(50L,"Karol","kraus","AAAA", List.of()));
        Courier courier =courierService.save(new Courier(1L,"Karol", " Kraus",CourierState.FREE, List.of()));
        packageService.save(new Package(1L, customer,courier, PackageState.NEW,20));
        mockMvc.perform(post("/courier/pickup/1/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    void shouldNotPickUp() throws Exception{
        Customer customer = customerService.save(new Customer(50L,"Karol","kraus","AAAA", List.of()));
        Courier courier =courierService.save(new Courier(1L,"Karol", " Kraus",CourierState.FREE, List.of()));
        packageService.save(new Package(1L, customer,courier, PackageState.NEW,20));
        mockMvc.perform(post("/courier/pickup/1/2"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
    @Test
    void shouldDeliverPackage() throws Exception{
        Customer customer = customerService.save(new Customer(50L,"Karol","kraus","AAAA", List.of()));
        Courier courier =courierService.save(new Courier(1L,"Karol", " Kraus",CourierState.FREE, List.of()));
        packageService.save(new Package(1L, customer,courier, PackageState.IN_DELIVERY,20));
        mockMvc.perform(post("/courier/deliver/1/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }
    @Test
    void shouldNotDeliverPackage() throws Exception{
        Customer customer = customerService.save(new Customer(50L,"Karol","kraus","AAAA", List.of()));
        Courier courier =courierService.save(new Courier(1L,"Karol", " Kraus",CourierState.FREE, List.of()));
        packageService.save(new Package(1L, customer,courier, PackageState.IN_DELIVERY,20));
        mockMvc.perform(post("/courier/deliver/1/2"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}