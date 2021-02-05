package com.kurier.Exception;


public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(Long id) {
        super("Nie znaleziono klienta " + id);
    }
}
