package com.hibernate.jpa.repositories.creditcard;

import com.hibernate.jpa.domain.creditcard.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CreditCardRepository extends JpaRepository<CreditCard, Long> {
}
