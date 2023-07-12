package com.hibernate.jpa.dao;

import com.hibernate.jpa.domain.Author;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AuthorDao {
    List<Author> getAuthorsByLastNameLikeQuery(String lastName);

    // using query
    List<Author> getAuthorsByLastNameLike(String lastName);
    Author getAuthorById(Long id);
    Author getAuthorByName(String firstName, String lastName);
    Author saveAuthor(String firstName, String lastName);

    Author updateAuthor(Author author);

    void deleteAuthorById(Long id);

    List<Author> findAllAuthors();

    Author getAuthorByNameNamedQuery(String firstName, String lastName);

    Author getAuthorByNameCriteria(String firstName, String lastName);

    Author getAuthorByNameNative(String firstName, String lastName);
}
