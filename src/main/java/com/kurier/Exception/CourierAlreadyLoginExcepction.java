package com.kurier.Exception;

public class CourierAlreadyLoginExcepction extends RuntimeException{
    public CourierAlreadyLoginExcepction(Long id) {
        super("Jeteś juz zalogowany " + id);
    }
}
