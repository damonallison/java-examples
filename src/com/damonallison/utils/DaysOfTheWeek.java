package com.damonallison.utils;

public enum DaysOfTheWeek {

	SUNDAY ("lazy"),
	MONDAY ("uhhh"),
	TUESDAY ("uh"),
	WEDNESDAY ("hump"),
	THURSDAY ("better"),
	FRIDAY ("tgif"),
	SATURDAY ("party");

	private String slangName;
	
	/**
	 * Enums are actual classes. We can create constructors and hold
	 * state on the enum types themselves. 
	 * @param myName
	 */
	DaysOfTheWeek(String slangName) {
		this.slangName = slangName;
	}
	
	public String getSlangName() {
		return this.slangName;
	}
	
	public boolean isWeekend() {
		return this == DaysOfTheWeek.SATURDAY || 
				this == DaysOfTheWeek.SUNDAY;
	}
}
