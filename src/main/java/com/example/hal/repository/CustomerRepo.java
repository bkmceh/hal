package com.example.hal.repository;

import com.example.hal.model.customers.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CustomerRepo extends JpaRepository<CustomerEntity, Long> {
}
