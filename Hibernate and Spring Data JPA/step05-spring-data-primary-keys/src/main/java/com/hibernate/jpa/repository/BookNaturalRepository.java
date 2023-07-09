package com.hibernate.jpa.repository;

import com.hibernate.jpa.domain.Book;
import com.hibernate.jpa.domain.BookNatural;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookNaturalRepository extends JpaRepository<BookNatural, String> {
}
