package com.hibernate.jpa.dao;

import com.hibernate.jpa.dao.impl.AuthorDaoImpl;
import com.hibernate.jpa.dao.impl.BookDaoImpl;
import com.hibernate.jpa.domain.Author;
import com.hibernate.jpa.domain.Book;
import jakarta.persistence.EntityNotFoundException;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ActiveProfiles("dev")
@DataJpaTest
//@ComponentScan(basePackages = { "com.hibernate.jpa.dao" })
@Import({AuthorDaoImpl.class, BookDaoImpl.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookDaoTest {
    @Autowired
    BookDao bookDao;

    @Test
    void findByIsbn() {
        Book book = new Book();
        book.setIsbn("132456" + RandomString.make());
        book.setTitle("ISBN test");
        Book saved = bookDao.saveNewBook(book);
        Book fetched = bookDao.findByIsbn(book.getIsbn());
        assertThat(fetched).isNotNull();

        System.out.println(fetched.getId() + " " + fetched.getIsbn() + " " + fetched.getTitle());
    }

    @Test
    void getById() {
        Book book = bookDao.getById(3L);

        assertThat(book.getId()).isNotNull();
    }

    @Test
    void findBookByTitle() {
        Book book = bookDao.findBookByTitle("Clean Code");
        assertThat(book).isNotNull();
    }


    @Test
    void findBookByTitleNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            bookDao.findBookByTitle("Clean Codes");
        });
    }

    @Test
    void saveNewBook() {
        Book book = new Book();
        book.setIsbn("1234");
        book.setPublisher("Self");
        book.setTitle("my book");
        Book saved = bookDao.saveNewBook(book);

        assertThat(saved).isNotNull();
    }

    @Test
    void updateBook() {
        Book book = new Book();
        book.setIsbn("1234");
        book.setPublisher("Self");
        book.setTitle("my book");
        book.setAuthor(new Author("John", "FK"));
        Book saved = bookDao.saveNewBook(book);

        saved.setTitle("New Book");
        bookDao.updateBook(saved);

        Book fetched = bookDao.getById(saved.getId());

        assertThat(fetched.getTitle()).isEqualTo("New Book");
    }

    @Test
    void deleteBookById() {
        Book book = new Book();
        book.setIsbn("1234");
        book.setPublisher("Self");
        book.setTitle("my book");
        book.setAuthor(new Author("John", "FK"));
        Book saved = bookDao.saveNewBook(book);

        bookDao.deleteBookById(saved.getId());
        Book deleted = bookDao.getById(saved.getId());
        assertThat(deleted).isNull();
    }


    @Test
    void findAll() {
        List<Book> books = bookDao.findAll();
        assertThat(books).isNotNull();
        assertThat(books.size()).isGreaterThan(0);
        books.forEach(book -> System.out.println(book.getId() + " " + book.getAuthor() + " " + book.getTitle()));
    }
}