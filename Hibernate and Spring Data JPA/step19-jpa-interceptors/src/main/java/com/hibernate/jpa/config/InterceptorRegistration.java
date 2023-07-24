package com.hibernate.jpa.config;

import com.hibernate.jpa.interceptor.EncryptionInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class InterceptorRegistration implements HibernatePropertiesCustomizer {
    private final EncryptionInterceptor encryptionInterceptor;

    @Autowired
    public InterceptorRegistration(EncryptionInterceptor encryptionInterceptor) {
        this.encryptionInterceptor = encryptionInterceptor;
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put("hibernate.session_factory.interceptor", encryptionInterceptor);
    }
}
