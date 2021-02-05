package com.kurier.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CourierBusyExcepctionAdvice {

    @ResponseBody
    @ExceptionHandler({CourierBusyExcepcion.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String Courier (CourierBusyExcepcion ex){
        return ex.getMessage();
    }
}