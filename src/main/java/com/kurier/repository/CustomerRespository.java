package com.kurier.repository;

import com.kurier.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRespository extends JpaRepository <Customer, Long> {
}
