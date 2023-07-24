package com.hibernate.jpa.service;


import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class EncryptionServiceImpl implements EncryptionService {
    @Override
    public String encrypt(String str) {
        return Base64.getEncoder().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String decrypt(String encodedStr) {
        return new String(Base64.getDecoder().decode(encodedStr));
    }
}
