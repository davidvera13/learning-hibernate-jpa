package com.hibernate.jpa.beer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class JsonUtil {
    // Lay Initialization
    private static volatile ObjectMapper mapper = null;

    // Prevent multiple instances
    private JsonUtil() {}

    /**
     * Double-checked locking
     * @return
     */
    public static synchronized ObjectMapper getMapper() {
        if (mapper == null) {
            synchronized (JsonUtil.class) {
                if (mapper == null) {
                    mapper = new ObjectMapper()

                            .registerModule(new JavaTimeModule())
                            .registerModule(new Jdk8Module());
                }
            }
        }
        return mapper;
    }
}
