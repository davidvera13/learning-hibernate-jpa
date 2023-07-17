package com.hibernate.jpa.dao;

import com.hibernate.jpa.dao.impl.AuthorDaoImpl;
import com.hibernate.jpa.dao.impl.BookDaoImpl;
import com.hibernate.jpa.domain.Author;
import com.hibernate.jpa.domain.Book;
import net.bytebuddy.utility.RandomString;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    void findBookByTitleNamedQuery() {
        Book book = bookDao.findBookByTitleNamedQuery("Clean Code");

    }

    @Test
    void findBookByTitleNative() {
        Book book = bookDao.findBookByTitleNative("Clean Code");
        assertThat(book).isNotNull();
    }

    @Test
    void findAll() {
        List<Book> books = bookDao.findAll();
        assertThat(books).isNotNull();
        assertThat(books.size()).isGreaterThan(0);
        books.forEach(book -> System.out.println(book.getId() + " " + book.getAuthor() + " " + book.getTitle()));
    }

    // let's now do paging on query
    @Test
    void findAllBooksPaginated() {
        List<Book> books;
        books = bookDao.findAllBooks(10, 0);
        assertThat(books).isNotNull();
        assertThat(books.size()).isEqualTo(10);
        System.out.println(books.size());

        books = bookDao.findAllBooks(10, 10);
        assertThat(books).isNotNull();
        assertThat(books.size()).isLessThanOrEqualTo(10);
        System.out.println(books.size());

        books = bookDao.findAllBooks(10, 20);
        assertThat(books).isNotNull();
        assertThat(books.size()).isLessThanOrEqualTo(10);
        System.out.println(books.size());


        books = bookDao.findAllBooks(10, 120);
        assertThat(books).isNotNull();
        assertThat(books.size()).isEqualTo(0);
        System.out.println(books.size());

    }

    // using pageable
    @Test
    void findAllBooksPageable() {
        Pageable pageable;

        List<Book> books;
        pageable = PageRequest.of(0, 10);
        books = bookDao.findAllBooksPageable(pageable);
        assertThat(books).isNotNull();
        assertThat(books.size()).isEqualTo(10);
        System.out.println(books.size());

        books = bookDao.findAllBooksPageable(PageRequest.of(1, 10));
        assertThat(books).isNotNull();
        assertThat(books.size()).isLessThanOrEqualTo(10);
        System.out.println(books.size());

        books = bookDao.findAllBooksPageable(PageRequest.of(2, 10));
        assertThat(books).isNotNull();
        assertThat(books.size()).isLessThanOrEqualTo(10);
        System.out.println(books.size());


        books = bookDao.findAllBooksPageable(PageRequest.of(20, 100));
        assertThat(books).isNotNull();
        assertThat(books.size()).isEqualTo(0);
        System.out.println(books.size());
    }

    // using pageable
    @Test
    void findAllBooksSortingByTitle() {
        Pageable pageable;

        List<Book> books;
        pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("title")));
        books = bookDao.findAllBooksSortingByTitle(pageable);
        assertThat(books).isNotNull();
        assertThat(books.size()).isEqualTo(10);
        System.out.println(books.size());

        books = bookDao.findAllBooksSortingByTitle(
                PageRequest.of(0, 10, Sort.by(Sort.Order.desc("title"))));
        assertThat(books).isNotNull();
        assertThat(books.size()).isLessThanOrEqualTo(10);
        System.out.println(books.size());

        books = bookDao.findAllBooksSortingByTitle(
                PageRequest.of(0, 10, Sort.by(Sort.Order.desc("title"))));
        assertThat(books).isNotNull();
        assertThat(books.size()).isLessThanOrEqualTo(10);
        System.out.println(books.size());
    }
}