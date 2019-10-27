package com.damonallison.language;

import com.google.common.base.Preconditions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Examples of rather obscure operator usage that I'll probably forget.
 */
public class OperatorTests {

	/**
	 * Multiple variable declarations can happen on a single line. Assignment is R to L.
	 */
	@Test
	void testMultipleVariableDeclaration() {
		// Multiple declarations are allowed on a single line. Separate with commas.
		int x, y;

		// Multiple assignment is possible.
		// Assignment operators are evaluated R to L.
		// All other operators with equal precedence are evaluated L to R.
		x = y = 200;
		assertTrue(x == 200 && y == 200);
	}

	/**
	 * Precedence is L to R. Use parentheses to force your desired order.
	 *
	 * As a general rule, wrap ambiguous expressions with parentheses to remove all ambiguity.
	 */
	@Test
	void testPrecedence() {
		// L to R evaluation. Since * and / have equal precedence, you need the inner
		// parentheses around 200 / 3.
		int remainder = 200 - (3 * (200 / 3));
		assertEquals(remainder, 200 % 3);
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
	void testIncrementOperators() {

		int i = 10;
		// Prefix : the operand (10) is returned prior to being incremented.
		assertEquals(10, i++);
		assertEquals(11, i);

		assertEquals(12, ++i);
		assertEquals(12, i);

		String str = i > 10 ? "Yes" : "No";
		assertEquals("Yes", str);

	}

	@Test
	void testBitwiseOperators() {
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
	void testTypeComparison() {
		String s = "damon";
		assertTrue(s instanceof String);
		assertTrue(s instanceof java.io.Serializable);
		assertTrue(s instanceof Object);

		// instanceof can also be used to determine if an object is an instance
		// of a sublcass. For example, if we are given an interface, we could
		// determine if an instance is an instance of a sublcass.
		// Here, we are given an interface (Serializable). We can determine
		// if the object is a particular subclass of Serializable, String.
		java.io.Serializable ser = s;
		assertTrue(ser instanceof String);

		// keep in mind that "null" is not an instance of anything
		assertFalse(null instanceof Object);
	}

	@Test
	void testVarArgs() {
		String[] expected = new String[] { "damon", "allison" };
		assertArrayEquals(expected, OperatorTests.echoVarArgs("damon", "allison"));
	}


	/**
	 * This function can be called with an array or a sequence of strings.
	 * Within the method body, the argument is treated as an array.
	 *
	 * This declaration is identical to:
	 * {@code public static String[] echoVarArgs(String[] s)}
	 *
	 * @param s An array of strings to print.
	 * @return A copy of s.
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
