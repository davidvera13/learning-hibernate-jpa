package com.hibernate.jpa.orders.repository;

import com.hibernate.jpa.orders.domain.Customer;
import com.hibernate.jpa.orders.domain.OrderHeader;
import com.hibernate.jpa.orders.domain.OrderLine;
import com.hibernate.jpa.orders.domain.Product;
import com.hibernate.jpa.orders.enums.ProductStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
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

    Product product;

    @BeforeEach
    void setup() {
        Product newProduct = new Product();
        newProduct.setProductStatus(ProductStatus.NEW);
        newProduct.setDescription("test product");
        product = productRepository.saveAndFlush(newProduct);
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