package com.hibernate.jpa.config;

import com.hibernate.jpa.config.SpringContextHelper;
import com.hibernate.jpa.domain.CreditCard;
import com.hibernate.jpa.service.EncryptionService;
import jakarta.persistence.*;

public class CreditCardJpaCallback {
    private EncryptionService encryptionService() {
        return SpringContextHelper.getApplicationContext().getBean(EncryptionService.class);
    }
    @PrePersist
    @PreUpdate
    public void beforeInsertOrUpdate(CreditCard creditCard) {
        System.out.println("beforeInsertOrUpdate() was called");
        System.out.println("**************************************************");
        System.out.println(creditCard.toString());

        creditCard.setCreditCardNumber(encryptionService().encrypt(creditCard.getCreditCardNumber()));
        System.out.println(creditCard);
        System.out.println("**************************************************");
    }

    @PostPersist
    @PostLoad
    @PostUpdate
    public void postLoad(CreditCard creditCard) {
        System.out.println("postLoad() was called");
        System.out.println("**************************************************");
        System.out.println(creditCard.toString());

        creditCard.setCreditCardNumber(encryptionService().decrypt(creditCard.getCreditCardNumber()));
        System.out.println(creditCard);
        System.out.println("**************************************************");
    }
}
