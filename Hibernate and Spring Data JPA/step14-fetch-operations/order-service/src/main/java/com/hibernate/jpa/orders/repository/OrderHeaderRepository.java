package com.hibernate.jpa.orders.repository;

import com.hibernate.jpa.orders.domain.Customer;
import com.hibernate.jpa.orders.domain.OrderHeader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface OrderHeaderRepository extends JpaRepository<OrderHeader, Long> {
    List<OrderHeader> findAllByCustomer(Customer customer);
}
