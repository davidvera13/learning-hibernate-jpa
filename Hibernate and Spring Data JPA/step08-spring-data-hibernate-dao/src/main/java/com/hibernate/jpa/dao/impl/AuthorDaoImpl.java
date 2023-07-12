package com.hibernate.jpa.dao.impl;

import com.hibernate.jpa.dao.AuthorDao;
import com.hibernate.jpa.domain.Author;
import com.hibernate.jpa.domain.Book;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorDaoImpl implements AuthorDao {
    private final EntityManagerFactory emf;

    @Autowired
    public AuthorDaoImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public Author getAuthorById(Long id) {
        return getEntityManager().find(Author.class, id);
    }


    @Override
    public Author getAuthorByName(String firstName, String lastName) {
        TypedQuery<Author> query = getEntityManager()
                .createQuery(
                        "SELECT a FROM Author a " +
                           "WHERE " +
                                "a.firstName = :first_name " +
                                "and " +
                                "a.lastName = :last_name",
                        Author.class);
        query.setParameter("first_name", firstName);
        query.setParameter("last_name", lastName);

        return query.getSingleResult();
    }

    @Override
    public Author saveAuthor(String firstName, String lastName) {
        Author author = new Author( firstName, lastName);
        EntityManager em = getEntityManager();
        // creating transaction
        // em.joinTransaction();
        // response is returned before persistence
        // em.persist(author);
        // forcing persistence: we need a transaction
        // jakarta.persistence.TransactionRequiredException: no transaction is in progress
        // em.flush();

        em.getTransaction().begin();
        em.persist(author);
        em.flush();
        em.getTransaction().commit();
        return author;
    }

    @Override
    public Author updateAuthor(Author author) {
        EntityManager em = getEntityManager();
        em.joinTransaction();
        em.merge(author);
        em.flush();
        em.clear();
        return em.find(Author.class, author.getId());
    }

    @Override
    public void deleteAuthorById(Long id) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        Author author = em.find(Author.class, id);
        em.remove(author);
        em.flush();
        em.getTransaction().commit();
    }


    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
