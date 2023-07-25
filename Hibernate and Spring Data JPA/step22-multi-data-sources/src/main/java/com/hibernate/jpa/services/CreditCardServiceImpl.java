package com.hibernate.jpa.services;

import com.hibernate.jpa.domain.cardholder.CreditCardHolder;
import com.hibernate.jpa.domain.creditcard.CreditCard;
import com.hibernate.jpa.domain.pan.CreditCardPAN;
import com.hibernate.jpa.repositories.cardholder.CreditCardHolderRepository;
import com.hibernate.jpa.repositories.creditcard.CreditCardRepository;
import com.hibernate.jpa.repositories.pan.CreditCardPANRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreditCardServiceImpl implements CreditCardService {

    private final CreditCardHolderRepository creditCardHolderRepository;
    private final CreditCardRepository creditCardRepository;
    private final CreditCardPANRepository creditCardPANRepository;

    @Autowired
    public CreditCardServiceImpl(
            CreditCardHolderRepository creditCardHolderRepository,
            CreditCardRepository creditCardRepository,
            CreditCardPANRepository creditCardPANRepository) {
        this.creditCardHolderRepository = creditCardHolderRepository;
        this.creditCardRepository = creditCardRepository;
        this.creditCardPANRepository = creditCardPANRepository;
    }

    @Transactional
    @Override
    public CreditCard getCreditCardById(Long id) {
        CreditCard creditCard = creditCardRepository.findById(id).orElseThrow();
        CreditCardHolder creditCardHolder = creditCardHolderRepository.findByCreditCardId(id).orElseThrow();
        CreditCardPAN creditCardPAN = creditCardPANRepository.findByCreditCardId(id).orElseThrow();

        creditCard.setFirstName(creditCardHolder.getFirstName());
        creditCard.setLastName(creditCardHolder.getLastName());
        creditCard.setZipCode(creditCard.getZipCode());
        creditCard.setCreditCardNumber(creditCardPAN.getCreditCardNumber());

        return creditCard;
    }

    @Transactional
    @Override
    public CreditCard saveCreditCard(CreditCard cc) {
        CreditCard savedCC = creditCardRepository.save(cc);
        savedCC.setFirstName(cc.getFirstName());
        savedCC.setLastName(cc.getLastName());
        savedCC.setZipCode(cc.getCreditCardNumber());
        savedCC.setCreditCardNumber(cc.getCreditCardNumber());

        creditCardHolderRepository.save(CreditCardHolder.builder()
                        .creditCardId(savedCC.getId())
                        .firstName(savedCC.getFirstName())
                        .lastName(savedCC.getLastName())
                        .zipCode(savedCC.getZipCode())
                .build());

        creditCardPANRepository.save(CreditCardPAN.builder()
                        .creditCardNumber(savedCC.getCreditCardNumber())
                        .creditCardId(savedCC.getId())
                .build());

        return savedCC;
    }
}













