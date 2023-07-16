package com.hibernate.jpa.dao;

import com.hibernate.jpa.domain.Author;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AuthorDao {
    Author getAuthorById(Long id);
    Author getAuthorByName(String firstName, String lastName);
    Author saveAuthor(String firstName, String lastName);

    Author updateAuthor(Author author);

    void deleteAuthorById(Long id);

    List<Author> findAuthorsByLastName(Pageable pageable, String lastName);
}
