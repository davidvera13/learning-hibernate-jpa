package com.hibernate.jpa.dao;

import com.hibernate.jpa.domain.Book;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BookDao {
    Book findByIsbn(String isbn);

    Book getById(Long id);

    Book findBookByTitle(String title);

    Book saveNewBook(Book book);

    Book updateBook(Book book);

    void deleteBookById(Long id);

    List<Book> findAll();
    Book findBookByTitleNamedQuery(String title);

    Book findBookByTitleNative(String title);

    List<Book> findAllBooks(int limit, int offset);

    List<Book> findAllBooksPageable(Pageable pageable);

    List<Book> findAllBooksSortingByTitle(Pageable pageable);
}
