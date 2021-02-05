package com.kurier.controller;

import com.kurier.Exception.ClientNotFoundException;
import com.kurier.model.Customer;
import com.kurier.model.Package;
import com.kurier.model.enums.PackageState;
import com.kurier.service.CustomerService;
import com.kurier.service.PackageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("customer")
public class CustomerController {

    CustomerService customerService;
    PackageService packageService;

    public CustomerController(CustomerService customerService, PackageService packageService) {
        this.customerService = customerService;
        this.packageService = packageService;
    }

    @GetMapping("findAll")
    ResponseEntity <List<Customer>> findAll(){
        return ResponseEntity.ok(customerService.findAll());
    }


    @GetMapping("find/{id}")
    Customer findById(@PathVariable Long id){
        return customerService.findbyID(id).orElseThrow(() -> new ClientNotFoundException(id));
    }

    @DeleteMapping("{id}")
    ResponseEntity<Customer> deleteById(@PathVariable Long id){
        customerService.deleteByID(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("add")
    ResponseEntity<Customer> save(@RequestBody Customer customer){
        return ResponseEntity.ok(customerService.save(customer));
    }

    @PostMapping("send/{id}")
    ResponseEntity<Package> send(@RequestBody Package pac, @PathVariable Long id){
        pac.setSender(customerService.findbyID(id).get());
        return ResponseEntity.ok(packageService.save(pac));
    }

    // finding all packages belong to the specify customer
    @GetMapping("{id}/findPackages")
    ResponseEntity<List<Package>> findAllYourPackages(@PathVariable Long id){
        return ResponseEntity.ok(packageService.findAllbyCustomer(customerService.findbyID(id).get()));
    }

    // finding all delivered packages belong to the specify customer
    @GetMapping("{id}/findPackagesDelivered")
    ResponseEntity<List<Package>> findAllYourPackagesDelivered(@PathVariable Long id){
        return ResponseEntity.ok(packageService.findAllbyCustomerandState(customerService.findbyID(id).get(), PackageState.DELIVERED));
    }

    @PostMapping("cancel/{idc}/order/{idp}")
    ResponseEntity <Package> cancel(@PathVariable Long idc,@PathVariable Long idp){
        return ResponseEntity.ok(customerService.cancelPackage(idc, idp));
    }
}
