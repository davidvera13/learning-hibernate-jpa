package com.hibernate.jpa.dao;

import com.hibernate.jpa.domain.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookDao {
    Book findByIsbn(String isbn);

    Book getById(long id);

    Book findBookByTitle(String title);

    Book saveNewBook(Book book);

    Book updateBook(Book book);

    void deleteBookById(long id);

    List<Book> findAll();

    List<Book> findAllBooks(int limit, int offset);

    List<Book> findAllBooksPageable(Pageable pageable);

    List<Book> findAllBooksSortingByTitle(Pageable pageable);

    Page<Book> findAllBooksSortingByTitlePage(Pageable pageable);
}
