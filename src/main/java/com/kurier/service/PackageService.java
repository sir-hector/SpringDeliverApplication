package com.kurier.service;


import com.kurier.Exception.ClientNotFoundException;
import com.kurier.Exception.CourierBusyExcepcion;
import com.kurier.Exception.PackageNotFoundException;
import com.kurier.model.Courier;
import com.kurier.model.Customer;
import com.kurier.model.Package;
import com.kurier.model.enums.CourierState;
import com.kurier.model.enums.PackageState;
import com.kurier.repository.PackageRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PackageService {

    private PackageRepository packageRepository;
    private CourierService courierService;


    public PackageService(PackageRepository packageRepository, @Lazy CourierService courierService) {
        this.packageRepository = packageRepository;
        this.courierService = courierService;

    }

    public Package save (Package pac) {
        return packageRepository.save(pac);
    }

    public List<Package> findAll() {
        return packageRepository.findAll();
    }


    public Optional<Package> findbyId(Long idp) {
        Optional<Package> byId = packageRepository.findById(idp);
        if(byId.isEmpty()){
            throw new PackageNotFoundException("Paczka nie istnieje");
        }
        return byId;
    }

    public Package pickup(Long idc, Long idp){
        Courier courier = courierService.findbyId(idc).get();
        Package packa = findbyId(idp).get();
        if(!(courier.getState() == CourierState.FREE)){
            throw new CourierBusyExcepcion(idc, courier.getName());
        }
        if(!(packa.getState() == PackageState.NEW)){
            throw new PackageNotFoundException("Paczka została pobrana lub jest juz dostarczona");
        }
        packa.setState(PackageState.IN_DELIVERY);
        courierService.changeState(idc, CourierState.BUSY);
        packa.setKurierId(courier);
        return packageRepository.save(packa);
    }

    public Package deliverPackage(Long idc, Long idp) {
        Courier courier = courierService.findbyId(idc).get();
        Package packa = findbyId(idp).get();
        if((packa.getState() == PackageState.NEW)){
            throw new PackageNotFoundException("Paczka nie została pobrana");
        }
        if(!(packa.getKurierId() == courier)){
            throw new PackageNotFoundException("Paczka nie została pobrana przez tego kuriera");
        }
        packa.setState(PackageState.DELIVERED);
        return packageRepository.save(packa);
    }

    public List<Package>  findAllbyCustomerandState(Customer customer, PackageState state) {
        return packageRepository.findAllBySenderAndState(customer, state);
    }

    public List<Package> findAllbyCustomer(Customer customer) {
        return packageRepository.findAllBySender(customer);
    }


    public void deleteAll() {
        packageRepository.deleteAll();
    }
}
