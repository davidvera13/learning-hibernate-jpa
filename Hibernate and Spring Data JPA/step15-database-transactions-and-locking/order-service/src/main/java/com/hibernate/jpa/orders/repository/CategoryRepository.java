package com.hibernate.jpa.orders.repository;

import com.hibernate.jpa.orders.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
