package com.damonallison.utils;

import com.sun.corba.se.spi.orb.StringPair;

public class Language {
	
	public Language() {
		
	}
	
	/**
	 * String based switch requires Java 1.7 and above.
	 * @param s The string to switch on.
	 */
	public String switchExample(String s) {
		
		// You need to always, manually check for null prior to using |s|
		// in the switch statement to prevent a NullPointerException.
		
		if (s == null) {
			return null;
		}
		switch (s.toLowerCase()) {
		case "damon":
			return "i know you, damon";
		default:
			return "who are you?";
		}
	}
	
	/**
	 * This function can be called with an array or a sequence
	 * of strings. Within the method body, the argument is 
	 * treated as an array.
	 * @param s An array of strings to print.
	 */
	public String[] varArgsExample(String... s) {
		if (s == null) {
			return null;
		}
		
		String[] ret = new String[s.length];
		
		// Typically we'd use copy, but we are showing "s" being 
		// used as an array.
		for (int i = 0; i < s.length; i++) {
			ret[i] = s[i];
		}
		
		return ret;
	}
}
