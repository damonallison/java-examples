package com.damonallison.tests;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.Test;

import com.damonallison.annotations.ClassHeader;
import com.damonallison.classes.Bike;
import com.damonallison.classes.Bike.BikeBuilder;
import com.damonallison.utils.DaysOfTheWeek;
import com.damonallison.utils.Language;

@ClassHeader(author = "Damon Allison", date = "2015/05/11", reviewers = {
		"Damon", "Allison" })
public class AllTests {

	/**
	 * A simple interface that will be used to illustrate anonymous functions.
	 * An anonymous concrete class will be created that implements this
	 * interface.
	 * 
	 * <pre>
	 * {@code
	 * IRun runner = new IRun() {
	 *   public bool run() {
	 *     return true;
	 *   }
	 * }
	 * </pre>
	 *
	 */
	private interface IRun {
		boolean run();
	}

	@Test
	public void testAnonymousClasses() {
		int x = 10;
		IRun runner = new IRun() {

			// NOTE : you cannot declare constructors in an anonymous class.
			@Override
			public boolean run() {
				return x > 1;
			}
		};
		assertTrue(runner.run());
	}

	@Test
	public void testAnonymousClasses2() throws Exception {

		// Anonymous classes can only capture final or "effectively final"
		// variables.
		// Don't rely on "effectively final". If they are going to be captured,
		// just make them final.
		final Integer capturedInt = 100;
		Callable<Integer> callable = new Callable<Integer>() {

			@Override
			public Integer call() {
				return capturedInt;
			}

		};
		assertTrue(callable.call() == 100);
	}

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
		final Language lang = new Language();
		assertEquals("i know you, damon", lang.switchExample("damon"));
		assertEquals("who are you?", lang.switchExample("unknown"));
		assertNull(lang.switchExample(null));

		String[] expected = new String[] { "damon", "allison" };
		assertArrayEquals(expected, lang.varArgsExample("damon", "allison"));

	}

	@Test
	public void testInnerClass() {

		BikeBuilder bb = new BikeBuilder();
		bb.setSpeed(10);
		bb.setGear(1);
		Bike b = bb.build();
		/**
		 * Instantiating an instance of an (non-static) inner class requires an
		 * instance of the outer class.
		 * 
		 * Static inner classes are instantiated the same as a top level static
		 * class. Being "inner" doesn't make it *any* different than a top level
		 * static class - it's simply packaged within another class for
		 * encapsulation purposes.
		 */
		Bike.Mechanic m = b.new Mechanic();
		m.name = "Damon";
		assertEquals("Mechanic Damon for bike with speed 10", m.toString());
	}

	/**
	 * A helper function that will perform a filter based on a predicate. This
	 * has to be baked into the system library somewhere (streams?), but here we
	 * are..
	 * 
	 * @FunctionalInterface. A functional interface has a single abstract
	 *                       method. Predicate is a functional interface with a
	 *                       single "test" abstract method.
	 * 
	 * @param list
	 * @param pred
	 * @return a filtered list containing only items passing pred.
	 */
	public <T> ArrayList<T> filter(ArrayList<T> list, Predicate<T> pred) {
		Objects.requireNonNull(list);
		Objects.requireNonNull(pred);

		ArrayList<T> ret = new ArrayList<T>();
		for (T item : list) {
			if (pred.test(item)) {
				ret.add(item);
			}
		}
		return ret;
	}

	private class BikeComparator implements Comparator<Bike> {
		public int compare(Bike a, Bike b) {
			return Integer.compare(b.getSpeed(), a.getSpeed());
		}
	}

	@Test
	public void testLambdaExpressions() {
		final ArrayList<Bike> bikes = new ArrayList<Bike>();
		BikeBuilder bb = new Bike.BikeBuilder();
		for (int i = 1; i < 100; i++) {
			bb.setSpeed(i);
			bb.setGear(i);
			bb.setWheelCount(i);
			bikes.add(bb.build());
		}

		// Filter
		//
		// We simply pass the lambda in place of a dedicated "Predicate<Bike>"
		// instance. This works because Predicate<T> is a functional interface.
		//

		Predicate<Bike> p = new Predicate<Bike>() {
			@Override
			public boolean test(Bike b) {
				return b.getGear() % 2 == 0;
			}
		};
		ArrayList<Bike> f = this.filter(bikes, p);
		ArrayList<Bike> filtered = this.filter(bikes,
				(b) -> b.getGear() % 2 == 0);
		assertTrue(filtered.size() == 49); // Evens 2 - 98
		assertEquals(f, filtered);

		// Streams look promising.
		List<Bike> filtered2 = bikes.stream().filter(b -> {
			// manually making this a multiple statement block
			// to prove we can use generic blocks as functions.
				int gear = b.getGear();
				return gear % 2 == 0;
			}).collect(Collectors.toList());
		assertTrue(filtered2.size() == 49);

		// Map
		int[] gearMap = bikes.stream().mapToInt(bike -> bike.getGear())
				.toArray();

		assertTrue(gearMap[gearMap.length - 1] == 99);

		// Sort
		//
		// Is there any better way to create a strongly typed array?
		// This *should be* a one-line function!
		//
		Bike[] bikeArray = new Bike[bikes.size()];
		bikes.toArray(bikeArray);

		assertTrue(bikeArray[0].getSpeed() == 1);
		assertTrue(bikeArray[bikeArray.length - 1].getSpeed() == 99);

		// Sorts by speed, descending.
		// Uses a strongly typed Comparator object.
		Arrays.sort(bikeArray, (a, b) -> new BikeComparator().compare(a, b));

		assertTrue(bikeArray[0].getSpeed() == 99);
		assertTrue(bikeArray[bikeArray.length - 1].getSpeed() == 1);

		// Method references
		//
		// Rather than calling an existing method via a lambda expression, a
		// method reference could be used.

		BikeComparator bc = new BikeComparator();
		Bike[] bikeArray2 = new Bike[bikes.size()];
		bikes.toArray(bikeArray2);

		assertTrue(bikeArray2[0].getSpeed() == 1);
		assertTrue(bikeArray2[bikeArray2.length - 1].getSpeed() == 99);

		// Method reference example. Notice we don't use a lambda expression
		// as we do above with : (a, b) -> new BikeComparator().compare(a, b)
		Arrays.sort(bikeArray2, bc::compare);

		assertTrue(bikeArray2[0].getSpeed() == 99);
		assertTrue(bikeArray2[bikeArray2.length - 1].getSpeed() == 1);
	}
}
