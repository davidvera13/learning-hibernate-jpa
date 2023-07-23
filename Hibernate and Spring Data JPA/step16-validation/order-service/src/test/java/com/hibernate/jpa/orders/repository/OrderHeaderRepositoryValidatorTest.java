package com.hibernate.jpa.orders.repository;

import com.hibernate.jpa.orders.domain.*;
import com.hibernate.jpa.orders.enums.ProductStatus;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("dev")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderHeaderRepositoryValidatorTest {

    @Autowired
    OrderHeaderRepository orderHeaderRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    OrderApprovalRepository orderApprovalRepository;

    Product product;

    @BeforeEach
    void setup() {
        Product newProduct = new Product();
        newProduct.setProductStatus(ProductStatus.NEW);
        newProduct.setDescription("test product");
        product = productRepository.saveAndFlush(newProduct);
    }

    @Test
    void testOrderDeleteOrphans() {
        OrderHeader orderHeader = new OrderHeader();
        Customer customer = new Customer();
        // Without validator
        // 2023-07-23T22:40:39.151+02:00  WARN 37276 --- [           main] o.h.engine.jdbc.spi.SqlExceptionHelper   : SQL Error: 1406, SQLState: 22001
        //2023-07-23T22:40:39.152+02:00 ERROR 37276 --- [           main] o.h.engine.jdbc.spi.SqlExceptionHelper   : Data truncation: Data too long for column 'customer_name' at row 1
        // org.springframework.dao.DataIntegrityViolationException:
        // could not execute statement [Data truncation: Data too long for column 'customer_name' at row 1]
        // With validator
        // List of constraint violations:[
        //	ConstraintViolationImpl{interpolatedMessage='la longueur doit être comprise entre 2 et 50', propertyPath=customerName, rootBeanClass=class com.hibernate.jpa.orders.domain.Customer, messageTemplate='{org.hibernate.validator.constraints.Length.message}'}
        //]

        customer.setCustomerName("new Customer with a very very very very but very long name over 50 chars");
        customer.setPhone("this is a too long phone number ... it should fail");

        Address address = new Address();
        address.setCity("A nice city with a very very long name too, it should throw exception");
        customer.setAddress(address);
        orderHeader.setCustomer(customerRepository.save(customer));

        OrderLine orderLine = new OrderLine();
        orderLine.setQuantityOrdered(3);
        orderLine.setProduct(product);

        OrderApproval orderApproval = new OrderApproval();
        orderApproval.setApprovedBy("me");
        orderHeader.setOrderApproval(orderApproval);

        orderHeader.addOrderLine(orderLine);
        OrderHeader savedOrder = orderHeaderRepository.saveAndFlush(orderHeader);

        System.out.println("order saved and flushed");

        orderHeaderRepository.deleteById(savedOrder.getId());
        orderHeaderRepository.flush();

        assertThrows(EntityNotFoundException.class, () -> {
            OrderHeader fetchedOrder = orderHeaderRepository.getById(savedOrder.getId());
            System.out.println(fetchedOrder);
        });
    }
}