package com.hibernate.jpa;

import com.hibernate.jpa.repository.BookRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class Step05SpringDataPrimaryKeysApplicationTests {
    @Autowired
    BookRepository bookRepository;

    @Test
    void bookRepositoryTest() {
        long count = bookRepository.count();
        System.out.println(count);
        assertThat(count).isGreaterThanOrEqualTo(2);
    }
}
