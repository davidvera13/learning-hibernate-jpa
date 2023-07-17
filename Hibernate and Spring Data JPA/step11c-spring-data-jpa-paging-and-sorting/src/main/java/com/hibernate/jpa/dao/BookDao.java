package com.hibernate.jpa.dao;

import com.hibernate.jpa.domain.Book;

import java.util.List;

public interface BookDao {
    Book findByIsbn(String isbn);

    Book getById(long id);

    Book findBookByTitle(String title);

    Book saveNewBook(Book book);

    Book updateBook(Book book);

    void deleteBookById(long id);

    List<Book> findAll();
}
