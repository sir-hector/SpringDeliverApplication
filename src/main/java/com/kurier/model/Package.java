package com.kurier.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kurier.model.enums.PackageState;

import javax.persistence.*;

@Entity
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Customer sender;
    @ManyToOne
    private Courier kurier;
    private PackageState state;
    private double price;

    public Package(Long id, Customer sender, Courier kurier) {
        this.id = id;
        this.sender = sender;
        this.kurier= kurier;
    }

    public Package(Long id, Customer sender, Courier kurier, PackageState state, double price) {
        this.id = id;
        this.sender = sender;
        this.kurier = kurier;
        this.state = state;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Customer getSender() {
        return sender;
    }

    public void setSender(Customer sender) {
        this.sender = sender;
    }


    public Courier getKurierId() {
        return kurier;
    }

    public void setKurierId(Courier kurier) {
        this.kurier = kurier;
    }

    public PackageState getState() {
        return state;
    }

    public void setState(PackageState state) {
        this.state = state;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Package(){
        this.state = PackageState.NEW;
        this.price = Math.random()*100;
    }
}
