package com.hibernate.jpa.orders.repository;

import com.hibernate.jpa.orders.domain.OrderHeader;
import com.hibernate.jpa.orders.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByDescription(String description);
}
