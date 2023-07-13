package com.hibernate.jpa.dao.impl;

import com.hibernate.jpa.dao.BookDao;
import com.hibernate.jpa.domain.Book;
import com.hibernate.jpa.repository.BookRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookDaoImpl implements BookDao {
    private final BookRepository bookRepository;

    @Autowired
    public BookDaoImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    // typed query
    @Override
    public Book findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);

    }

    @Override
    public Book getById(long id) {
        return bookRepository.findById(id);

    }


    @Override
    public Book findBookByTitle(String title) {
        return bookRepository.findByTitle(title)
                .orElseThrow(EntityNotFoundException::new);
    }


    // named query
    @Override
    public List<Book> findAll() {
        return bookRepository.findAll();
    }

    @Override
    public Book saveNewBook(Book book) {
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(Book book) {
        Book storedBook = bookRepository.findById(book.getId().longValue());
        storedBook.setTitle(book.getTitle());
        storedBook.setAuthor(book.getAuthor());
        storedBook.setIsbn(book.getIsbn());
        storedBook.setPublisher(book.getPublisher());
        return bookRepository.save(storedBook);
    }

    @Override
    public void deleteBookById(long id) {
        bookRepository.deleteById(id);
    }
}
