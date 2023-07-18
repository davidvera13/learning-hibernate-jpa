package com.hibernate.jpa.orders.repository;

import com.hibernate.jpa.orders.domain.OrderHeader;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("dev")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderHeaderRepositoryTest {

    @Autowired
    OrderHeaderRepository orderHeaderRepository;

    @Test
    void testSaveOrder() {
        OrderHeader orderHeader = new OrderHeader();
        orderHeader.setCustomer("New Customer");
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