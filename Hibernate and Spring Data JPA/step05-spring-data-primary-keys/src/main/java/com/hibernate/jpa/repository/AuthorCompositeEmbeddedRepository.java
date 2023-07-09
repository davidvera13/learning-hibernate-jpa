package com.hibernate.jpa.repository;

import com.hibernate.jpa.domain.AuthorComposite;
import com.hibernate.jpa.domain.AuthorCompositeEmbedded;
import com.hibernate.jpa.domain.composite.AuthorCompositeEmbeddedPk;
import com.hibernate.jpa.domain.composite.AuthorCompositePk;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorCompositeEmbeddedRepository extends JpaRepository<AuthorCompositeEmbedded, AuthorCompositeEmbeddedPk> {
}
