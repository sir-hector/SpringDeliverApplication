package com.kurier.service;

import com.kurier.Exception.ClientNotFoundException;
import com.kurier.Exception.CourierBusyExcepcion;
import com.kurier.Exception.PackageNotFoundException;
import com.kurier.model.Courier;
import com.kurier.model.Customer;
import com.kurier.model.Package;
import com.kurier.model.enums.CourierState;
import com.kurier.model.enums.PackageState;
import com.sun.xml.bind.v2.runtime.reflect.Lister;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
public class PackageServiceIT {

    @Autowired
    private PackageService packageService;

    @Autowired
    private CourierService courierService;

    @Autowired
    private CustomerService customerService;

    @BeforeEach
    void cleanup() {
        packageService.deleteAll();
    }

    @Test
    void shouldSavePackage(){
        Package packag = packageService.save(new Package());
        assertThat(packag.getId()).isPositive();
    }

    @Test
    void shouldNotFindAnyone(){
        List<Package> packageList = packageService.findAll();
        assertThat(packageList).isEmpty();
    }

    @Test
    void shouldFindSomeone(){
        packageService.save(new Package());
        List<Package> packageList = packageService.findAll();
        assertThat(packageList).isNotEmpty();
    }

    @Test
    void shouldFindPackageByCustomer(){
        Courier courier = courierService.save(new Courier(50L,"Karol","kraus", CourierState.BUSY,List.of()));
        Customer customer = customerService.save(new Customer(50L,"Karol","kraus","AAAAA",List.of()));
        packageService.save(new Package(1L, customer,courier));
        List<Package> all = packageService.findAllbyCustomer(customer);
        assertThat(all).isNotEmpty();
    }
    @Test
    void shouldNotFindPackageByCustomer(){
        Courier courier = courierService.save(new Courier(50L,"Karol","kraus", CourierState.BUSY,List.of()));
        Customer customer = customerService.save(new Customer(50L,"Karol","kraus","AAAAA",List.of()));
        Customer customer2 = customerService.save(new Customer(20L,"Karol","kraus","AAAAA",List.of()));
        packageService.save(new Package(1L, customer,courier));
        List<Package> all = packageService.findAllbyCustomer(customer2);
        assertThat(all).isEmpty();
    }

    @Test
    void schouldfindById(){
        Package pack = packageService.save(new Package());
        assertThat(packageService.findbyId(pack.getId())).isNotEmpty();
    }
    @Test
    void shoulThrowExceptionPackageNotFoundException(){
        assertThatExceptionOfType(PackageNotFoundException.class).isThrownBy(()->packageService.findbyId(10L));
    }

    @Test
    void shouldPickUpThePackage(){
        Courier courier = courierService.save(new Courier(50L,"Karol","kraus", CourierState.FREE,List.of()));
        Package pack = packageService.save(new Package());
        packageService.pickup(courier.getId(), pack.getId());
        assertThat(pack.getState() == PackageState.IN_DELIVERY);
    }

    @Test
    void shouldThrowExceptionCourierBusyExcepcion(){
        Courier courier = courierService.save(new Courier(50L,"Karol","kraus", CourierState.BUSY,List.of()));
        Package pack = packageService.save(new Package());
        assertThatExceptionOfType(CourierBusyExcepcion.class).isThrownBy(()->packageService.pickup(courier.getId(), pack.getId()));
    }

    @Test
    void shouldThrowPackageNotFoundExceptionPackageDelivered(){
        Courier courier = courierService.save(new Courier(50L,"Karol","kraus", CourierState.FREE,List.of()));
        Customer customer = customerService.save(new Customer(50L,"Karol","kraus","AAAAA",List.of()));
        Package pack = packageService.save(new Package(1L, customer,courier,PackageState.DELIVERED,20));
        assertThatExceptionOfType(PackageNotFoundException.class).isThrownBy(()->packageService.pickup(courier.getId(), pack.getId()));
    }

    @Test
    void shouldThrowPackageNotFoundExceptionPackageInDelivery(){
        Courier courier = courierService.save(new Courier(50L,"Karol","kraus", CourierState.FREE,List.of()));
        Customer customer = customerService.save(new Customer(50L,"Karol","kraus","AAAAA",List.of()));
        Package pack = packageService.save(new Package(1L, customer,courier,PackageState.IN_DELIVERY,20));
        assertThatExceptionOfType(PackageNotFoundException.class).isThrownBy(()->packageService.pickup(courier.getId(), pack.getId()));
    }

    @Test
    void shouldDeliverPackage(){
        Courier courier = courierService.save(new Courier(50L,"Karol","kraus", CourierState.FREE,List.of()));
        Customer customer = customerService.save(new Customer(50L,"Karol","kraus","AAAAA",List.of()));
        Package pack = packageService.save(new Package(1L, customer,courier,PackageState.IN_DELIVERY,20));
        Package delivered = packageService.deliverPackage(courier.getId(),pack.getId());
        assertThat(delivered.getState()).isEqualTo(PackageState.DELIVERED);
    }

    @Test
    void shouldThrowExceptionWrongCourier(){
        Courier courier = courierService.save(new Courier(50L,"Karol","kraus", CourierState.FREE,List.of()));
        Courier courier2 = courierService.save(new Courier(20L,"Karol","kraus", CourierState.FREE,List.of()));
        Customer customer = customerService.save(new Customer(50L,"Karol","kraus","AAAAA",List.of()));
        Package pack = packageService.save(new Package(1L, customer,courier,PackageState.IN_DELIVERY,20));
        assertThatExceptionOfType(PackageNotFoundException.class).isThrownBy(()->packageService.deliverPackage(courier2.getId(),pack.getId()));
    }
    @Test
    void shouldThrowExceptionWrongPackageStatus(){
        Courier courier = courierService.save(new Courier(50L,"Karol","kraus", CourierState.FREE,List.of()));
        Customer customer = customerService.save(new Customer(50L,"Karol","kraus","AAAAA",List.of()));
        Package pack = packageService.save(new Package(1L, customer,courier,PackageState.NEW,20));
        assertThatExceptionOfType(PackageNotFoundException.class).isThrownBy(()->packageService.deliverPackage(courier.getId(),pack.getId()));
    }

    @Test
    void shouldFindPackageByCustomerandState(){
        Courier courier = courierService.save(new Courier(50L,"Karol","kraus", CourierState.BUSY,List.of()));
        Customer customer = customerService.save(new Customer(50L,"Karol","kraus","AAAAA",List.of()));
        packageService.save(new Package(1L, customer,courier,PackageState.NEW,20));
        List<Package> all = packageService.findAllbyCustomerandState(customer,PackageState.NEW);
        assertThat(all).isNotEmpty();
    }
    @Test
    void shouldNotFindPackageByCustomerCustomerandState(){
        Courier courier = courierService.save(new Courier(50L,"Karol","kraus", CourierState.BUSY,List.of()));
        Customer customer = customerService.save(new Customer(50L,"Karol","kraus","AAAAA",List.of()));
        packageService.save(new Package(1L, customer,courier,PackageState.NEW,20));
        List<Package> all = packageService.findAllbyCustomerandState(customer,PackageState.DELIVERED);
        assertThat(all).isEmpty();
    }



}
