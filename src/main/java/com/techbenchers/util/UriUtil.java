package com.techbenchers.util;

public class UriUtil {
	public static String uriGenerator(String... args) {
		try {
			StringBuilder uri = new StringBuilder();
			for (String s : args) {
				String temp = s.trim().replaceAll("\\s+", "-");
				uri.append(temp);
				uri.append("-");
			}
			return uri.substring(0, uri.length() - 2);
		} catch (Exception e) {
			System.out.println("Exceptiton in  uriGenerator " + e.toString());
			throw e;
		}
	}
}
