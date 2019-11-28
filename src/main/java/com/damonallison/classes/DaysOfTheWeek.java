package com.damonallison.classes;

/**
 * Java enums are more robust than C or other languages.
 * Java enums can:
 * <ul>
 * <li>Hold state.
 * <li>Declare methods.
 * </ul>
 * <p>
 * All java enums inherit from java.lang.Enum. An enum cannot extend anything
 * else.
 */
public enum DaysOfTheWeek {

    SUNDAY("lazy"),
    MONDAY("uhhh"),
    TUESDAY("uh"),
    WEDNESDAY("hump"),
    THURSDAY("better"),
    FRIDAY("tgif"),
    SATURDAY("party");

    private String slangName;

    /**
     * Enums are actual classes. We can create constructors and hold state on
     * the enum types themselves..
     * <p>
     * Enum constructors are always private.
     */
    DaysOfTheWeek(String slangName) {
        this.slangName = slangName;
    }

    public String getSlangName() {
        return this.slangName;
    }

    public boolean isWeekend() {
        return this == DaysOfTheWeek.SATURDAY || this == DaysOfTheWeek.SUNDAY;
    }
}