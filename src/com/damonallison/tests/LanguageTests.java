package com.damonallison.tests;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.damonallison.utils.DaysOfTheWeek;
import com.damonallison.utils.Language;

public class LanguageTests {

	@Test
	public void booleanAnding() {
		boolean valid = true;
		Boolean[] others = { new Boolean(true), new Boolean(false) };

		for (Boolean bool : others) {
			valid &= bool;
		}
		System.out.println("Final : " + valid);
	}

	/*
	 * Enums in java are more like classes - they can expose methods.
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

	@Test
	public void testLanguage() {
		assertEquals("i know you, damon", Language.switchExample("damon"));
		assertEquals("who are you?", Language.switchExample("unknown"));

		String[] expected = new String[] { "damon", "allison" };
		assertArrayEquals(expected, Language.varArgsExample("damon", "allison"));
	}

	@Test(expected = NullPointerException.class)
	public void testNullSwitch() {
		Language.switchExample(null);
	}

}
