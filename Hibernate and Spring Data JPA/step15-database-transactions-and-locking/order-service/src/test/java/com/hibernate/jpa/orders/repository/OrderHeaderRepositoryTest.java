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

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("dev")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderHeaderRepositoryTest {

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
        customer.setCustomerName("new Customer");
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


    @Test
    void testOrderDeleteCascade() {
        // create order, persist and tests
        OrderHeader orderHeader = new OrderHeader();
        Customer customer = new Customer();
        customer.setCustomerName("New Customer");
        Customer savedCustomer = customerRepository.save(customer);
        orderHeader.setCustomer(savedCustomer);

        OrderLine orderLine = new OrderLine();
        orderLine.setQuantityOrdered(12);
        orderLine.setProduct(product);

        orderHeader.addOrderLine(orderLine);
        OrderApproval orderApproval = new OrderApproval();
        orderApproval.setApprovedBy("Me, myself and I");
        orderHeader.setOrderApproval(orderApproval);

        OrderHeader savedOrder = orderHeaderRepository.save(orderHeader);
        assertNotNull(savedOrder);
        assertNotNull(savedOrder.getId());

        // retrieve order and test
        OrderHeader fetchedOrder = orderHeaderRepository.findById(savedOrder.getId()).orElseThrow();

        assertNotNull(fetchedOrder);
        assertNotNull(fetchedOrder.getId());
        assertNotNull(fetchedOrder.getOrderLines());
        assertEquals(fetchedOrder.getOrderLines().size(), 1);

        // delete
        orderHeaderRepository.deleteById(fetchedOrder.getId());
        orderHeaderRepository.flush();

        //assertThrows(EntityNotFoundException.class, () -> {
        //   OrderHeader storedOrderHeader = orderHeaderRepository.getById(savedOrder.getId());
        //   assertNull(storedOrderHeader);
        //});
        // Optional<OrderHeader> storedOrderHeader = orderHeaderRepository.findById(fetchedOrder.getId());
        // System.out.println(storedOrderHeader.get());
        assertThrows(jakarta.persistence.EntityNotFoundException.class, () -> {
            OrderHeader storedOrderHeader = orderHeaderRepository.getById(savedOrder.getId());
            System.out.println(storedOrderHeader);
        });

    }

    @Test
    void testOrderApproval() {
        OrderHeader orderHeader = new OrderHeader();
        Customer customer = new Customer();
        customer.setCustomerName("New Customer");
        Customer savedCustomer = customerRepository.save(customer);
        orderHeader.setCustomer(savedCustomer);

        OrderLine orderLine = new OrderLine();
        orderLine.setQuantityOrdered(12);
        orderLine.setProduct(product);

        orderHeader.addOrderLine(orderLine);
        OrderApproval orderApproval = new OrderApproval();
        orderApproval.setApprovedBy("Me, myself and I");

        // Now, using cascade persist
        // OrderApproval savedApproval = orderApprovalRepository.save(orderApproval);
        // orderHeader.setOrderApproval(savedApproval);
        orderHeader.setOrderApproval(orderApproval);

        // Hibernate: insert into product (created_date,description,last_modified_date,product_status) values (?,?,?,?)
        //Hibernate: insert into customer (address,city,state,zip_code,created_date,customer_name,email,last_modified_date,phone) values (?,?,?,?,?,?,?,?,?)
        //Hibernate: insert into order_approval (approved_by,created_date,last_modified_date) values (?,?,?)
        //Hibernate: insert into order_header (bill_to_address,bill_to_city,bill_to_state,bill_to_zip_code,created_date,customer_id,last_modified_date,order_approval_id,order_status,shipping_address,shipping_city,shipping_state,shipping_zip_code) values (?,?,?,?,?,?,?,?,?,?,?,?,?)
        //Hibernate: insert into order_line (created_date,last_modified_date,order_header_id,product_id,quantity_ordered) values (?,?,?,?,?)
        OrderHeader savedOrder = orderHeaderRepository.save(orderHeader);
        assertNotNull(savedOrder);
        assertNotNull(savedOrder.getId());

        OrderHeader fetchedOrder = orderHeaderRepository.findById(savedOrder.getId()).orElseThrow();

        assertNotNull(fetchedOrder);
        assertNotNull(fetchedOrder.getId());
        assertNotNull(fetchedOrder.getOrderLines());
        assertEquals(fetchedOrder.getOrderLines().size(), 1);


    }


    @Test
    void testSaveOrderWithOrderLinesAndProductUsingHelper() {
        OrderHeader orderHeader = new OrderHeader();
        Customer customer = new Customer();
        customer.setCustomerName("New Customer");
        Customer savedCustomer = customerRepository.save(customer);
        orderHeader.setCustomer(savedCustomer);

        OrderLine orderLine = new OrderLine();
        orderLine.setQuantityOrdered(12);
        orderLine.setProduct(product);

        // we use helper method instead
        //orderHeader.setOrderLines(Set.of(orderLine));
        //orderLine.setOrderHeader(orderHeader);
        orderHeader.addOrderLine(orderLine);

        // circular reference should be fixed : endless loop
        // Hibernate: insert into product (created_date,description,last_modified_date,product_status) values (?,?,?,?)
        // Hibernate: insert into order_header (bill_to_address,bill_to_city,bill_to_state,bill_to_zip_code,created_date,customer,last_modified_date,order_status,shipping_address,shipping_city,shipping_state,shipping_zip_code) values (?,?,?,?,?,?,?,?,?,?,?,?)
        // Hibernate: insert into order_line (created_date,last_modified_date,order_header_id,product_id,quantity_ordered) values (?,?,?,?,?)
        // we could also persist each orderLine
        OrderHeader savedOrder = orderHeaderRepository.save(orderHeader);
        assertNotNull(savedOrder);
        assertNotNull(savedOrder.getId());

        OrderHeader fetchedOrder = orderHeaderRepository.getById(savedOrder.getId());

        assertNotNull(fetchedOrder);
        assertNotNull(fetchedOrder.getId());
        assertNotNull(fetchedOrder.getOrderLines());
        assertEquals(fetchedOrder.getOrderLines().size(), 1);

    }

    @Test
    void testSaveOrderWithOrderLinesAndProduct() {
        OrderHeader orderHeader = new OrderHeader();
        Customer customer = new Customer();
        customer.setCustomerName("New Customer");
        Customer savedCustomer = customerRepository.save(customer);
        orderHeader.setCustomer(savedCustomer);

        OrderLine orderLine = new OrderLine();
        orderLine.setQuantityOrdered(12);
        orderLine.setProduct(product);

        orderHeader.setOrderLines(Set.of(orderLine));
        orderLine.setOrderHeader(orderHeader);

        // circular reference should be fixed : endless loop
        // Hibernate: insert into product (created_date,description,last_modified_date,product_status) values (?,?,?,?)
        // Hibernate: insert into order_header (bill_to_address,bill_to_city,bill_to_state,bill_to_zip_code,created_date,customer,last_modified_date,order_status,shipping_address,shipping_city,shipping_state,shipping_zip_code) values (?,?,?,?,?,?,?,?,?,?,?,?)
        // Hibernate: insert into order_line (created_date,last_modified_date,order_header_id,product_id,quantity_ordered) values (?,?,?,?,?)
        // we could also persist each orderLine
        OrderHeader savedOrder = orderHeaderRepository.save(orderHeader);
        assertNotNull(savedOrder);
        assertNotNull(savedOrder.getId());

        OrderHeader fetchedOrder = orderHeaderRepository.getById(savedOrder.getId());

        assertNotNull(fetchedOrder);
        assertNotNull(fetchedOrder.getId());
        assertNotNull(fetchedOrder.getOrderLines());
        assertEquals(fetchedOrder.getOrderLines().size(), 1);

    }

    @Test
    void testSaveOrderWithOrderLines() {
        OrderHeader orderHeader = new OrderHeader();
        Customer customer = new Customer();
        customer.setCustomerName("New Customer");
        Customer savedCustomer = customerRepository.save(customer);
        orderHeader.setCustomer(savedCustomer);

        OrderLine orderLine = new OrderLine();
        orderLine.setQuantityOrdered(12);

        orderHeader.setOrderLines(Set.of(orderLine));
        orderLine.setOrderHeader(orderHeader);

        // circular reference should be fixed : endless loop
        // Hibernate: insert into order_header (bill_to_address,bill_to_city,bill_to_state,bill_to_zip_code,created_date,customer,last_modified_date,order_status,shipping_address,shipping_city,shipping_state,shipping_zip_code) values (?,?,?,?,?,?,?,?,?,?,?,?)
        // Hibernate: insert into order_line (created_date,last_modified_date,order_header_id,quantity_ordered) values (?,?,?,?)
        // we could also persist each orderLine
        OrderHeader savedOrder = orderHeaderRepository.save(orderHeader);
        assertNotNull(savedOrder);
        assertNotNull(savedOrder.getId());

        OrderHeader fetchedOrder = orderHeaderRepository.getById(savedOrder.getId());

        assertNotNull(fetchedOrder);
        assertNotNull(fetchedOrder.getId());
        assertNotNull(fetchedOrder.getOrderLines());
        assertEquals(fetchedOrder.getOrderLines().size(), 1);
    }

    @Test
    void testSaveOrder() {
        OrderHeader orderHeader = new OrderHeader();
        Customer customer = new Customer();
        customer.setCustomerName("New Customer");
        Customer savedCustomer = customerRepository.save(customer);
        orderHeader.setCustomer(savedCustomer);

        OrderHeader savedOrder = orderHeaderRepository.save(orderHeader);

        assertNotNull(savedOrder);
        assertNotNull(savedOrder.getId());

        OrderHeader fetchedOrder = orderHeaderRepository.getById(savedOrder.getId());

        assertNotNull(fetchedOrder);
        assertNotNull(fetchedOrder.getId());
    }

    @Test
    void testEquals() {
        OrderHeader obj1 = new OrderHeader();
        obj1.setId(1L);

        OrderHeader obj2 = new OrderHeader();
        obj2.setId(1L);

        assertEquals(obj1, obj2);
    }

    @Test
    void testNotEquals() {
        OrderHeader obj1 = new OrderHeader();
        obj1.setId(1L);

        OrderHeader obj2 = new OrderHeader();
        obj2.setId(2L);

        assertNotEquals(obj1, obj2);
    }
}