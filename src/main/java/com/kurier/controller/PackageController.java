package com.kurier.controller;

import com.kurier.model.Customer;
import com.kurier.model.Package;
import com.kurier.service.PackageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("package")
public class PackageController {

    private PackageService packageService;

    public PackageController(PackageService packageService) {
        this.packageService = packageService;
    }

    @GetMapping("findAll")
    ResponseEntity <List<Package>> findAll(){
        return ResponseEntity.ok(packageService.findAll());
    }

    @GetMapping("find/{id}")
    ResponseEntity <Package> find(@PathVariable Long id){
        return ResponseEntity.ok(packageService.findbyId(id).get());
    }

}
