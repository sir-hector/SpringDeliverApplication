package com.kurier.service;


import com.kurier.Exception.ClientNotFoundException;
import com.kurier.Exception.CourierAlreadyLoginExcepction;
import com.kurier.Exception.CourierMustDeliverPackage;
import com.kurier.model.Courier;
import com.kurier.model.Package;
import com.kurier.model.enums.CourierState;
import com.kurier.repository.CourierRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourierService {

    private CourierRepository courierRepository;
    private PackageService packageService;

    public CourierService(CourierRepository courierRepository, PackageService packageService) {

        this.courierRepository = courierRepository;
        this.packageService = packageService;
    }

    public Courier save(Courier courier) {
        return courierRepository.save(courier);
    }

    public List<Courier> findAll() {
        return courierRepository.findAll();
    }

    public Optional<Courier> findbyId (Long courierID){

        Optional<Courier> byId = courierRepository.findById(courierID);
        if(byId.isEmpty()){
            throw new ClientNotFoundException(courierID);
        }
        return byId;
    }

    public Courier changeState(Long id, CourierState state){
        Optional<Courier> courier= findbyId(id);
        if (state == courier.get().getState() && state == CourierState.FREE) throw new CourierAlreadyLoginExcepction(id);
        if (state == courier.get().getState() && state == CourierState.OUT) throw new CourierAlreadyLoginExcepction(id);
        if (state == CourierState.OUT && courier.get().getState() ==CourierState.BUSY) throw new CourierMustDeliverPackage(id);
        courier.get().setState(state);
        return courierRepository.save(courier.get());
    }

    public Courier givePackagetoOtherCurier(Long idp, Long idk, Long idk2){
        Package pack = packageService.findbyId(idp).get();
        Courier given = findbyId(idk).get();
        Courier taken = findbyId(idk2).get();

        if (pack.getKurierId().getId() != given.getId()) throw new RuntimeException();
        if (taken.getState() == CourierState.BUSY) throw new CourierMustDeliverPackage(taken.getId());

        pack.setKurierId(taken);
        changeState(given.getId(), CourierState.FREE);
        return changeState(taken.getId(), CourierState.BUSY);
    }

    public void deleteByID(Long id) {
        courierRepository.deleteById(id);
    }

    public void deleteAll() {
        courierRepository.deleteAll();
    }
}
