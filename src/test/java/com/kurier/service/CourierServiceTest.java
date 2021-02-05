package com.kurier.service;

import com.kurier.model.Courier;
import com.kurier.model.enums.CourierState;
import com.kurier.repository.CourierRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CourierServiceTest {

    @Mock
    private CourierRepository courierRepository;

    @InjectMocks
    private CourierService courierService;

    @Test
    void save() {
        Courier courier = new Courier("Karol","Kraus");
        Mockito.when(courierRepository.save(courier)).thenReturn(new Courier(50L,"Karol","kraus", CourierState.BUSY,List.of()));
        Courier save = courierService.save(courier);
        assertThat(save.getId()).isEqualTo(50L);
    }

    @Test
    void findAll() {
        Mockito.when(courierRepository.findAll()).thenReturn(List.of());
        List<Courier> couriers = courierService.findAll();
        assertThat(couriers).isEmpty();
    }

    @Test
    void findbyId() {
        Mockito.when(courierRepository.findById(1L)).thenReturn(Optional.of(new Courier()));
        Optional<Courier> courier = courierService.findbyId(1L);
        assertThat(courier).isNotEmpty();
    }

    @Test
    void changeState() {
        Courier courier = new Courier(50L,"Karol","kraus", CourierState.BUSY,List.of());
        Mockito.when(courierRepository.findById(courier.getId())).thenReturn(Optional.of(courier));
        Mockito.when(courierRepository.save(courier)).thenReturn(courier);
        Courier save = courierService.changeState(courier.getId(),courier.getState());
        assertThat(save.getId()).isEqualTo(50L);
    }

    @Test
    void deleteByID() {
        courierRepository.deleteById(1L);
        Mockito.verify(courierRepository,Mockito.times(1)).deleteById(1L);
    }
}