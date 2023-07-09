package com.hibernate.jpa.repository;

import com.hibernate.jpa.domain.Author;
import com.hibernate.jpa.domain.AuthorUuid;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AuthorUuidRepository extends JpaRepository<AuthorUuid, UUID> {
}
