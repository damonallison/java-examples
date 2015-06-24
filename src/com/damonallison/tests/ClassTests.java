package com.damonallison.tests;

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
}
