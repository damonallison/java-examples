
package com.damonallison.tests.classes;

import com.damonallison.classes.Bike;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ClassTests
{


	/**
	 * The initialization order for members of a class:
	 *
	 * <ol>
	 *     <li>Static variables are initialized. They can be initialized to the results of a method call</li>
	 *     <li>Static initializer block(s)</li>
	 *     <li>Instance variables</li>
	 *     <li>Initializer Blocks</li>
	 *     <li>Constructor</li>
	 * </ol>
	 */
	@Test
	public void testInitializationOrder() {
		/**
		 * Static instance variables (and static constructors) are invoked prior
		 * to it's first usage. (When the class is first loaded or used, not
		 * sure which).
		 */
		// Proves the static initializer was invoked.
		assertTrue(Bike.STATIC_INITIALIZER_INVOKED);
		assertTrue(Bike.CLASS_CREATED);
		assertTrue(Bike.staticInitializerInvoked());

		Bike b = Bike.BikeBuilder
				.newBuilder()
				.setSpeed(100)
				.setGear(10)
				.setWheelCount(2).build();

		assertTrue(b.INSTANCE_CREATED);
		assertTrue(b.INITIALIZER_INVOKED);
	}

	/**
	 * Nested static classes are different than non-static "inner classes".
	 *
	 * Nested static classes do not need an instance of the outer class to
	 * create or use.
	 * 
	 * In this example, we are creating and using a nested static class without
	 * having to create a {@code Bike} object first.
	 *
	 * Think of a static nested class as any top-level class. The only difference
	 * is a static nested class was nested within another class for packaging
	 * purposes.
	 */
	@Test
	public void testNestedStaticClasses() {

		Bike.BikeBuilder bb = Bike.BikeBuilder.newBuilder();
		Bike b = bb.setGear(10).setSpeed(10).setWheelCount(2).build();
		assertEquals(2, b.getWheelCount());
	}

	/**
	 * Instantiating an instance of an (non-static) inner class requires an
	 * instance of the outer class.
	 *
	 * Static inner classes are instantiated the same as a top level static
	 * class. Being "inner" doesn't make it *any* different than a top level
	 * static class - it's simply packaged within another class for
	 * encapsulation purposes.
	 */
	@Test
	public void testInnerClass() {

		Bike.BikeBuilder bb = Bike.BikeBuilder.newBuilder();
		bb.setSpeed(10);
		bb.setGear(1);
		Bike b = bb.build();
		Bike.Mechanic m = b.new Mechanic();
		m.name = "Damon";
		assertEquals("Mechanic Damon for bike with speed 10", m.toString());
	}
}
