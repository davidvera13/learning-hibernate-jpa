package com.hibernate.jpa.dao.impl;

import com.hibernate.jpa.dao.BookDao;
import com.hibernate.jpa.domain.Author;
import com.hibernate.jpa.domain.Book;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import org.aspectj.weaver.ast.Literal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookDaoImpl implements BookDao {
    private final EntityManagerFactory emf;

    @Autowired
    public BookDaoImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    // typed query
    @Override
    public Book findByIsbn(String isbn) {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b WHERE b.isbn = :isbn", Book.class);
            // Query query = em.createQuery("SELECT b FROM Book b WHERE b.isbn = :isbn");
            query.setParameter("isbn", isbn);
            // Book book = (Book) query.getSingleResult();
            return query.getSingleResult();
        } finally {
            em.close();
        }

    }

    @Override
    public Book getById(Long id) {
        EntityManager em = getEntityManager();
        Book book = getEntityManager().find(Book.class, id);
        em.close();
        return book;
    }



    @Override
    public Book findBookByTitle(String title) {
        EntityManager em = getEntityManager();
        TypedQuery<Book> query = em
                .createQuery("SELECT b FROM Book b where b.title = :title", Book.class);
        query.setParameter("title", title);
        Book book = query.getSingleResult();
        em.close();
        return book;
    }

    // named query
    @Override
    public Book findBookByTitleNamedQuery(String title) {
        EntityManager em = getEntityManager();
        TypedQuery<Book> query = em
                .createNamedQuery("find_by_title", Book.class);
        query.setParameter("title", title);
        Book book = query.getSingleResult();
        em.close();
        return book;
    }

    public Book findBookByTitleNative(String title) {
        try(EntityManager em = getEntityManager()) {
            Query query = em.createNativeQuery("SELECT * FROM book b WHERE b.title = ? ", Book.class);
            query.setParameter(1, title);
            return (Book) query.getSingleResult();
        }
    }

    // named query
    @Override
    public List<Book> findAll() {
        EntityManager em = getEntityManager();
        TypedQuery<Book> query = em
                .createNamedQuery("book_find_all", Book.class);
        List<Book> books = query.getResultList();
        em.close();
        return books;
    }

    @Override
    public Book saveNewBook(Book book) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.persist(book);
        em.flush();
        em.getTransaction().commit();
        em.close();
        return book;
    }

    @Override
    public Book updateBook(Book book) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        em.merge(book);
        em.flush();
        em.clear();
        Book savedBook = em.find(Book.class, book.getId());
        em.getTransaction().commit();
        em.close();
        return savedBook;

    }

    @Override
    public void deleteBookById(Long id) {
        EntityManager em = getEntityManager();
        em.getTransaction().begin();
        Book book = em.find(Book.class, id);
        em.remove(book);
        em.getTransaction().commit();
        em.close();
    }

    private EntityManager getEntityManager(){
        return emf.createEntityManager();
    }
}
