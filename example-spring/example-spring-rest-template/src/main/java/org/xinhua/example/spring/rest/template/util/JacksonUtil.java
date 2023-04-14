package org.xinhua.example.spring.rest.template.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.IOException;

public final class JacksonUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static ObjectNode createObjectNode() {
        return objectMapper.createObjectNode();
    }

    public static JsonNode jsonToNode(byte[] json) {
        try {
            return objectMapper.readTree(json);
        } catch (IOException e) {
        }
        return null;
    }

    public static String getText(JsonNode jsonNode, String key) {
        JsonNode node = jsonNode.get(key);
        return node == null ? "" : node.asText();
    }

    public static int getInt(JsonNode jsonNode, String key) {
        JsonNode node = jsonNode.get(key);
        return node == null ? 0 : node.asInt();
    }

}
