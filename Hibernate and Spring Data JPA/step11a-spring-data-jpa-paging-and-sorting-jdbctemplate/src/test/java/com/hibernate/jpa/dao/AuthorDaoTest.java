package com.hibernate.jpa.dao;

import com.hibernate.jpa.dao.impl.AuthorDaoImpl;
import com.hibernate.jpa.domain.Author;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("dev")
@DataJpaTest
//@ComponentScan(basePackages = { "com.hibernate.jpa.dao" })
@Import(AuthorDaoImpl.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthorDaoTest {
    @Autowired
    AuthorDao authorDao;

    @Test
    void getAuthorById() {
        Author author = authorDao.getAuthorById(1L);
        assertThat(author).isNotNull();
        assertEquals("Craig", author.getFirstName());
        assertEquals("Walls", author.getLastName());
        System.out.println(author.getFirstName() + " " +  author.getLastName());
    }

    @Test
    void getAuthorByName() {
        Author author = authorDao.getAuthorByName("Craig", "Walls");
        assertThat(author).isNotNull();
        assertEquals("Craig", author.getFirstName());
        assertEquals("Walls", author.getLastName());
        System.out.println(author.getFirstName() + " " +  author.getLastName());
    }

    @Test
    void saveAuthor() {
        Author author = authorDao.saveAuthor("Joe", "Jackson");
        assertThat(author).isNotNull();
        assertEquals("Joe", author.getFirstName());
        assertEquals("Jackson", author.getLastName());
        System.out.println(author.getId() + " > " + author.getFirstName() + " " +  author.getLastName());
    }

    @Test
    void updateAuthor() {
        Author author = authorDao.saveAuthor("Walter", "Skinner");
        System.out.println(author.getId() + " > " + author.getFirstName() + " " +  author.getLastName());
        author.setLastName("Michael");
        author.setFirstName("Jackson");

        Author updated = authorDao.updateAuthor(author);
        System.out.println(updated.getId() + " > " + updated.getFirstName() + " " +  updated.getLastName());
    }


    @Test
    void deleteAuthor() {
        Author author = authorDao.saveAuthor("John", "Doe");
        System.out.println(author.getId() + " > " + author.getFirstName() + " " +  author.getLastName());


        authorDao.deleteAuthorById(author.getId());
        assertThrows(EmptyResultDataAccessException.class, () -> {
            authorDao.getAuthorById(author.getId());
        });
    }

    @Test
    void findAuthorsByLastName() {
        Pageable pageable;

        List<Author> authors;
        pageable = PageRequest.of(0, 10, Sort.by(Sort.Order.desc("firstName")));

        authors = authorDao.findAuthorsByLastName(pageable, "Smith");
        assertThat(authors).isNotNull();
        assertThat(authors.size()).isLessThanOrEqualTo(10);
        System.out.println(authors.size());

        authors = authorDao.findAuthorsByLastName(
                PageRequest.of(0, 10, Sort.by(Sort.Order.desc("firstName"))), "Smith");
        assertThat(authors).isNotNull();
        assertThat(authors.size()).isLessThanOrEqualTo(10);
        System.out.println(authors.size());




    }

}