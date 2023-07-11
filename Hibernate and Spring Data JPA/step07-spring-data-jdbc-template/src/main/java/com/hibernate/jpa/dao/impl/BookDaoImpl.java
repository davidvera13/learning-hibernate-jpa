package com.hibernate.jpa.dao.impl;

import com.hibernate.jpa.dao.AuthorDao;
import com.hibernate.jpa.dao.BookDao;
import com.hibernate.jpa.domain.Book;
import com.hibernate.jpa.mappers.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class BookDaoImpl implements BookDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public BookDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Book getById(Long id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM book where id = ?",
                getBookMapper(), id);

    }

    @Override
    public Book findBookByTitle(String title) {
        return jdbcTemplate.queryForObject("SELECT * FROM book where title = ?",
                getBookMapper(),
                title);
    }

    @Override
    public Book saveNewBook(Book book) {
        if(book.getAuthor() != null)
            jdbcTemplate.update("INSERT INTO book (isbn, publisher, title, author_id) VALUES (?, ?, ?, ?)",
                    book.getIsbn(), book.getPublisher(), book.getTitle(), book.getAuthor().getId());
        else
            jdbcTemplate.update("INSERT INTO book (isbn, publisher, title) VALUES (?, ?, ?)",
                    book.getIsbn(), book.getPublisher(), book.getTitle());

        Long createdId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);

        return this.getById(createdId);

    }

    @Override
    public Book updateBook(Book book) {
        if(book.getAuthor() != null)
            jdbcTemplate.update(
                    "UPDATE book set isbn = ?, publisher = ?, title = ?, author_id = ? where id = ?",
                    book.getIsbn(), book.getPublisher(), book.getTitle(), book.getAuthor().getId(), book.getId());
        else
            jdbcTemplate.update(
                    "UPDATE book set isbn = ?, publisher = ?, title = ? where id = ?",
                    book.getIsbn(), book.getPublisher(), book.getTitle(), book.getId());

        return this.getById(book.getId());

    }

    @Override
    public void deleteBookById(Long id) {
        jdbcTemplate.update("DELETE from book where id = ?", id);
    }

    private BookMapper getBookMapper(){
        return new BookMapper();
    }
}
