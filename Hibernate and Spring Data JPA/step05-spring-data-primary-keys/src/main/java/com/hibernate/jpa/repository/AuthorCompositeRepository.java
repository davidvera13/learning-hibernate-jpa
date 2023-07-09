package com.hibernate.jpa.repository;

import com.hibernate.jpa.domain.AuthorComposite;
import com.hibernate.jpa.domain.composite.AuthorCompositePk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorCompositeRepository extends JpaRepository<AuthorComposite, AuthorCompositePk> {
}
