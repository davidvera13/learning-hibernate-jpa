package com.hibernate.jpa.dao;

import com.hibernate.jpa.domain.Author;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AuthorDao {
    List<Author> getAuthorsByLastNameLike(String lastName);
    Author getAuthorById(long id);
    Author getAuthorByName(String firstName, String lastName);
    Author saveAuthor(String firstName, String lastName);

    Author updateAuthor(Author author);

    void deleteAuthorById(Long id);

    List<Author> findAllAuthors();

}
