package com.hibernate.jpa.orders.repository;

import com.hibernate.jpa.orders.domain.OrderApproval;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderApprovalRepository extends JpaRepository<OrderApproval, Long> {
}
