package com.damonallison.tests.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.damonallison.classes.DaysOfTheWeek;

public class EnumTests {
	/**
	 * Enums in java are more like classes - they can hold state and expose methods.
	 */
	@Test
	public void testEnums() {

		// Enums can have methods.
		assertTrue(DaysOfTheWeek.SATURDAY.isWeekend());

		// Enums extend from java.lang.Enum and therefore
		// inherit all the properties from that structure.
		for (DaysOfTheWeek d : DaysOfTheWeek.values()) {

			// Creating an enum value from a string.
			DaysOfTheWeek d2 = DaysOfTheWeek.valueOf(d.name());
			assertTrue(d == d2);
			assertEquals(d, d2);
		}
	}

	/**
	 * Attempting to parse an invalid value will throw an {@link IllegalArgumentException}
	 */
	public void testInvalidName() {
		DaysOfTheWeek.valueOf("not valid");
	}
}
