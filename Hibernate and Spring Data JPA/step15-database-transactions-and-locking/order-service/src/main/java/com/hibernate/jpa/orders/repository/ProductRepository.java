package com.hibernate.jpa.orders.repository;

import com.hibernate.jpa.orders.domain.OrderHeader;
import com.hibernate.jpa.orders.domain.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByDescription(String description);

    // will lock the entry :
    //    select
    //        id
    //    from
    //        product
    //    where
    //        id=? for update

    @Override
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<Product> findById(Long id);
}
