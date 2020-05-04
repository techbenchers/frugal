package com.techbenchers.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.techbenchers.type.Constants;

import javax.validation.constraints.NotNull;

import java.io.IOException;
import java.security.Principal;


public class JsonUtil {

	public static String objectToJsonString(@NotNull Principal principal) throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsString(principal);
		} catch (Exception e) {
			System.out.println("Exception in objectToJsonString: " + e.toString());
			throw e;
		}
	}

	public static JsonNode jsonStringToJsonObject(@NotNull String json) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(json, JsonNode.class);
		} catch (IOException e) {
			System.out.println("Exception in jsonStringToJsonObject: " + e.toString());
			throw e;
		}
	}

	public static String getFieldValue(@NotNull JsonNode jsonNode, @NotNull String field) throws Exception {
		try {
			return jsonNode.get(Constants.AUTHORITIES_KEY).get(0).get(Constants.ATTRIBUTES_KEY).get(field).asText();
		} catch (Exception ex) {
			throw new Exception("Error in getFieldValue in extracting field: " + field + " " + ex.toString());
		}
	}

	public static boolean hasField(@NotNull JsonNode jsonNode, @NotNull String field) throws Exception {
		try {
			return jsonNode.get(Constants.AUTHORITIES_KEY).get(0).get(Constants.ATTRIBUTES_KEY).has(field);
		} catch (Exception ex) {
			throw new Exception("Error in hasField in checking field: " + field + " " + ex.toString());
		}
	}

}