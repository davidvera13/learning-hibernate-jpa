package com.hibernate.jpa.config;

import com.hibernate.jpa.listener.PostLoadListener;
import com.hibernate.jpa.listener.PreInsertListener;
import com.hibernate.jpa.listener.PreUpdateListener;
import org.hibernate.event.service.spi.EventListenerRegistry;
import org.hibernate.event.spi.EventType;
import org.hibernate.internal.SessionFactoryImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;

@Configuration
public class ListenerRegistration implements BeanPostProcessor {
    private final PostLoadListener postLoadListener;
    private final PreUpdateListener preUpdateListener;
    private final PreInsertListener preInsertListener;

    @Autowired
    public ListenerRegistration(
            PostLoadListener postLoadListener,
            PreUpdateListener preUpdateListener,
            PreInsertListener preInsertListener) {
        this.postLoadListener = postLoadListener;
        this.preUpdateListener = preUpdateListener;
        this.preInsertListener = preInsertListener;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return BeanPostProcessor.super.postProcessBeforeInitialization(bean, beanName);
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        if (bean instanceof LocalContainerEntityManagerFactoryBean){
            LocalContainerEntityManagerFactoryBean lemf = (LocalContainerEntityManagerFactoryBean) bean;
            SessionFactoryImpl sessionFactory = (SessionFactoryImpl) lemf.getNativeEntityManagerFactory();
            EventListenerRegistry registry = sessionFactory.getServiceRegistry()
                    .getService(EventListenerRegistry.class);

            registry.appendListeners(EventType.POST_LOAD, postLoadListener);
            registry.appendListeners(EventType.PRE_INSERT, preInsertListener);
            registry.appendListeners(EventType.PRE_UPDATE, preUpdateListener);
        }

        return bean;
    }
}
