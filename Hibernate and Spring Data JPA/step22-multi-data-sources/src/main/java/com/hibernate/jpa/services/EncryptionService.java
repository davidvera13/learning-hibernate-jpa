package com.hibernate.jpa.services;


public interface EncryptionService {

    String encrypt(String freeText);

    String decrypt(String encryptedText);
}
