package com.damonallison.libraries.stdlib;

import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Java provides wrapper types for primitives and the compiler will automatically box/unbox when needed.
 *
 * Use object wrappers when:
 *
 * * You are calling code that expects {@link Object}.
 * * For using convenience functions supplied by the object ({@code MIN_VALUE || MAX_VALUE}).
 * * For using class methods like value conversion to/from strings.
 */
public class NumbersTests {

	/**
	 * Shows converting numbers to / from different types
	 */
	@Test
	public void testNumberCasting() {
		int i = 10;
		long l = Integer.valueOf(i).longValue(); // type cast

		Integer iobj = i; // boxes
		Long lobj = l;    // boxes

		assertEquals(iobj.longValue(), lobj.longValue()); // unboxes
		assertTrue(Integer.compare(i, Long.valueOf(l).intValue()) == 0);

		// Convert to a string (2 ways)
		String s = Integer.toString(i);
		s = String.valueOf(i);

		// Convert from a string
		i = Integer.parseInt(s); // preferred - no garbage wrapper class.
		i = Integer.valueOf(s);  // will create a garbage wrapper class.
	}

	@Test
	void testNumberPrinting() {

		// %f == float
		// %d == decimal
		double f = Math.PI;
		int i = 1;
		String s = String.format("%d. PI is %1.2f", i, f);

		assertEquals("1. PI is 3.14", s);


		// DecimalFormat is more flexible and complex.
		// # == number placeholder
		//
		DecimalFormat df = new DecimalFormat("##.##");
		s = df.format(f);
		assertEquals("3.14", s);

		// The formatter will round this up.
		s = df.format(100);
		assertEquals("100", s);

		// The formatter will round this down
		s = df.format(1);
		assertEquals("1", s);
	}

	@Test
	public void testMath() {
		assertEquals(10, Math.round(10.49));

		// .5 will always round up
		assertEquals(11, Math.round(10.5));
		assertEquals(12, Math.round(11.5));
		assertEquals(0, Math.round(-0.5));

		double d = 44.5;
		assertEquals(Double.valueOf(45), Double.valueOf(Math.ceil(d))); // returns the smallest decimal *larger* than d.
		assertEquals(Double.valueOf(44), Double.valueOf(Math.floor(d))); // returns the largest decimal *smaller* than d.
	}

	@Test
	public void testRandom() {
		// By default Math.random() will returns a pseudo-random between 0.0 and 1.0 (but not 1.0).
		// 0.0 <= number < 10.0
		int number = (int)(Math.random() * 10);

		assertTrue(number < 10);

		// If you don't want to deal in doubles (or want to create more than one random number),
		// use Random or SecureRandom.
		Random r = new Random();
		assert(r.nextInt(10) < 10);
	}
}
