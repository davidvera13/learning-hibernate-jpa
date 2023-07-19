package com.hibernate.jpa.orders.repository;

import com.hibernate.jpa.orders.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
