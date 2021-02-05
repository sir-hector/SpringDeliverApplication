package com.kurier.controller;

import com.kurier.model.Courier;
import com.kurier.model.Package;
import com.kurier.model.enums.CourierState;
import com.kurier.service.CourierService;
import com.kurier.service.PackageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("courier")
public class CourierController {

    private CourierService courierService;
    private PackageService packageService;

    public CourierController(CourierService courierService, PackageService packageService) {
        this.courierService = courierService;
        this.packageService = packageService;
    }

    @PostMapping("add")
    ResponseEntity<Courier> save (@RequestBody Courier courier){
        return ResponseEntity.ok(courierService.save(courier));
    }
    @GetMapping ("findAll")
    ResponseEntity<List<Courier>> findAll (){
        return ResponseEntity.ok(courierService.findAll());
    }

    @GetMapping ("find/{id}")
    ResponseEntity<Courier> findById(@PathVariable Long id){
        Optional<Courier> byId = courierService.findbyId(id);
        return ResponseEntity.ok(byId.get());
    }

    @DeleteMapping("delete/{id}")
    ResponseEntity<Courier> deleteById(@PathVariable Long id){
        courierService.deleteByID(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("login/{id}")
    ResponseEntity<Courier> changeStateFree (@PathVariable Long id){
        Courier updated = courierService.changeState(id, CourierState.FREE);
        return ResponseEntity.ok(courierService.save(updated));
    }

    @PostMapping("logout/{id}")
    ResponseEntity<Courier> changeStateOut (@PathVariable Long id){
        Courier updated = courierService.changeState(id, CourierState.OUT);
        return ResponseEntity.ok(courierService.save(updated));
    }

    // Picking up package by courier//
    @PostMapping("pickup/{idc}/{idp}")
    ResponseEntity<Package> pickUpPackage (@PathVariable Long idc, @PathVariable Long idp) {
        return ResponseEntity.ok(packageService.pickup(idc, idp));
    }

    // deliver package by courier //
    @PostMapping("deliver/{idc}/{idp}")
    ResponseEntity<Package> deliverPackage (@PathVariable Long idc, @PathVariable Long idp) {
        return ResponseEntity.ok( packageService.deliverPackage(idc,idp));
    }

    @PostMapping("givePackage/{idp}/from/{idk1}/to/{idk2}")
    ResponseEntity<Courier> deliverPackage (@PathVariable Long idp, @PathVariable Long idk1, @PathVariable Long idk2) {
        return ResponseEntity.ok(courierService.givePackagetoOtherCurier(idp,idk1,idk2));
    }
}
