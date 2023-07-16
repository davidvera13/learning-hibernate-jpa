package com.hibernate.jpa.dao.impl;

import com.hibernate.jpa.dao.AuthorDao;
import com.hibernate.jpa.domain.Author;
import com.hibernate.jpa.mappers.AuthorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorDaoImpl implements AuthorDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AuthorDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Author getAuthorById(Long id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM author WHERE id = ?",
                getRowMapper(),
                id);
    }

    @Override
    public Author getAuthorByName(String firstName, String lastName) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM author WHERE first_name = ? and last_name = ?",
                getRowMapper(),
                firstName, lastName);
    }

    @Override
    public Author saveAuthor(String firstName, String lastName) {
        jdbcTemplate.update(
                "INSERT INTO author(first_name, last_name) VALUES (?, ?)",
                firstName, lastName);
        Long createdId = jdbcTemplate.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
        return this.getAuthorById(createdId);
    }

    @Override
    public Author updateAuthor(Author author) {
        jdbcTemplate.update(
                "UPDATE author SET first_name = ?, last_name = ? WHERE id = ?",
                author.getFirstName(), author.getLastName(), author.getId());
        return this.getAuthorById(author.getId());
    }

    @Override
    public void deleteAuthorById(Long id) {
        jdbcTemplate.update("DELETE FROM author WHERE id = ?", id);
    }

    @Override
    public List<Author> findAuthorsByLastName(Pageable pageable, String lastName) {
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT * FROM author WHERE last_name = ? ");
        if(pageable.getSort().getOrderFor("firstname") != null) {
            sb
                    .append( "ORDER BY first_name ")
                    .append(pageable.getSort().getOrderFor("title").getDirection().name());
        }
        sb.append("  limit ? offset ?");
        System.out.println(sb);


        return jdbcTemplate.query(sb.toString(),
                getRowMapper(),
                lastName, pageable.getPageSize(), pageable.getOffset());
    }

    private RowMapper<Author> getRowMapper() {
        return new AuthorMapper();
    }
}
