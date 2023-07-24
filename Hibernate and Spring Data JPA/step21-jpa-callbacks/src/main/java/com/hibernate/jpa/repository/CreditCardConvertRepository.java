package com.hibernate.jpa.repository;

import com.hibernate.jpa.domain.CreditCard;
import com.hibernate.jpa.domain.CreditCardConvert;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreditCardConvertRepository extends JpaRepository<CreditCardConvert, Long> {
}
