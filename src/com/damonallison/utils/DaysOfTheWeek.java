package com.damonallison.utils;

/**
 * Java enums can have methods and constructors.
 * 
 * All java enums inherit from java.lang.Enum. An enum cannot extend anything
 * else.
 * 
 * @author Damon Allison
 *
 */
public enum DaysOfTheWeek {

	SUNDAY("lazy"), //
	MONDAY("uhhh"), //
	TUESDAY("uh"), //
	WEDNESDAY("hump"), //
	THURSDAY("better"), //
	FRIDAY("tgif"), //
	SATURDAY("party");

	private String slangName;

	/**
	 * Enums are actual classes. We can create constructors and hold state on
	 * the enum types themselves.
	 *
	 * Enum constructors *could* be left as package-private, however the
	 * compiler will still compile them as private. There is no such thing as a
	 * package-private enum constructor - only private.
	 */
	private DaysOfTheWeek(String slangName) {
		this.slangName = slangName;
	}

	public String getSlangName() {
		return this.slangName;
	}

	public boolean isWeekend() {
		return this == DaysOfTheWeek.SATURDAY || this == DaysOfTheWeek.SUNDAY;
	}
}