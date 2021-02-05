package com.kurier.model;

import com.kurier.model.enums.CourierState;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Courier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private CourierState state;
    @OneToMany
    private List<Package> packageList = new ArrayList<>();

    public Courier(Long id, String name, String surname, CourierState state, List<Package> packageList) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.state = state;
        this.packageList = packageList;
    }

    public Courier(String name, String surname) {
        this.name = name;
        this.surname = surname;
        this.state = CourierState.OUT;
    }

    public Courier(){
        this.state = CourierState.OUT;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public CourierState getState() {
        return state;
    }

    public void setState(CourierState state) {
        this.state = state;
    }

    public List<Package> getPackageList() {
        return packageList;
    }

    public void setPackageList(List<Package> packageList) {
        this.packageList = packageList;
    }
}
