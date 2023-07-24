package com.hibernate.jpa.domain;

import com.hibernate.jpa.config.CreditCardJpaCallback;
import com.hibernate.jpa.interceptor.EncryptedString;
import jakarta.persistence.*;

@Entity
@EntityListeners(CreditCardJpaCallback.class)
public class CreditCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // we use annotation created
    @EncryptedString
    private String creditCardNumber;

    private String cvv;

    private String expirationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return "CreditCard{" +
                "id=" + id +
                ", creditCardNumber='" + creditCardNumber + '\'' +
                ", cvv='" + cvv + '\'' +
                ", expirationDate='" + expirationDate + '\'' +
                '}';
    }

    // solution 1: in entity class, we can implement callbacks
    @PrePersist
    public void prePersistCallback() {
        System.out.println("JPA Prepersist callback was called");
    }
}