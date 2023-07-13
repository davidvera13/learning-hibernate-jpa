package com.hibernate.jpa.repository;

import com.hibernate.jpa.domain.Author;
import com.hibernate.jpa.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
    Author findById(long id);
    List<Author> findAllByLastNameLike(String lastName);
    Optional<Author> findByFirstNameAndLastName(String firstName, String lastName);
}
