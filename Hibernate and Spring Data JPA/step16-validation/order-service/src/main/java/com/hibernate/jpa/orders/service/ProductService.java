package com.hibernate.jpa.orders.service;

import com.hibernate.jpa.orders.domain.Product;

public interface ProductService {
    Product saveProduct(Product product);
    Product updateQuantityOnHandStock(Long id, Integer quantityOnHand);

    Product getById(Long id);
}
