package com.hibernate.jpa.orders.service;

import com.hibernate.jpa.orders.domain.Product;
import com.hibernate.jpa.orders.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product saveProduct(Product product) {
        return productRepository.saveAndFlush(product);
    }

    @Override
    @Transactional
    // or Caused by: java.sql.SQLException: Cannot execute statement in a READ ONLY transaction.
    public Product updateQuantityOnHandStock(Long id, Integer quantityOnHand) {
        Product storedProduct = productRepository.findById(id)
                .orElseThrow();
        storedProduct.setQuantityOnHand(quantityOnHand);
        return productRepository.saveAndFlush(storedProduct);
    }

    @Override
    public Product getById(Long id) {
        return productRepository.findById(id).orElseThrow();
    }
}
