package com.hibernate.jpa.orders.repository;

import com.hibernate.jpa.orders.domain.OrderHeader;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderHeaderRepository extends JpaRepository<OrderHeader, Long> {
}
