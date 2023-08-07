package org.xinhua.example.spring.mybatisplus.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Slf4j
@Component
public final class JacksonUtil {

    private static ObjectMapper objectMapper;

    @Autowired
    public JacksonUtil(ObjectMapper objectMapper) {
        JacksonUtil.objectMapper = objectMapper;
    }

    /**
     * 对象转json
     */
    public static String objectToJson(Object obj) {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("对象转json错误||e={}", e);
        }

        return json;
    }

    /**
     * json转实体对象类
     */
    public static <T> T jsonToClass(String json, Class<T> beanType) {
        T t = null;

        if (null == json || json.equals("")) {
            return t;
        }

        try {
            t = objectMapper.readValue(json, beanType);
        } catch (Exception e) {
            log.error("json转对象错误||e={}", e);
        }

        return t;
    }

    public static <T> T jsonToClass(String json, Class<?> parametrized, Class<?>... parameterClasses) {
        T t = null;

        if (null == json || json.equals("")) {
            return t;
        }

        try {
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(parametrized, parameterClasses);
            t = objectMapper.readValue(json, javaType);
        } catch (Exception e) {
            log.error("json转对象错误||e={}", e);
        }

        return t;
    }

    /**
     * json转collection，泛型
     */
    public static <T> T jsonToCollection(String json, Class<? extends Collection> collectionClass, Class beanClass) {
        T t = null;

        if (null == json || json.equals("")) {
            return t;
        }

        try {
            JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(collectionClass, beanClass);
            t = objectMapper.readValue(json, javaType);
        } catch (Exception e) {
            log.error("json转集合错误||e={}", e);
        }

        return t;
    }

    /**
     * json转jsonnode对象
     */
    public static JsonNode jsonToNode(String json) {
        JsonNode jsonNode = null;

        try {
            jsonNode = objectMapper.readTree(json);
        } catch (IOException e) {
            log.error("json转node错误||e={}", e);
        }

        return jsonNode;
    }

    /**
     * 对象转为jsonnode
     */
    public static JsonNode objectToNode(Object obj) {
        JsonNode jsonNode = null;

        try {
            jsonNode = objectMapper.convertValue(obj, JsonNode.class);
        } catch (Exception e) {
            log.error("obj转node错误||e={}", e);
        }

        return jsonNode;
    }

    public static ObjectNode createObjectNode() {
        return objectMapper.createObjectNode();
    }

    public static ArrayNode createArrayNode() {
        return objectMapper.createArrayNode();
    }

    /**
     * 获取string类型的值，加null判断
     *
     * @param jsonNode
     * @param key
     * @return
     */
    public static String getText(JsonNode jsonNode, String key) {
        JsonNode node = jsonNode.get(key);
        return node == null ? "" : node.asText();
    }

    /**
     * 获取int类型的值，加null判断
     *
     * @param jsonNode
     * @param key
     * @return
     */
    public static int getInt(JsonNode jsonNode, String key) {
        JsonNode node = jsonNode.get(key);
        return node == null ? 0 : node.asInt();
    }

}
