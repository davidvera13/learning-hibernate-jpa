package com.hibernate.jpa.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;


@NamedQueries({
        @NamedQuery(
                name = "Book.jpaNamedQuery",
                query= "FROM Book b WHERE b.title = :title"),
        @NamedQuery(
                name="book_find_all",
                query = "FROM Book"),
        @NamedQuery(
                name="find_by_title",
                query = "FROM Book b WHERE b.title = :title")
})
@Entity
@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String isbn;
    private String publisher;
    // LEAKAGE OF RELATIONAL MAPPING > WE SHOULD USE AUTHOR OBJECT
    // property that is not tied to database
    @Transient
    private Author author;

    public Book(String title, String isbn, String publisher, Author author) {
        this.title = title;
        this.isbn = isbn;
        this.publisher = publisher;
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
