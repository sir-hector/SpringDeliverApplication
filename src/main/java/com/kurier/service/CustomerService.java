package com.kurier.service;

import com.kurier.Exception.ClientNotFoundException;
import com.kurier.model.Courier;
import com.kurier.model.Customer;
import com.kurier.model.Package;
import com.kurier.model.enums.PackageState;
import com.kurier.repository.CustomerRespository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private CustomerRespository customerRespository;
    private PackageService packageService;

    public CustomerService (CustomerRespository customerRespository, PackageService packageService){

        this.customerRespository = customerRespository;
        this.packageService = packageService;
    }

    public List<Customer> findAll() {
        return customerRespository.findAll();
    }

    public Customer save(Customer customer) {
        return customerRespository.save(customer);
    }

    public Optional<Customer> findbyID(Long id) {
        Optional<Customer> byId = customerRespository.findById(id);
        if(byId.isEmpty()){
            throw new ClientNotFoundException(id);
        }
        return byId;
    }

    public void deleteByID(Long id) {
        customerRespository.deleteById(id);
    }

    public void deleteAll(){
        customerRespository.deleteAll();
    }

    public Package cancelPackage(Long idc, Long idp) {
        Customer sender = findbyID(idc).get();
        Package pack = packageService.findbyId(idp).get();
        if(pack.getSender() != sender && pack.getState() != PackageState.IN_DELIVERY) throw new RuntimeException();
        pack.setState(PackageState.CANCELED);
        return packageService.save(pack);
    }
}
