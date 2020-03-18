package com.techbenchers.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.security.Principal;
import java.util.HashMap;

public class JsonUtil {
    public HashMap<String, String> parse(Principal principal) {
        String json = objectToJsonString(principal);
        JsonNode jsonNode = jsonStringToJsonObject(json);
        HashMap<String, String> fieldValueMap = getFieldValueMap(jsonNode);
        return fieldValueMap;
    }

    public String objectToJsonString(Principal principal) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String json = objectMapper.writeValueAsString(principal);
            return json;
        } catch (Exception e) {
            System.out.println("Exception in objectToJsonString: " + e.toString());
        }
        return null;
    }

    public JsonNode jsonStringToJsonObject(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readValue(json, JsonNode.class);
            return jsonNode;
        } catch (IOException e) {
            System.out.println("Exception in jsonStringToJsonObject: " + e.toString() + "\n");
            e.printStackTrace();
        }
        return null;
    }

    public String getFieldValue(JsonNode jsonNode, String field) {
        try {
            String fieldValue = jsonNode.get("authorities").get(0).get("attributes").get(field).asText();
            return fieldValue;
        } catch (Exception e) {
            System.out.println("Exception in getFieldValue: " + e.toString());
        }
        return null;
    }

    public HashMap<String, String> getFieldValueMap(JsonNode jsonNode) {
        HashMap<String, String> map = new HashMap<>();
        map.put("user_id", getFieldValue(jsonNode, "sub"));
        map.put("user_name", getFieldValue(jsonNode, "name"));
        map.put("email", getFieldValue(jsonNode, "email"));
        return map;
    }
}