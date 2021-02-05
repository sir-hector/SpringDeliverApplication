package com.kurier.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class CourierMustDeliverAdvice {

    @ResponseBody
    @ExceptionHandler(CourierMustDeliverPackage.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String mesege (CourierMustDeliverPackage ex) {
        return  ex.getMessage();
    }
}
