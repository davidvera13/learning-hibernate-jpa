package com.hibernate.jpa.dao.impl;

import com.hibernate.jpa.dao.AuthorDao;
import com.hibernate.jpa.domain.Author;
import com.hibernate.jpa.repository.AuthorRepository;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class AuthorDaoImpl implements AuthorDao {
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorDaoImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    // using typed queries
    @Override
    public List<Author> getAuthorsByLastNameLike(String lastName) {
        return authorRepository.findAllByLastNameLike(lastName);
    }

    // named query
    @Override
    public List<Author> findAllAuthors() {
        return authorRepository.findAll();
    }

    @Override
    public Author getAuthorById(long id) {
        return authorRepository.findById(id);
    }


    @Override
    public Author getAuthorByName(String firstName, String lastName) {
        return authorRepository
                .findByFirstNameAndLastName(firstName, lastName)
                .orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public Author saveAuthor(String firstName, String lastName) {
        return authorRepository.save(new Author(firstName, lastName));
    }

    // do all operation in a single transaction
    @Transactional
    @Override
    public Author updateAuthor(Author author) {
        Author storedAuthor = getAuthorById(author.getId());
        storedAuthor.setFirstName(author.getFirstName());
        storedAuthor.setFirstName(author.getFirstName());
        return authorRepository.save(storedAuthor);
    }

    @Override
    public void deleteAuthorById(Long id) {
        authorRepository.deleteById(id);
    }
}
