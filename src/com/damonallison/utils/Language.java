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
	 * This function can be called with an array or a sequence of strings.
	 * Within the method body, the argument is treated as an array.
	 * 
	 * This declaration is identical to
	 * 
	 * {@code public static String[] echoVarArgs(String[] s)}
	 * 
	 * @param s
	 *            An array of strings to print.
	 */
	public static String[] echoVarArgs(String... s) {
		Preconditions.checkNotNull(s);
		Preconditions.checkArgument(s.getClass().isArray()); // s is an array -

		String[] ret = new String[s.length];
		// Typically we'd use copy, but we are showing "s" being
		// used as an array.
		for (int i = 0; i < s.length; i++) {
			ret[i] = s[i];
		}
		return ret;
	}
}
