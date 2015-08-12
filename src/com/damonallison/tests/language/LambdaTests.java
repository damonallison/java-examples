package com.damonallison.tests.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.junit.Test;

import com.damonallison.classes.Bike;
import com.damonallison.classes.Bike.BikeBuilder;

/**
 * Lambda expressions (lambdas) simplify the syntax for creating anonymous classes.
 * 
 * Anonymous classes simplify the syntax for class creation by allowing you to 
 * define an unnamed class. For classes with a single method, this syntax is still
 * very verbose. Lambdas allow you to write a single-method anonymous class more concisely.
 * 
 * Any interface that is a {@link FunctionalInterface} can be represented as a lambda expression.
 * A {@link FunctionalInterface} contains exactly one abstract function, such as {@link Predicate}.
 * 
 * When
 * 
 * @author Damon Allison
 */
public class LambdaTests {
	
	/** 
	 * Shows the basics of lambda expression usage. 
	 */
	@Test
	public void simpleLambdaExpressions() {
		
		// create data
		List<Bike> bikes = new ArrayList<>(100);
		for (int i = 1; i < 101; i++) {
			bikes.add(BikeBuilder.newBuilder().setGear(i).setSpeed(i).setWheelCount(2).build());
		}

		// filter - without lambdas
		Predicate<Bike> bikePred = new Predicate<Bike>() {
			public boolean test(Bike b) {
				return b.getSpeed() > 50;
			}
		};
		List<Bike> fastBikes = new ArrayList<>();
		for (Bike b : bikes) {
			if (bikePred.test(b)) {
				fastBikes.add(b);
			}
		}
		assertEquals(50, fastBikes.size());
				
		// filter - with lambdas
		fastBikes = bikes.stream().filter((b) -> {
			return b.getSpeed() > 50;
		}).collect(Collectors.toList());
		assertEquals(50, fastBikes.size());

		
		// for-each (side-effects) - simplifies iteration
		fastBikes.forEach(b -> assertTrue(b.getSpeed() > 50));
		
		// map
		List<String> speedStrings = new ArrayList<>(fastBikes.size());
		fastBikes.parallelStream() // parallelStream will execute on a tread pool (fork/join pool)
			.map(b -> b.getSpeed())
			.forEach(speed -> speedStrings.add("You're fast " + speed.toString())); 
		
		assertTrue(speedStrings.size() == fastBikes.size());
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
	public <T> List<T> filter(ArrayList<T> list, Predicate<T> pred) {
		Objects.requireNonNull(list);
		Objects.requireNonNull(pred);

		return list.stream().filter(pred).collect(Collectors.toList());
	}

	/**
	 * Sorts bikes by speed, ascending.
	 */
	private class SpeedComparator implements Comparator<Bike> {
		public int compare(Bike a, Bike b) {
			return Integer.compare(a.getSpeed(), b.getSpeed());
		}
	}

	/**
	 * Lambda expressions allow you to express instances of single-method
	 * classes more compactly.
	 * 
	 * Any interface that is a "functional interface" - an interface that
	 * contains only *1* abstract method - can be represented with a lambda
	 * expression.
	 * 
	 * The lambda expression creates a class that implements the interface
	 * without having to specify the name of the interface's method.
	 * 
	 * GOLDEN RULE : LAMBDA EXPRESSIONS ONLY WORK FOR FUNCTIONAL INTERFACES.
	 */
	@Test
	public void testLambdaExpressions() {
		final ArrayList<Bike> bikes = new ArrayList<Bike>();
		BikeBuilder bb = BikeBuilder.newBuilder();

		// Adding in reverse order to test sort later on.
		for (int i = 99; i >= 1; i--) {
			bb.setSpeed(i);
			bb.setGear(i);
			bb.setWheelCount(i);
			bikes.add(bb.build());
		}

		/**
		 * Filter - using a predicate.
		 */
		Predicate<Bike> p = new Predicate<Bike>() {
			@Override
			public boolean test(Bike b) {
				return b.getGear() % 2 == 0;
			}
		};
		List<Bike> f = this.filter(bikes, p);

		/**
		 * Filter - using a lambda expression.
		 * 
		 * This lambda expression implements the Predicate<T> interface and the
		 * 'test' method.
		 * 
		 * Notice that single line lambda expressions can omit the "return"
		 * keyword and {} block.
		 */
		List<Bike> filtered = this.filter(bikes, (b) -> {
			// Do anything here..
			return b.getGear() % 2 == 0;
		});
		assertTrue(filtered.size() == 49); // Evens 98 -> 2
		assertEquals(f, filtered);

		/**
		 * Streams (Java 1.8) provide a sequential stream of objects which can
		 * be processed functionally.
		 * 
		 * Operations on streams are called "aggregate operations" in the java
		 * docs. Aggregate (stream) operations are pipeline based - think
		 * reactive programming.
		 */
		List<Bike> filtered2 = bikes
				.stream()
				.parallel()
				.filter(b -> {
					/**
					 * Because we are on a parallel stream, this filtering will
					 * be done concurrently.
					 * 
					 * Note : this function was manually split into two lines to
					 * show that anonymous functions can be implemented with a
					 * block rather than as a single line.
					 */
					int gear = b.getGear();
					return gear % 2 == 0;
				})
				.sorted((b, b2) -> new Integer(b.getGear())
						.compareTo((Integer) b2.getGear()))
				.collect(Collectors.toList());

		assertTrue(filtered2.size() == 49);
		assertTrue("Should be sorted by gear, ascending", //
				filtered2.get(0).getGear() == 2);

		// Map
		int[] gearMap = bikes.stream().mapToInt(bike -> bike.getGear())
				.toArray();

		assertTrue(gearMap[0] == 99);
		assertTrue(gearMap[gearMap.length - 1] == 1);

		// Array Sort
		//
		// Is there any better way to create a strongly typed array?
		// This *should be* a one-line function!
		//
		Bike[] bikeArray = new Bike[bikes.size()];
		bikes.toArray(bikeArray);

		assertTrue(bikeArray[0].getSpeed() == 99);
		assertTrue(bikeArray[bikeArray.length - 1].getSpeed() == 1);

		// Sorts by speed, descending.
		// Uses a strongly typed Comparator object.
		Arrays.sort(bikeArray, (a, b) -> new SpeedComparator().compare(a, b));
		assertTrue(bikeArray[0].getSpeed() == 1);
		assertTrue(bikeArray[bikeArray.length - 1].getSpeed() == 99);

		/**
		 * Method References
		 * 
		 * If a lambda expressiond does nothing but invoke a method like
		 * "compare" above, you can use a metho reference.
		 * 
		 * A method reference *is* a lambda expression
		 * 
		 */
		Bike[] bikeArray2 = new Bike[bikes.size()];
		bikes.toArray(bikeArray2);

		assertTrue(bikeArray2[0].getSpeed() == 99);
		assertTrue(bikeArray2[bikeArray2.length - 1].getSpeed() == 1);

		// Method reference example. Notice we don't use a lambda expression
		// as we do above with : (a, b) -> new BikeComparator().compare(a, b)
		Arrays.sort(bikeArray2, (new SpeedComparator())::compare);

		assertTrue(bikeArray2[0].getSpeed() == 1);
		assertTrue(bikeArray2[bikeArray2.length - 1].getSpeed() == 99);
	}

	@Test
	public void testLambdaExpressionSyntax() {

		Predicate<String> isLongAnonymousClass = new Predicate<String>() {
			public boolean test(String s) {
				return s.length() > 10;
			}
		};

		assertTrue(!isLongAnonymousClass.test("damon"));
		assertTrue(isLongAnonymousClass.test("damon allison"));

		/**
		 * Lambda expressions are simply syntactic shortcuts for implementing an
		 * interface with a single abstract method. Rather than creating an
		 * anonymous class and overriding the single method as we did above,
		 * lambda expressions allow you to simplify the syntax.
		 */
		Predicate<String> isLongLambdaExpression = s -> s.length() > 10;

		assertTrue(!isLongLambdaExpression.test("damon"));
		assertTrue(isLongLambdaExpression.test("damon allison"));
	}
}
