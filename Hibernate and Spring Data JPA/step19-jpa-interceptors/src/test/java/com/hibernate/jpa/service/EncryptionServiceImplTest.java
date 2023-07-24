package com.hibernate.jpa.service;

import com.hibernate.jpa.repository.CreditCardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EncryptionServiceImplTest {
    final String CREDIT_CARD = "123467891114";
    @Autowired
    EncryptionService encryptionService;

    @Test
    void encrypt() {
        System.out.println(encryptionService.encrypt(CREDIT_CARD));
        assertEquals(encryptionService.encrypt(CREDIT_CARD), "MTIzNDY3ODkxMTE0");
    }

    @Test
    void decrypt() {
        System.out.println(encryptionService.decrypt("MTIzNDY3ODkxMTE0"));
        assertEquals(CREDIT_CARD, encryptionService.decrypt("MTIzNDY3ODkxMTE0"));
    }
}