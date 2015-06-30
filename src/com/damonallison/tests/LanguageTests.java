package com.damonallison.tests;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.damonallison.utils.DaysOfTheWeek;
import com.damonallison.utils.Language;
import com.google.common.base.Preconditions;

public class LanguageTests {

	/**
	 * Shows:
	 * <ol>
	 * <li>Multiple variable declarations can happen on a single line.</li>
	 * <li>Assignment is R to L. Multiple assignment can happen on a single
	 * line.</li>
	 * <li>Logical compliment operator can be used on boolean expressions</li>
	 * </ol>
	 */
	@Test
	public void testAssignmentAndLogicalCompliment() {
		// Multiple declarations are allowed on a single line. Separate with
		// commas.
		int x = 10, y = 20;

		// Multiple assignment can happen. Assignment operators are evaluated R
		// to L.
		// All other operators with equal precedence are evaluated L to R.
		x = y = 200;

		assertTrue(x == 200 && y == 200);

		int remainder = 200 - (3 * (200 / 3));
		assertEquals(remainder, 200 % 3);

		// Logical compliment operator works on booleans only (safer than C).
		boolean t = !!true;
		assertTrue(t);
	}

	/**
	 * Increment operators can be applied before or after the operand. If
	 * applied *after* the operand, the operand is returned as it's original
	 * value, before the increment is applied.
	 * 
	 * Why did the C language people invent both the prefix and postfix
	 * operators? As a shorthand? These operators aren't used much, but I'm sure
	 * they have caused many developers subtle bugs over time.
	 */
	@Test
	public void testIncrementOperators() {

		int i = 10;
		// Prefix : the operand (10) is returned prior to
		// being incremented.
		assertEquals(10, i++);
		assertEquals(11, i);

		assertEquals(12, ++i);
		assertEquals(12, i);

		String str = i > 10 ? "Yes" : "No";
		assertEquals("Yes", str);

	}

	@Test
	public void testBitwiseOperators() {
		// 1111
		int mask = 0xF;
		assertEquals(15, mask);

		// The bitwise compliment operator (~) will invert a bit pattern
		// (0 -> 1, 1 -> 0)
		mask = ~mask;
		assertEquals(-16, mask);
		mask = ~mask;
		assertEquals(15, mask);

		// >>> will always shift a 0 into the left most position.
		// .. will shift a 0
		mask = mask >>> 1;
		// 0111
		assertEquals((4 + 2 + 1), mask);
		mask = mask >> 0; // no-op
		assertEquals((4 + 2 + 1), mask);

		// 11100
		mask = mask << 2;
		assertEquals((16 + 8 + 4), mask);
		// 111000
		mask = mask << 1;
		assertEquals((32 + 16 + 8), mask);

		// Exclusive OR (XOR) - A or B but not both.
		// 111000 + 001111 == 110111
		mask = mask ^ 0xF;
		assertEquals(32 + 16 + 4 + 2 + 1, mask);

		// Inclusive OR - A or B OR both.
		mask = mask | 0xF;
		assertEquals(32 + 16 + 8 + 4 + 2 + 1, mask);

		// AND : A AND B
		// 111111 & 000100
		mask = mask & 0x4;
		assertEquals(4, mask);
	}

	/**
	 * {@code instanceof} is used to determine if an object is an instance of a
	 * class or interface. {@code instanceof} will return true for all types the
	 * object conforms to, up to {@code Object}.
	 */
	@Test
	public void testTypeComparison() {
		String s = "damon";
		assertTrue(s instanceof String);
		assertTrue(s instanceof java.io.Serializable);
		assertTrue(s instanceof Object);

		// instanceof can also be used to determine if an object is an instance
		// of a sublcass. For example, if we are given an interface, we could
		// determine if an instance is an instance of a sublcass.
		// Here, we are given an interface (Serializable). We can determine
		// if the object is a particular subclass of Serializable, String.
		java.io.Serializable ser = (java.io.Serializable) s;
		assertTrue(ser instanceof String);

		// keep in mind that "null" is not an instance of anything
		assertFalse(null instanceof Object);
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
	public void testSwitch() {
		/**
		 * Java 1.7 allows you to switch on {@code String} values.
		 * 
		 * {@code break} is necessary because java will fall through.
		 */

		String s = "damon";
		// You need to always, manually check for null prior to using
		// a string in the switch statement to prevent a NullPointerException.
		Preconditions.checkNotNull(s);

		switch (s.toLowerCase()) {
		case "damon":
			break;
		case "other":
			fail();
		default:
			fail();
		}

		// Switch works with primitive types, enum types, String, and a few
		// classes that wrap primitive types (Character, Byte, Short, Integer)
		byte x = 0x3;
		switch (x) {
		case 3:
			break;
		default:
			fail();
		}
	}

	@Test(expected = NullPointerException.class)
	public void testNullSwitch() {
		String s = null;
		switch (s) {
		default:
			fail();
		}
	}

	@Test
	public void testVarArgs() {
		String[] expected = new String[] { "damon", "allison" };
		assertArrayEquals(expected, Language.echoVarArgs("damon", "allison"));
	}

}
