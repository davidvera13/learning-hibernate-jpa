package com.hibernate.jpa.interceptor;

import com.hibernate.jpa.service.EncryptionService;
import org.hibernate.CallbackException;
import org.hibernate.Interceptor;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class EncryptionInterceptor implements Interceptor {
    private final EncryptionService encryptionService;

    @Autowired
    public EncryptionInterceptor(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }


    @Override
    public boolean onLoad(
            Object entity,
            Object id,
            Object[] state,
            String[] propertyNames,
            Type[] types) throws CallbackException {
        System.out.println(">> onLoad() called");
        Object[] newState = processFields(entity, state, propertyNames, "onLoad");
        return Interceptor.super.onLoad(entity, id, state, propertyNames, types);
    }

    @Override
    public boolean onFlushDirty(
            Object entity,
            Object id,
            Object[] currentState,
            Object[] previousState,
            String[] propertyNames,
            Type[] types) throws CallbackException {
        System.out.println(">> onFlushDirty() called");
        Object[] newState = processFields(entity, currentState, propertyNames, "onFlushDirty");
        return Interceptor.super.onFlushDirty(entity, id, currentState, previousState, propertyNames, types);
    }

    @Override
    public boolean onSave(
            Object entity,
            Object id,
            Object[] state,
            String[] propertyNames,
            Type[] types) throws CallbackException {
        System.out.println(">> onSave() called");
        Object[] newState = processFields(entity, state, propertyNames, "onSave");
        return Interceptor.super.onSave(entity, id, state, propertyNames, types);
    }


    private Object[] processFields(
            Object entity,
            Object[] state,
            String[] propertyNames,
            String type) {
        // we retrieve all fields and check their annotations
        List<String> annotationFields = getAnnotationFields(entity);

        for (String field : annotationFields) {
            for (int i = 0; i < propertyNames.length; i++) {
                if (propertyNames[i].equals(field)) {
                    if (StringUtils.hasText(state[i].toString())) {
                        if ("onSave".equals(type) || "onFlushDirty".equals(type)) {
                            state[i] = encryptionService.encrypt(state[i].toString());
                        } else if ("onLoad".equals(type)) {
                            state[i] = encryptionService.decrypt(state[i].toString());
                        }
                    }
                }
            }
        }

        return state;
    }

    private List<String> getAnnotationFields(Object entity) {

        List<String> annotatedFields = new ArrayList<>();
        // we parse the properties of a class using introspection
        for (Field field : entity.getClass().getDeclaredFields()) {
            if (!Objects.isNull(field.getAnnotation(EncryptedString.class))) {
                annotatedFields.add(field.getName());
            }
        }

        return annotatedFields;
    }
}
