package com.hibernate.jpa;

import com.hibernate.jpa.domain.*;
import com.hibernate.jpa.domain.composite.AuthorCompositeEmbeddedPk;
import com.hibernate.jpa.domain.composite.AuthorCompositePk;
import com.hibernate.jpa.repository.*;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("dev")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DataJpaTest
@ComponentScan(basePackages = { "com.hibernate.jpa.bootstrap" })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PkTest {
    @Autowired
    BookUuidRfcRepository bookUuidRfcRepository;

    @Autowired
    AuthorUuidRepository authorUuidRepository;
    @Autowired
    BookNaturalRepository bookNaturalRepository;
    @Autowired
    AuthorCompositeRepository authorCompositeRepository;
    @Autowired
    AuthorCompositeEmbeddedRepository authorCompositeEmbeddedRepository;

    @Test
    void createAndCheckAuthor(){
        AuthorUuid shakespeare = new AuthorUuid("William", "Shakespeare");
        AuthorUuid storedAuthorUuid;
        storedAuthorUuid = authorUuidRepository.save(shakespeare);
        assertNotNull(storedAuthorUuid);
        assertNotNull(storedAuthorUuid.getId());

        System.out.println("shakespeare > id : " + storedAuthorUuid.getId());

        assertEquals(storedAuthorUuid, authorUuidRepository.findById(storedAuthorUuid.getId()).orElseThrow());
    }

    @Test
    void createAndCheckBook(){
        BookUuidRfc lordOfTheRings = new BookUuidRfc("Lord of the rings", "123456", "Publisher ltd", null);
        BookUuidRfc storedBook;
        storedBook = bookUuidRfcRepository.save(lordOfTheRings);
        assertNotNull(storedBook);
        assertNotNull(storedBook.getId());

        System.out.println("Lord of the rings > id : " + storedBook.getId());

        assertEquals(storedBook, bookUuidRfcRepository.findById(storedBook.getId()).orElseThrow());
    }

    @Test
    void createAndCheckBookNatural(){
        BookNatural lordOfTheRings = new BookNatural("Lord of the rings", "123456", "Publisher ltd", null);
        BookNatural storedBook;
        storedBook = bookNaturalRepository.save(lordOfTheRings);
        assertNotNull(storedBook);
        assertNotNull(storedBook.getTitle());

        System.out.println("Lord of the rings > id : " + storedBook.getTitle());

        assertEquals(storedBook, bookNaturalRepository.findById(storedBook.getTitle()).orElseThrow());
    }

    @Test
    void createAuthorComposite() {
        AuthorCompositePk key = new AuthorCompositePk("John", "Doe");
        AuthorComposite authorComposite = new AuthorComposite(key.getFirstName(), key.getLastName(), "Sweden");
        AuthorComposite storedAuthor = authorCompositeRepository.save(authorComposite);

        assertNotNull(storedAuthor);
        assertNotNull(storedAuthor.getFirstName());
        assertNotNull(storedAuthor.getLastName());

        assertEquals(storedAuthor, authorCompositeRepository.getReferenceById(key));

    }

    @Test
    void createAuthorCompositeEmbedded() {
        AuthorCompositeEmbeddedPk key = new AuthorCompositeEmbeddedPk("John", "Doe");
        AuthorCompositeEmbedded authorComposite = new AuthorCompositeEmbedded(key, "Sweden");
        AuthorCompositeEmbedded storedAuthor = authorCompositeEmbeddedRepository.save(authorComposite);

        assertNotNull(storedAuthor);
        assertNotNull(storedAuthor.getId().getFirstName());
        assertNotNull(storedAuthor.getId().getLastName());

        assertEquals(storedAuthor, authorCompositeEmbeddedRepository.getReferenceById(key));

    }
}
