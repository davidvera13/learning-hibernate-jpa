package com.hibernate.jpa;

import com.hibernate.jpa.domain.Book;
import com.hibernate.jpa.repository.BookRepository;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.annotation.Commit;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ComponentScan(basePackages = {"com.hibernate.jpa.bootstrap"})
public class SpringbootJpaBootstrapingInitDataTest {
    @Autowired
    BookRepository bookRepository;

    // we use save and saveAll
    // note: we don't execute the bootstrap code ...
    @Test
    @Order(1)
    // @Rollback(value = false)    // if not, second test fails
    @Commit                     // rollback equals false
    void jpaSpliceTest() {
        long countBefore = bookRepository.count();
        List<Book> savedBooks = new ArrayList<>();
        Book book = bookRepository.save(new Book("My book", "122454678974", "Self"));
        savedBooks.add(book);
        List<Book> books = bookRepository.saveAll(List.of(
                new Book("second book", "123456", "Myself"),
                new Book("third book", "456789", "Myself too")
        ));
        savedBooks.addAll(books);

        long countAfter = bookRepository.count();
        assertThat(countBefore).isLessThan(countAfter);
        System.out.println(countAfter);
        savedBooks.forEach(System.out::println);
    }

    @Test
    @Order(2)
    void jpaSpliceTransaction(){
        long countBefore = bookRepository.count();
        // we use data init
        assertThat(countBefore).isEqualTo(5);
    }
}
