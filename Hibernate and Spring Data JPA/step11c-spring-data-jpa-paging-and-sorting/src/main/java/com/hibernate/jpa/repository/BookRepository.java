package com.hibernate.jpa.repository;

import com.hibernate.jpa.domain.Book;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.stream.Stream;


@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    Book findByIsbn(String isbn);
    Book findById(long id);

    Optional<Book> findByTitle(String title);

    @Nullable
    Book getByTitle(@Nullable String title);

    Stream<Book> findAllByTitleNotNull();


    // async: Future, CompletableFuture, ListenableFuture
    @Async
    Future<Book> queryBookByTitle(String title);

    // Declaring Queries Using @Query
    @Query("SELECT b FROM Book b WHERE b.title = ?1")
    Book findBookByTitleWithQuery(String title);

    // named parameter query
    @Query("SELECT b FROM Book b WHERE b.title = :title")
    Book findBookByTitleWithNameParameterQuery(@Param("title") String title);

    // HQL : Native SQL Queries
    @Query(value = "SELECT * FROM book WHERE title = :title", nativeQuery = true)
    Book findBookByTitleWithHqlQuery(@Param("title") String title);

    // jpa named query
    Book jpaNamedQuery(@Param("title") String title);

    @Query(value = "SELECT * FROM book LIMIT :limit OFFSET :offset", nativeQuery = true)
    List<Book> findAllWithLimitAndOffset(@Param("limit") int limit, @Param("offset") int offset);
}
