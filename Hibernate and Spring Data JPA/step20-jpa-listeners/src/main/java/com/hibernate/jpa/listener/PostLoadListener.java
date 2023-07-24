package com.hibernate.jpa.listener;

import com.hibernate.jpa.service.EncryptionService;
import org.hibernate.event.spi.PostLoadEvent;
import org.hibernate.event.spi.PostLoadEventListener;
import org.springframework.stereotype.Component;


@Component
public class PostLoadListener
        extends AbstractEncryptionListener
        implements PostLoadEventListener {

    public PostLoadListener(EncryptionService encryptionService) {
        super(encryptionService);
    }

    @Override
    public void onPostLoad(PostLoadEvent event) {
        this.decrypt(event.getEntity());

        System.out.println(">> onPostLoad() called");
    }
}
