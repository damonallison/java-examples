package com.damonallison.utils;

import com.google.common.base.Preconditions;

/**
 * A set of functions to show java language features.
 * 
 * @author Damon Allison
 */
public final class Language {

	private Language() {
	}

	/**
	 * Java 1.7 allows you to switch on {@code String} values.
	 */
	public static String switchExample(String s) {

		// You need to always, manually check for null prior to using
		// a string in the switch statement to prevent a NullPointerException.
		Preconditions.checkNotNull(s);

		switch (s.toLowerCase()) {
		case "damon":
			return "i know you, damon";
		case "other":
			return "who?";
		default:
			return "who are you?";
		}
	}

	/**
	 * This function can be called with an array or a sequence of strings.
	 * Within the method body, the argument is treated as an array.
	 * 
	 * @param s
	 *            An array of strings to print.
	 */
	public static String[] varArgsExample(String... s) {
		Preconditions.checkNotNull(s);

		String[] ret = new String[s.length];

		// Typically we'd use copy, but we are showing "s" being
		// used as an array.
		for (int i = 0; i < s.length; i++) {
			ret[i] = s[i];
		}
		return ret;
	}
}
