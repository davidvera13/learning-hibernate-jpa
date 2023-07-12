package com.hibernate.jpa;


import com.hibernate.jpa.repository.BookRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ActiveProfiles("dev")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataJpaTest
@ComponentScan(basePackages = { "com.hibernate.jpa.bootstrap" })
// by default it will use h2, we want to test mysql
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MysqlIntegrationTest {
    @Autowired
    BookRepository bookRepository;

    @Test
    @Order(1)
    void jpaMysqlTest() {
        long countBefore = bookRepository.count();
        assertThat(countBefore).isEqualTo(7);
    }
}
