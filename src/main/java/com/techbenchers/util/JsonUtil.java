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
			String json = objectMapper.writeValueAsString(principal);
			return json;
		} catch (Exception e) {
			System.out.println("Exception in objectToJsonString: " + e.toString());
			throw e;
		}
	}

	public static JsonNode jsonStringToJsonObject(@NotNull String json) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			JsonNode jsonNode = objectMapper.readValue(json, JsonNode.class);
			return jsonNode;
		} catch (IOException e) {
			System.out.println("Exception in jsonStringToJsonObject: " + e.toString());
			throw e;
		}
	}

	public static String getFieldValue(@NotNull JsonNode jsonNode, @NotNull String field) throws Exception {
		try {
			String fieldValue = jsonNode.get(Constants.AUTHORITIES_KEY).get(0).get(Constants.ATTRIBUTES_KEY).get(field).asText();
			return fieldValue;
		} catch (Exception ex) {
			throw new Exception("Error in getFieldValue in extracting field: " + field + " " + ex.toString());
		}

	}

}