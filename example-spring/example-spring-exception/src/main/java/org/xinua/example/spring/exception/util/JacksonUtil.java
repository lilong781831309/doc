package org.xinua.example.spring.exception.util;

import com.fasterxml.jackson.databind.ObjectMapper;


public final class JacksonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String objectToJson(Object obj) {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }

}
