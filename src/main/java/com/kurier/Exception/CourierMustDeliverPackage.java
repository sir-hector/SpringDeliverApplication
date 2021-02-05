package com.kurier.Exception;

public class CourierMustDeliverPackage extends  RuntimeException{
    public CourierMustDeliverPackage(Long id){
        super ("Kurier " + id  + "musi dostarczyć przesyłkę");
    }
}
