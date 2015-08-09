package com.damonallison.tests.classes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.damonallison.classes.Bike;

public class ClassTests {

	@Test
	public void testStaticInstanceVariables() {
		/**
		 * Static instance variables (and static constructors) are invoked prior
		 * to it's first usage. (When the class is first loaded or used, not
		 * sure which).
		 */
		assertTrue(Bike.CLASS_CREATED);
		assertTrue(Bike.staticConstrucoreInvoked());
	}

	/**
	 * Nested static classes are different than non-static "inner classes".
	 * Nested static classes do not need an instance of the outer class to
	 * create or use.
	 * 
	 * In this example, we are creating and using a nested static class without
	 * having to create a {@code Bike} object first.
	 */
	@Test
	public void testNestedStaticClasses() {

		Bike.BikeBuilder bb = Bike.BikeBuilder.newBuilder();
		Bike b = bb.setGear(10).setSpeed(10).setWheelCount(2).build();
		assertEquals(2, b.getWheelCount());
	}
}
