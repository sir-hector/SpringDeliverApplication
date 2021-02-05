package com.kurier.repository;

import com.kurier.model.Customer;
import com.kurier.model.Package;
import com.kurier.model.enums.PackageState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PackageRepository extends JpaRepository <Package, Long> {

    List<Package> findAllBySender(Customer customer);

    List<Package> findAllBySenderAndState(Customer customer, PackageState state);
}
