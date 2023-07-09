package com.hibernate.jpa.bootstrap;

import com.hibernate.jpa.domain.Book;
import com.hibernate.jpa.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final BookRepository bookRepository;

    @Autowired
    public DataInitializer(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void run(String... args) {
        /// save
        Book bookDDD = new Book("Domain Driven Design", "123", "RandomHouse");
        System.out.println("Domain Driven Design > Id: " + bookDDD.getId() );
        Book savedDDD = bookRepository.save(bookDDD);
        System.out.println("Domain Driven Design > Id: " + savedDDD.getId() );

        Book bookSIA = new Book("Spring In Action", "234234", "Oriely");
        System.out.println("Spring In Action > Id: " + bookSIA.getId() );
        Book savedSIA = bookRepository.save(bookSIA);
        System.out.println("Spring In Action > Id: " + savedSIA.getId() );

        // find all
        bookRepository.findAll().forEach(book -> {
            System.out.println("Book Id: " + book.getId());
            System.out.println("Book Title: " + book.getTitle());
        });

    }
}