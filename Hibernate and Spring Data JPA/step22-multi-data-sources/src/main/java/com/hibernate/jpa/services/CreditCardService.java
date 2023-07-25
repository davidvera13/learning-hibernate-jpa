package com.hibernate.jpa.services;

import com.hibernate.jpa.domain.creditcard.CreditCard;


public interface CreditCardService {

    CreditCard getCreditCardById(Long id);

    CreditCard saveCreditCard(CreditCard cc);
}
