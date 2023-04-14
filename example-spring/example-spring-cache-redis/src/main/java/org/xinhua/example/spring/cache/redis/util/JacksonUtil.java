package org.xinhua.example.spring.cache.redis.util;

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

    public static <T> T jsonToClass(String json, Class<T> beanType) {
        T t = null;

        if (null == json || json.equals("")) {
            return t;
        }

        try {
            t = objectMapper.readValue(json, beanType);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }

}
