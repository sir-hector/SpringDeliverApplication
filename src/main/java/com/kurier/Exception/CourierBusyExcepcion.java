package com.kurier.Exception;

public class CourierBusyExcepcion extends RuntimeException{
    public CourierBusyExcepcion(Long id, String imie){
        super("Kurier " + id + " "+ imie + " ma już pobraną przesyłkę");
    }
}
