package com.kurier.service;

import com.kurier.model.Courier;
import com.kurier.model.Customer;
import com.kurier.model.enums.CourierState;
import com.kurier.repository.CourierRepository;
import com.kurier.repository.CustomerRespository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRespository customerRespository;

    @InjectMocks
    private CustomerService customerService;

    @Test
    void findAll() {
        Mockito.when(customerRespository.findAll()).thenReturn(List.of());
        List<Customer> customers = customerService.findAll();
        assertThat(customers).isEmpty();
    }

    @Test
    void save() {
        Customer customer = new Customer(1L,"Karol","Kraus","AAAA",List.of());
        Mockito.when(customerRespository.save(customer)).thenReturn(new Customer(1L,"Karol","Kraus","AAAA",List.of()));
        Customer save = customerService.save(customer);
        assertThat(save.getId()).isEqualTo(1L);

    }

    @Test
    void findbyID() {
        Mockito.when(customerRespository.findById(1L)).thenReturn(Optional.of(new Customer()));
        Optional<Customer> customer = customerService.findbyID(1L);
        assertThat(customer).isNotEmpty();
    }

    @Test
    void deleteByID() {
        customerRespository.deleteById(1L);
        Mockito.verify(customerRespository,Mockito.times(1)).deleteById(1L);
    }

}