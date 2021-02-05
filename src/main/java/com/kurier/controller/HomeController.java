package com.kurier.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Projekt aplikacja kurierska s20687");
    }
}
