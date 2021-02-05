package com.kurier.service;

import com.kurier.Exception.ClientNotFoundException;
import com.kurier.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
public class CustomerServiceIT {

    @Autowired
    private CustomerService customerService;

    @BeforeEach
    void cleanUp(){
        customerService.deleteAll();
    }

    @Test
    void shouldNotFindAnyone(){
        List<Customer> all = customerService.findAll();
        assertThat(all).isEmpty();
    }

    @Test
    void shouldFindSomeone(){
        customerService.save(new Customer());
        List<Customer> all = customerService.findAll();
        assertThat(all).isNotEmpty();
    }

    @Test
    void shouldSaveCustomer(){
        Customer customer = customerService.save(new Customer());
        assertThat(customer.getId()).isPositive();
    }

    @Test
    void shouldFindById(){
        Customer customer = customerService.save(new Customer());
        assertThat(customerService.findbyID(customer.getId())).isNotEmpty();
    }
    @Test
    void shouldThrowExceptionClientNotFound(){
        assertThatExceptionOfType(ClientNotFoundException.class).isThrownBy(()->customerService.findbyID(10L));
    }


}
