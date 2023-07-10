package com.hibernate.jpa.dao;

import com.hibernate.jpa.domain.Author;

import java.sql.SQLException;
import java.util.Optional;

public interface AuthorDao {
    Author getAuthorById(Long id);
    Author getAuthorByName(String firstName, String lastName);
    Author saveAuthor(String firstName, String lastName);

    Author updateAuthor(Author author);

    void deleteAuthorById(Long id);
}
