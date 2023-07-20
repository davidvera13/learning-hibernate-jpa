package com.hibernate.jpa.orders;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

//@Disabled // not configured...
@ActiveProfiles("dev")
@SpringBootTest
class OrderServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
