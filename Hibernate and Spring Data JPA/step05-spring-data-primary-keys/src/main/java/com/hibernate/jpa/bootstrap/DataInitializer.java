package com.hibernate.jpa.bootstrap;

import com.hibernate.jpa.domain.*;
import com.hibernate.jpa.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile({ "dev", "default" })
@Component
public class DataInitializer implements CommandLineRunner {
    private final BookRepository bookRepository;
    private final AuthorUuidRepository authorUuidRepository;

    private final AuthorRepository authorRepository;
    private final BookUuidRfcRepository bookUuidRfcRepository;
    private final BookNaturalRepository bookNaturalRepository;

    @Autowired
    public DataInitializer(
            BookRepository bookRepository,
            AuthorUuidRepository authorUuidRepository,
            AuthorRepository authorRepository,
            BookUuidRfcRepository bookUuidRfcRepository,
            BookNaturalRepository bookNaturalRepository) {
        this.bookRepository = bookRepository;
        this.authorUuidRepository = authorUuidRepository;
        this.authorRepository = authorRepository;
        this.bookUuidRfcRepository = bookUuidRfcRepository;
        this.bookNaturalRepository = bookNaturalRepository;
    }

    @Override
    public void run(String... args) {
        // delete all
        bookRepository.deleteAll();
        authorRepository.deleteAll();
        authorUuidRepository.deleteAll();
        // Adding books with long
        Book bookDDD = new Book("Domain Driven Design", "123", "RandomHouse", null);
        System.out.println("Domain Driven Design > Id: " + bookDDD.getId() );
        Book savedDDD = bookRepository.save(bookDDD);
        System.out.println("Domain Driven Design > Id: " + savedDDD.getId() );

        Book bookSIA = new Book("Spring In Action", "234234", "Oriely", null);
        System.out.println("Spring In Action > Id: " + bookSIA.getId() );
        Book savedSIA = bookRepository.save(bookSIA);
        System.out.println("Spring In Action > Id: " + savedSIA.getId() );

        // adding books with uuid rfc
        BookUuidRfc dune = new BookUuidRfc("Dune", "123456", "Publisher ltd", null);
        BookUuidRfc storedBook;
        storedBook = bookUuidRfcRepository.save(dune);
        System.out.println("Dune > id : " + storedBook.getId());
        BookUuidRfc bladeRunner = new BookUuidRfc("Blade runner", "2049", "Publisher ltd", null);
        storedBook = bookUuidRfcRepository.save(bladeRunner);
        System.out.println("Dune > id : " + storedBook.getId());

        // Adding books with natural pk
        BookNatural bookNatural = new BookNatural("Gone with the Stream", "123456", "Ltd. estd.", null);
        BookNatural storedBookNatural = bookNaturalRepository.save(bookNatural);
        System.out.println("Gone with the stream > id Natural " + storedBookNatural.getTitle());

        // Adding author with long id
        Author tolkien = new Author("JRR", "Tolkien");
        Author dick = new Author("Philip K.", "Dick");
        Author storedAuthor;
        storedAuthor = authorRepository.save(tolkien);
        System.out.println("Tolkien > id : " + storedAuthor.getId());
        storedAuthor = authorRepository.save(dick);
        System.out.println("Dick > id : " + storedAuthor.getId());

        // adding author with UUID
        AuthorUuid herbert = new AuthorUuid("Franck", "Herbert");
        AuthorUuid farmer = new AuthorUuid("Philip Jose", "Farmer");

        AuthorUuid storedAuthorUuid;
        storedAuthorUuid = authorUuidRepository.save(herbert);
        System.out.println("Herbert > id : " + storedAuthorUuid.getId());
        storedAuthorUuid = authorUuidRepository.save(farmer);
        System.out.println("Farmer > id : " + storedAuthorUuid.getId());

        System.out.println("*************************************");

        // find all
        bookRepository.findAll().forEach(book -> {
            System.out.println("Book Id: " + book.getId());
            System.out.println("Book Title: " + book.getTitle());
        });

        authorUuidRepository.findAll().forEach(authorUuid ->
                System.out.println(authorUuid.getFirstName() + " " + authorUuid.getLastName()));

        authorRepository.findAll().forEach(author ->
                System.out.println(author.getFirstName() + " " + author.getLastName()));

        bookUuidRfcRepository.findAll().forEach(bookUuidRfc -> {
            System.out.println("Book Id: " + bookUuidRfc.getId());
            System.out.println("Book Title: " + bookUuidRfc.getTitle());
        });

        bookNaturalRepository.findAll().forEach(System.out::println);

    }
}