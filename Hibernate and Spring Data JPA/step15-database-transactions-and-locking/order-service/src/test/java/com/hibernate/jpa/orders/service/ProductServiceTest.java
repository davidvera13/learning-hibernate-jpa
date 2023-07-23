package com.hibernate.jpa.orders.service;

import com.hibernate.jpa.orders.domain.Product;
import com.hibernate.jpa.orders.enums.ProductStatus;
import com.hibernate.jpa.orders.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;


@ActiveProfiles("dev")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(basePackageClasses = { ProductService.class })
class ProductServiceTest {

    @Autowired
    ProductService productService;

    @Test
    void testSaveProduct() {
        Product product = new Product();
        product.setDescription("description");
        product.setProductStatus(ProductStatus.IN_STOCK);
        Product savedProduct = productService.saveProduct(product);

        assertNotNull(savedProduct);
        assertNotNull(savedProduct.getId());

        Product fetchedProduct = productService
                .getById(savedProduct.getId());

        assertNotNull(fetchedProduct);
        assertNotNull(fetchedProduct.getId());
        assertEquals(fetchedProduct.getProductStatus(), ProductStatus.IN_STOCK);
    }
    @Test
    void addAndUpdateProduct() {
        Product product = new Product();
        product.setDescription("My Product");
        product.setProductStatus(ProductStatus.NEW);

        Product savedProduct = productService.saveProduct(product);

        savedProduct.setQuantityOnHand(25);

        Product savedProduct2 = productService.saveProduct(savedProduct);

        System.out.println(savedProduct2.getQuantityOnHand());
    }

}