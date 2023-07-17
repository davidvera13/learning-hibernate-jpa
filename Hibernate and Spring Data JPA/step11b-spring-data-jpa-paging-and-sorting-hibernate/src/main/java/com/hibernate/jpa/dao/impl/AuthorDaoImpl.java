package com.hibernate.jpa.dao.impl;

import com.hibernate.jpa.dao.AuthorDao;
import com.hibernate.jpa.domain.Author;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthorDaoImpl implements AuthorDao {
    private final EntityManagerFactory emf;

    @Autowired
    public AuthorDaoImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    // using query
    @Override
    public List<Author> getAuthorsByLastNameLikeQuery(String lastName) {
        try (EntityManager em = getEntityManager()) {
            Query query = em.createQuery("SELECT a from Author  a WHERE a.lastName like :lastName");
            query.setParameter("lastName", lastName + "%");
            return query.getResultList();
        }
    }

    // using typed queries
    @Override
    public List<Author> getAuthorsByLastNameLike(String lastName) {
        try (EntityManager em = getEntityManager()) {
            // Query query = em.createQuery("SELECT a from Author  a WHERE a.lastName like :lastName");
            TypedQuery<Author> query = em.createQuery("SELECT a from Author  a WHERE a.lastName like :lastName", Author.class);
            query.setParameter("lastName", lastName + "%");
            return query.getResultList();
        }
    }

    // named query
    @Override
    public List<Author> findAllAuthors() {
        try (EntityManager em = getEntityManager()) {
          TypedQuery<Author> typedQuery =   em.createNamedQuery("author_find_all", Author.class);
          List<Author> authors = typedQuery.getResultList();
          return authors;
        }
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

    // named query with params
    @Override
    public Author getAuthorByNameNamedQuery(String firstName, String lastName) {
        TypedQuery<Author> query = getEntityManager()
                .createNamedQuery("find_by_name", Author.class);
        query.setParameter("firstName", firstName);
        query.setParameter("lastName", lastName);

        return query.getSingleResult();
    }

    @Override
    public Author getAuthorByNameCriteria(String firstName, String lastName) {
        try(EntityManager em = getEntityManager()) {
            CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
            CriteriaQuery<Author> criteriaQuery = criteriaBuilder.createQuery(Author.class);
            Root<Author> root = criteriaQuery.from(Author.class);
            ParameterExpression<String> firstNameParam = criteriaBuilder.parameter(String.class);
            ParameterExpression<String> lastNameParam = criteriaBuilder.parameter(String.class);

            Predicate firstNamePred = criteriaBuilder.equal(root.get("firstName"), firstNameParam);
            Predicate lastNamePred = criteriaBuilder.equal(root.get("lastName"), lastNameParam);

            criteriaQuery.select(root).where(criteriaBuilder.and(firstNamePred, lastNamePred));
            TypedQuery<Author>typedQuery = em.createQuery(criteriaQuery);
            typedQuery.setParameter(firstNameParam, firstName);
            typedQuery.setParameter(lastNameParam, lastName);

            return typedQuery.getSingleResult();
        }
    }

    @Override
    public Author getAuthorByNameNative(String firstName, String lastName) {
        try(EntityManager em = getEntityManager()) {
            Query query = em.createNativeQuery("SELECT * FROM author a WHERE a.first_name = ? AND a.last_name = ?", Author.class);
            query.setParameter(1, firstName);
            query.setParameter(2, lastName);
            return (Author) query.getSingleResult();
        }
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

    @Override
    public List<Author> findAuthorsByLastName(Pageable pageable, String lastName) {
        EntityManager em = getEntityManager();
        try {
            // hql : replace * with a (alias)
            String sql = "SELECT a FROM Author a WHERE a.lastName = :lastName ";
            if(pageable.getSort().getOrderFor("firstName") != null) {
                sql += "ORDER BY a.firstName " +
                        pageable.getSort().getOrderFor("firstName").getDirection().name();
            }
            TypedQuery<Author> query = em.createQuery(sql, Author.class);
            query.setParameter("lastName", lastName);
            query.setFirstResult(Math.toIntExact(pageable.getOffset()));
            query.setMaxResults(pageable.getPageSize());
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }
}
