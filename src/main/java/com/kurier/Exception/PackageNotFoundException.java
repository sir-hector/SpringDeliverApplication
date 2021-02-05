package com.kurier.Exception;

public class PackageNotFoundException extends RuntimeException{
    public PackageNotFoundException(String id){
        super(id);
    }
}
