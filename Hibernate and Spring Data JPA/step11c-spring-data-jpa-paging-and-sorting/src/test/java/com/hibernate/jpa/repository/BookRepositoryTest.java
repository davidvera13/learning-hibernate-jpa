package com.hibernate.jpa.repository;

import com.hibernate.jpa.dao.impl.AuthorDaoImpl;
import com.hibernate.jpa.domain.Book;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("dev")
@DataJpaTest
@ComponentScan(basePackages = { "com.hibernate.jpa.dao" })
@EnableJpaRepositories(basePackages = {"com.hibernate.jpa.repository"})
@EntityScan(basePackages = {"com.hibernate.jpa.domain"})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    @Autowired
    BookRepository bookRepository;

    @Test
    void findByIsbn() {
        Book book = new Book();
        book.setIsbn("132456" + RandomString.make());
        book.setTitle("ISBN test");
        Book saved = bookRepository.save(book);
        Book fetched = bookRepository.findByIsbn(book.getIsbn());
        assertThat(fetched).isNotNull();
    }

    @Test
    void findById() {
        Book book = bookRepository.findById(1);
        System.out.println(book.getId() + " " + book.getAuthor() + " " + book.getTitle());
    }

    @Test
    void findByIdNotFound() {
        assertThrows(EmptyResultDataAccessException.class, () -> {
            bookRepository.findById(-52);
        });
    }

    @Test
    void findByTitle() {
        Optional<Book> optional = bookRepository.findByTitle("Clean Codes");
        if(optional.isPresent()) {
            Book book  = optional.get();
            System.out.println(book.getId() + " " + book.getAuthor() + "  " + book.getTitle());
        }
    }

    @Test
    void getByTitle() {
        assertNull(bookRepository.getByTitle(null));
    }

    // stream
    @Test
    void findAllByTitleNotNull() {
        AtomicInteger count = new AtomicInteger();
        bookRepository.findAllByTitleNotNull().forEach(book ->
                count.incrementAndGet());
        assertThat(count.get()).isGreaterThan(5);
    }
    // async: Future, CompletableFuture,
    @Test
    void queryBookByTitle()
            throws ExecutionException, InterruptedException {
        Future<Book> bookFuture = bookRepository.queryBookByTitle("Clean Code");
        Book book = bookFuture.get();
        assertThat(book).isNotNull();
        System.out.println(book.getId() + " " + book.getAuthor() + "  " + book.getTitle());
    }

    @Test
    void findBookByTitleWithQuery() {
        Book book = bookRepository.findBookByTitleWithQuery("Clean Code");
        assertThat(book).isNotNull();
        System.out.println(book.getId() + " " + book.getAuthor() + "  " + book.getTitle());
    }

    @Test
    void findBookByTitleWithNameParameterQuery() {
        Book book = bookRepository.findBookByTitleWithNameParameterQuery("Clean Code");
        assertThat(book).isNotNull();
        System.out.println(book.getId() + " " + book.getAuthor() + "  " + book.getTitle());
    }

    @Test
    void findBookByTitleWithHqlQuery() {
        Book book = bookRepository.findBookByTitleWithHqlQuery("Clean Code");
        assertThat(book).isNotNull();
        System.out.println(book.getId() + " " + book.getAuthor() + "  " + book.getTitle());
    }

    @Test
    void jpaNamedQuery() {
        Book book = bookRepository.jpaNamedQuery("Clean Code");
        assertThat(book).isNotNull();
        System.out.println(book.getId() + " " + book.getAuthor() + "  " + book.getTitle());
    }


}