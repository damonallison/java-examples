package com.damonallison.classes;

import com.damonallison.classes.Bike.BikeBuilder;
import com.damonallison.classes.MountainBike.MountainBikeBuilder;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClassTests {

	/**
	 * Tests the initialization order for members of a class:
	 *
	 * <ol>
	 * <li>Static variables are initialized. They can be initialized to the
	 * results of a method call</li>
	 * <li>Static initializer block(s)</li>
	 * <li>Instance variables</li>
	 * <li>Initializer Blocks</li>
	 * <li>Constructor</li>
	 * </ol>
	 */
	@Test
	void testInitializationOrder() {
		/*
		  Static instance variables (and static constructors) are invoked prior
		  to it's first usage. (When the class is first loaded or used, not
		  sure which).
		 */
		// Proves the static initializer was invoked.
		assertTrue(Bike.STATIC_INITIALIZER_INVOKED);
		assertTrue(Bike.CLASS_CREATED);
		assertTrue(Bike.staticInitializerInvoked());

		Bike b = Bike.BikeBuilder.newBuilder().setSpeed(100).setGear(10)
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
	 * Think of a static nested class as any top-level class. The only
	 * difference is a static nested class was nested within another class for
	 * packaging purposes.
	 */
	@Test
	void testNestedStaticClasses() {

		Bike.BikeBuilder bb = Bike.BikeBuilder.newBuilder();
		Bike b = bb.setGear(10).setSpeed(10).setWheelCount(2).build();
		assertEquals(2, b.getWheelCount());
	}

	/**
	 * Instantiating an instance of an (non-static) inner class requires an
	 * instance of the outer class.
	 */
	@Test
	void testInnerClass() {

		Bike.BikeBuilder bb = Bike.BikeBuilder.newBuilder();
		bb.setSpeed(10);
		bb.setGear(1);
		Bike b = bb.build();
		Bike.Mechanic m = b.new Mechanic();
		m.name = "Damon";
		assertEquals("Mechanic Damon for bike with speed 10", m.toString());
	}

	/**
	 * IBike defines a default method {@code IBike.isInHighGear}. {@link Bike}
	 * overrides it.
	 *
	 * This test verifies that the default implementation is used and can be
	 * overridden by a class.
	 */
	@Test
	void testDefaultInterfaceMethodOverride() {

		// This class does not override the default {@code IBike.isInHighGear}
		// method.
		// Therefore, the interface default method is used.
		final class MyBike implements IBike {

			private int speed, gear, wheelCount;

			private MyBike(int speed, int gear, int wheelCount) {
				this.speed = speed;
				this.gear = gear;
				this.wheelCount = wheelCount;
			}

			@Override
			public int getSpeed() {
				return speed;
			}

			@Override
			public int getGear() {
				return gear;
			}

			@Override
			public int getWheelCount() {
				return wheelCount;
			}

			@Override
			public int compareTo(IBike obj) {
				return Integer.compare(this.getSpeed(), obj.getSpeed());
			}
		}

		MyBike mb = new MyBike(5, 5, 2);
		assertFalse(mb.isInHighGear(), "The IBike default of 10 is considered high. We should be in a low gear.");

		mb = new MyBike(20, 11, 2);
		assertTrue(mb.isInHighGear(), "The IBike default of 10 is considered high. We should be in a high gear.");

		//
		// IBike overrides {@code IBike.isInHighGear}.
		// This assertion verifies the override.
		//
		IBike b = BikeBuilder.newBuilder().setGear(4).setSpeed(10).setWheelCount(2).build();
		assertFalse(b.isInHighGear(), "Bike considers 5 a high gear. We should be in a low gear");
		b = BikeBuilder.newBuilder().setGear(6).setSpeed(10).setWheelCount(2).build();
		assertTrue(b.isInHighGear(), "Bike considers 5 a high gear. We should be in a high gear");
	}

	@Test
	void testComparable() {
		IBike b1 = BikeBuilder.newBuilder().setGear(4).setSpeed(10)
				.setWheelCount(1).build();
		IBike b2 = BikeBuilder.newBuilder().setGear(4).setSpeed(10)
				.setWheelCount(2).build();

		// Object equality should fail - it takes into account all fields.
		assertNotEquals(b1, b2, "Pointer equality should fail.");
		assertNotEquals(b1, b2, "Pointer equality should fail.");
		assertNotEquals(b1, b2, "Logical equality should fail.");

		// Compare should succeed - it only accounts for speed.
		assertEquals(0, b1.compareTo(b2), "Objects should compare the same.");
		assertEquals(0, b2.compareTo(b1), "Objects should compare the same.");

		// Compare should fail, speeds are different.
		b2 = BikeBuilder.newBuilder().setGear(4).setSpeed(20).setWheelCount(2)
				.build();
		assertEquals(-1, b1.compareTo(b2), "b2 is going faster than b1.");
		assertEquals(1, b2.compareTo(b1), "b1 is going slower than b2.");

		// Sort using a comparator. Comparator is a nice functional interface
		// that can be used
		// to arbitrarily sort an object.

		// Example creating a comparator using a method reference.
		Comparator<IBike> wheelComparator = Comparator
				.comparing(IBike::getWheelCount);
		IBike[] bikeArray = { b2, b1 };
		List<IBike> bikes = Arrays.asList(bikeArray);
		bikes.sort(wheelComparator);
		assertEquals(1, bikes.get(0).getWheelCount());
		assertEquals(2, bikes.get(1).getWheelCount());

		// This is equivalent to the method reference IBike::getSpeed. I'm
		// writing this here
		// in long handed form to show how you could write an arbitrary lambda
		// sort expression.
		Comparator<IBike> speedComparator = Comparator.comparing(IBike::getSpeed);
		bikes.sort(speedComparator);
		assertEquals(10, bikes.get(0).getSpeed());
		assertEquals(20, bikes.get(1).getSpeed());

		// Example reversing the comparator
		speedComparator = speedComparator.reversed();
		bikes.sort(speedComparator);
		assertEquals(20, bikes.get(0).getSpeed());
		assertEquals(10, bikes.get(1).getSpeed());
	}

	/**
	 * Uses a class which inherits from a non-Object superclass.
	 */
	@Test
	void testInheritance() {
		MountainBikeBuilder mbb = MountainBikeBuilder.newBuilder();
		mbb.setSpeed(1).setGear(1).setWheelCount(2);
		mbb.setMaxElevation(100);
		MountainBike mb = mbb.build();
		assertEquals(100, mb.getMaxElevation());
	}

	/**
	 * Java provides multiple *interface* inheritance, not multiple classes.
	 *
	 * By not providing multiple inheritance, java prevents problems that arise
	 * from fields that exist in multiple superclasses. If a field "value"
	 * exists in multiple superclasses, which field should the subclass use?
	 *
	 * Interface inheritance has a similar problem with default methods. If the
	 * same default method is defined on multiple interfaces (with different
	 * implementations), which method will the subclass use? Can we tell java
	 * which one should be used?
	 *
	 * In this example, the class MultipleInheritance inherits from two
	 * interfaces with a {@code getValue()} member. This is a compile error. In
	 * order to fix the error, you must implement the "class wins" rule and
	 * override the ambiguous member in your class implementation.
	 */
	@Test
	void testMultipleInterfaceInheritance() {
		MultipleInheritance mi = new MultipleInheritance();
		assertEquals(1, mi.getValue());
	}

	/**
	 * The term "polymorphism" is the ability for a single object to have many
	 * different forms, or be represented as different types.
	 *
	 * All objects are polymorphic - they can be represented as their type or as
	 * {@link Object}. Classes which implement interfaces or superclasses can be
	 * represented as those types in addition to {@link Object}.
	 *
	 */
	@Test
	void testPolymorphism() {
		// Shows polymorphism. Here, bike is represented as an IBike, Bike, and
		// MountainBike.
		// In all cases, the methods that are invoked are from the actual type,
		// not the type that
		// the object is casted to at the moment.
		IBike bike = MountainBikeBuilder.newBuilder().setMaxElevation(100)
				.setGear(1).setSpeed(10).setWheelCount(2).build();
		Bike asBike = (Bike) bike;
		MountainBike asMountainBike = (MountainBike) bike;

		// These comparisons are actually pointless - they'll always refer to
		// the same methods.
		// This shows the concept of polymorphism by showing the same object
		// being used as
		// different types.
		assertEquals(bike.getSpeed(), asBike.getSpeed());
		assertEquals(bike.getSpeed(), asMountainBike.getSpeed());
		assertEquals(asBike.getSpeed(), asMountainBike.getSpeed());
	}
}
