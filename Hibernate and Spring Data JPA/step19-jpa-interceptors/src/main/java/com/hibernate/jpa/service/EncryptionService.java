package com.hibernate.jpa.service;

public interface EncryptionService {
    String encrypt(String str);
    String decrypt(String encodedStr);
}
