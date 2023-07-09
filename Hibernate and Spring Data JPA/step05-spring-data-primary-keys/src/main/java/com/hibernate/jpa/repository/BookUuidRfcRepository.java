package com.hibernate.jpa.repository;

import com.hibernate.jpa.domain.BookUuidRfc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BookUuidRfcRepository extends JpaRepository<BookUuidRfc, UUID> {
}
