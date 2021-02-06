package com.kurier.service;

import com.kurier.model.Courier;
import com.kurier.model.Customer;
import com.kurier.model.Package;
import com.kurier.model.enums.CourierState;
import com.kurier.model.enums.PackageState;
import com.kurier.repository.PackageRepository;
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
class PackageServiceTest {

    @Mock
    private PackageRepository packageRepository;
    @Mock
    private CourierService courierService;

    @InjectMocks
    private PackageService packageService;

    @Test
    void save(){
        Courier courier = new Courier(50L,"Karol","kraus", CourierState.FREE, List.of());
        Customer customer = new Customer(50L,"Karol","kraus","AAAA", List.of());
        Package pack = new Package();
        Mockito.when(packageRepository.save(pack)).thenReturn(new Package(1L,customer, courier,PackageState.NEW,20));
        Package save = packageService.save(pack);
        assertThat(save.getId()).isEqualTo(1L);
    }

    @Test
    void findAll(){
        Mockito.when(packageRepository.findAll()).thenReturn(List.of());
        List<Package> all = packageService.findAll();
        assertThat(all).isEmpty();
    }

    @Test
    void findbyId(){
        Mockito.when(packageRepository.findById(1L)).thenReturn(Optional.of(new Package()));
        Optional<Package> pack = packageService.findbyId(1L);
        assertThat(pack).isNotEmpty();
    }

    @Test
    void deleteByID(){
        packageRepository.deleteById(1L);
        Mockito.verify(packageRepository,Mockito.times(1)).deleteById(1L);
    }

    @Test
    void findAllbyCustomer(){
        Customer customer = new Customer();
        Mockito.when(packageRepository.findAllBySender(customer)).thenReturn(List.of());
        List<Package> all = packageService.findAllbyCustomer(customer);
        assertThat(all).isEmpty();
    }

    @Test
    void findAllbyCustomerAndState(){
        Customer customer = new Customer();
        Mockito.when(packageRepository.findAllBySenderAndState(customer, PackageState.CANCELED)).thenReturn(List.of());
        List<Package> all = packageService.findAllbyCustomerandState(customer,PackageState.CANCELED);
        assertThat(all).isEmpty();
    }

    @Test
    void PickUp(){
        Courier courier = new Courier(50L,"Karol","kraus", CourierState.FREE, List.of());
        Customer customer = new Customer(50L,"Karol","kraus","AAAA", List.of());
        Package packag = new Package(1L,customer, courier,PackageState.NEW,20);
        Mockito.when(courierService.findbyId(courier.getId())).thenReturn(Optional.of(courier));
        Mockito.when(courierService.changeState(courier.getId(), CourierState.BUSY)).thenReturn((courier));
        Mockito.when(packageRepository.findById(packag.getId())).thenReturn(Optional.of(packag));
        Mockito.when(packageRepository.save(packag)).thenReturn(packag);
        Package pickedup = packageService.pickup(courier.getId(),packag.getId());
        assertThat(pickedup.getState()).isEqualTo(PackageState.IN_DELIVERY);

    }

    @Test
    void deliverPackage(){
        Courier courier = new Courier(50L,"Karol","kraus", CourierState.BUSY,List.of());
        Customer customer = new Customer(50L,"Karol","kraus","AAAA", List.of());
        Package packag = new Package(1L,customer, courier,PackageState.IN_DELIVERY,20);
        Mockito.when(courierService.findbyId(courier.getId())).thenReturn(Optional.of(courier));
        Mockito.when(packageRepository.findById(packag.getId())).thenReturn(Optional.of(packag));
        Mockito.when(packageRepository.save(packag)).thenReturn(packag);
        Package pack = packageService.deliverPackage(courier.getId(),packag.getId());
        assertThat(pack.getId()).isEqualTo(1L);

    }

}