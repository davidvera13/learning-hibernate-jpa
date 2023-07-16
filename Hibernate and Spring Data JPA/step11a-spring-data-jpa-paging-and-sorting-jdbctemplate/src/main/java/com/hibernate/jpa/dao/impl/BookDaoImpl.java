package com.hibernate.jpa.dao.impl;

import com.hibernate.jpa.dao.AuthorDao;
import com.hibernate.jpa.dao.BookDao;
import com.hibernate.jpa.domain.Book;
import com.hibernate.jpa.mappers.BookMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;

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

    @Override
    public List<Book> findAllBooks() {
        return jdbcTemplate.query("SELECT * FROM book", getBookMapper());
    }

    @Override
    public List<Book> findAllBooks(int pageSize, int offset) {
        return jdbcTemplate.query(
                "SELECT * FROM book limit ? offset ?",
                getBookMapper(),
                pageSize, offset);
    }

    @Override
    public List<Book> findAllBooksPageable(Pageable pageable) {
        return jdbcTemplate.query(
                "SELECT * FROM book limit ? offset ? ",
                getBookMapper(),
                pageable.getPageSize(), pageable.getOffset());
    }

    @Override
    public List<Book> findAllBooksSortingByTitle(Pageable pageable) {
        String sql = "SELECT * FROM book ORDER BY title " +
                pageable.getSort().getOrderFor("title").getDirection().name() +
                "  limit ? offset ?";
        System.out.println(sql);
        return jdbcTemplate.query(sql,
                getBookMapper(),
                pageable.getPageSize(), pageable.getOffset());
    }

    private BookMapper getBookMapper(){
        return new BookMapper();
    }
}
