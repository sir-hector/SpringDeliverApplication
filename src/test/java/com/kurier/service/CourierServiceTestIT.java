package com.kurier.service;

import com.kurier.Exception.ClientNotFoundException;
import com.kurier.Exception.CourierAlreadyLoginExcepction;
import com.kurier.Exception.CourierMustDeliverPackage;
import com.kurier.model.Courier;
import com.kurier.model.enums.CourierState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
public class CourierServiceTestIT {

    @Autowired
    private CourierService courierService;

    @BeforeEach
    void cleanUp(){
        courierService.deleteAll();
    }

    @Test
    void shouldNotFindAnyone(){
        List<Courier> all = courierService.findAll();
        assertThat(all).isEmpty();
    }

    @Test
    void shouldFindSomeone(){
        courierService.save(new Courier("Karol", "Kraus"));
        List<Courier> all = courierService.findAll();
        assertThat(all).isNotEmpty();
    }

    @Test
    void shouldSaveCourier(){
        Courier save = courierService.save(new Courier("Karol", "Kraus"));
        assertThat(save.getId()).isPositive();
    }

    @Test
    void shouldFindById(){
        Courier courier = courierService.save(new Courier("Karol", " Kraus"));
        assertThat(courierService.findbyId(courier.getId())).isNotEmpty();
    }

    @Test
    void shouldThrowExceptionClientNotFound(){
        assertThatExceptionOfType(ClientNotFoundException.class).isThrownBy(()->courierService.findbyId(10L));
    }

    @Test
    void schouldThrowExceptionCourierAlreadyLogin(){
        Courier courier = courierService.save(new Courier(1L,"Karol", " Kraus",CourierState.FREE,List.of()));
        assertThatExceptionOfType(CourierAlreadyLoginExcepction.class).isThrownBy(()->courierService.changeState(courier.getId(),CourierState.FREE));
    }
    @Test
    void schouldThrowExceptionCourierAlreadyLogout(){
        Courier courier = courierService.save(new Courier(1L,"Karol", " Kraus",CourierState.OUT,List.of()));
        assertThatExceptionOfType(CourierAlreadyLoginExcepction.class).isThrownBy(()->courierService.changeState(courier.getId(),CourierState.OUT));
    }
    @Test
    void schouldChangeStateFromOUTtoFREE(){
        Courier courier = courierService.save(new Courier(1L,"Karol", " Kraus",CourierState.OUT,List.of()));
        courier = courierService.changeState(courier.getId(),CourierState.FREE);
        assertThat(courier.getState()).isEqualTo(CourierState.FREE);
    }
    @Test
    void schouldChangeStateFromFREEtoOUT(){
        Courier courier = courierService.save(new Courier(1L,"Karol", " Kraus",CourierState.FREE,List.of()));
        courier = courierService.changeState(courier.getId(),CourierState.OUT);
        assertThat(courier.getState()).isEqualTo(CourierState.OUT);
    }
    @Test
    void schouldChangeStateFromFREEtoBUSY(){
        Courier courier = courierService.save(new Courier(1L,"Karol", " Kraus",CourierState.FREE,List.of()));
        courier = courierService.changeState(courier.getId(),CourierState.BUSY);
        assertThat(courier.getState()).isEqualTo(CourierState.BUSY);
    }
    @Test
    void schouldChangeStateFromBUSYtoFREE(){
        Courier courier = courierService.save(new Courier(1L,"Karol", " Kraus",CourierState.BUSY,List.of()));
        courier = courierService.changeState(courier.getId(),CourierState.FREE);
        assertThat(courier.getState()).isEqualTo(CourierState.FREE);
    }
    @Test
    void schouldThrowExceptionCourierMustDeliverPackage(){
        Courier courier = courierService.save(new Courier(1L,"Karol", " Kraus",CourierState.BUSY,List.of()));
        assertThatExceptionOfType(CourierMustDeliverPackage.class).isThrownBy(()->courierService.changeState(courier.getId(),CourierState.OUT));
    }
}
