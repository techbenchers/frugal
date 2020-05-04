package com.techbenchers.util;

public class ValidationUtil {
	public static boolean Null(String... args) throws Exception {
		try {
			for (String s : args) {
				if (s == null || s.isEmpty()) return true;
			}
			return false;
		} catch (Exception e) {
			System.out.println("Exception in ValidationUtil.Null " + e.toString());
			throw e;
		}
	}

	public static boolean Null(Integer... args) throws Exception {
		try {
			for (Integer s : args) {
				if (s == null) return true;
			}
			return false;
		} catch (Exception e) {
			System.out.println("Exception in ValidationUtil.Null " + e.toString());
			throw e;
		}
	}
}
