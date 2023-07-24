package com.hibernate.jpa.config;

import com.hibernate.jpa.config.SpringContextHelper;
import com.hibernate.jpa.service.EncryptionService;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CreditCardConverter implements AttributeConverter<String, String> {
    private EncryptionService encryptionService() {
        return SpringContextHelper.getApplicationContext().getBean(EncryptionService.class);
    }

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return encryptionService().encrypt(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return encryptionService().decrypt(dbData);
    }
}
