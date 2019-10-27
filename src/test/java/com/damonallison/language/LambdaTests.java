package com.damonallison.language;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.damonallison.classes.Bike;
import com.damonallison.classes.Bike.BikeBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Lambda expressions (lambdas) allow you to express instances of single-method
 * classes more compactly.
 *
 * Anonymous classes simplify the syntax for class creation by allowing you to
 * define an unnamed class inline. For classes with a single method, this syntax
 * is still very verbose. Lambdas allow you to write a single-method anonymous
 * class more concisely.
 *
 * Any interface that is a {@link FunctionalInterface} can be represented as a
 * lambda expression. A {@link FunctionalInterface} contains exactly one
 * abstract function, such as {@link Predicate}.
 *
 * @author Damon Allison
 */
public class LambdaTests {

	/**
	 * Lambda expressions are simply syntactic shortcuts for implementing an
	 * interface with a single abstract method. Rather than creating an
	 * anonymous class and overriding the single method, lambda expressions
	 * allow you to simplify the syntax.
	 */
	@Test
	public void lambdaExpressionAssignment() {

		Predicate<String> isLongAnonymousClass = new Predicate<String>() {
			@Override
			public boolean test(String s) {
				return s.length() > 10;
			}
		};
		Predicate<String> isLongLambdaExpression = s -> s.length() > 10;

		assertTrue(!isLongAnonymousClass.test("damon"));
		assertTrue(isLongAnonymousClass.test("damon allison"));

		assertTrue(!isLongLambdaExpression.test("damon"));
		assertTrue(isLongLambdaExpression.test("damon allison"));
	}

	/**
	 * Shows examples of filtering with and without lambda expressions.
	 */
	@Test
	public void lambdaExpressionBasics() {

		// create data
		List<Bike> bikes = new ArrayList<>(100);
		for (int i = 1; i < 101; i++) { // gears need to be >= 1
			bikes.add(BikeBuilder.newBuilder().setGear(i).setSpeed(i)
					.setWheelCount(2).build());
		}

		//
		// filter - without lambdas
		//
		// Without lambdas, we must use an anonymous class (or a named class)
		// to create an instance of a predicate, implementing the interface's
		// only method. Here, we create an anonymous class instance of
		// Predicate<Bike>.
		//
		// Note that Predicate is a functional interface.
		//
		Predicate<Bike> bikePred = new Predicate<Bike>() {
			@Override
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

		//
		// filter - with lambdas
		//
		// With lambdas, we create an instance of the Predicate type
		// using lambda syntax. It is equivalent to "bikePred" above.
		//
		// Note that in the filter method, the parameter "b" is enclosed
		// by parens. Lambda expressions that require multiple parameters must
		// be enclosed
		// in parens. In this case, with only one param, we could have omitted
		// the parens.
		//
		// lambda expression equivalent to bikePred above
		List<Bike> fastBikes2 = bikes.stream().filter((b) -> b.getSpeed() > 50)
				.collect(Collectors.toList());

		assertEquals(fastBikes, fastBikes2);

		//
		// for-each (side-effects) - simplifies iteration
		//
		// Notice here we omitted the parens. This is legal with a single
		// parameter only.
		//
		// Also notice that the lambda implementation is defined in a block. Any
		// multi-line lambda must be defined in a block (and include a {@code
		// return} if appropriate).
		//
		fastBikes.forEach(b -> {
			assertTrue(b.getSpeed() > 50);
		});

		//
		// map
		//
		List<String> speedStrings = new ArrayList<>();
		// parallelStream will execute on a tread pool (fork/join pool)
		fastBikes.stream().map(b -> b.getSpeed()).forEach(speed -> {
			speedStrings.add("You're fast " + speed.toString());
		});
		assertTrue(speedStrings.size() == fastBikes.size());
	}

	/**
	 * Sorts bikes by speed, ascending.
	 */
	private class SpeedComparator implements Comparator<Bike> {
		@Override
		public int compare(Bike a, Bike b) {
			return Integer.compare(a.getSpeed(), b.getSpeed());
		}
	}

	private ArrayList<Bike> generateBikeListSortedDescending() {
		final ArrayList<Bike> bikes = new ArrayList<Bike>();
		BikeBuilder bb = BikeBuilder.newBuilder();

		// Adding in reverse order to test sort later on.
		for (int i = 99; i >= 1; i--) {
			bb.setSpeed(i);
			bb.setGear(i);
			bb.setWheelCount(i);
			bikes.add(bb.build());
		}
		return bikes;
	}

	@Test
	public void lambdaExpressionSort() {
		ArrayList<Bike> bikes = generateBikeListSortedDescending();

		// Array Sort - sorts using Arrays.sort()
		//
		// Is there any better way to create a strongly typed array?
		// This *should be* a one-line function!
		//
		Bike[] bikeArray = new Bike[bikes.size()];
		bikes.toArray(bikeArray);

		// Verify the current sort order is descending.
		assertTrue(bikeArray[0].getSpeed() == 99);
		assertTrue(bikeArray[bikeArray.length - 1].getSpeed() == 1);

		// Sort
		Arrays.sort(bikeArray, (a, b) -> new SpeedComparator().compare(a, b));

		// Verify the current sort order is ascending.
		assertTrue(bikeArray[0].getSpeed() == 1);
		assertTrue(bikeArray[bikeArray.length - 1].getSpeed() == 99);
	}

	@Test
	public void methodReferenceSort() {
		ArrayList<Bike> bikes = generateBikeListSortedDescending();
		/**
		 * Method References
		 *
		 * If a lambda expression does nothing but invoke a method like
		 * "compare" above, you can use a method reference.
		 *
		 * A method reference *is* a lambda expression
		 *
		 */
		Bike[] bikeArray2 = new Bike[bikes.size()];
		bikes.toArray(bikeArray2);

		// Method reference example. Notice we don't use a lambda expression
		// as we do above with : (a, b) -> new BikeComparator().compare(a, b)
		Arrays.sort(bikeArray2, (new SpeedComparator())::compare);

		assertTrue(bikeArray2[0].getSpeed() == 1);
		assertTrue(bikeArray2[bikeArray2.length - 1].getSpeed() == 99);
	}

	/**
	 * Lambda expressions are a natural fit for use with java streams. Lambda
	 * expressions are used for various stream pipeline stages.
	 */
	@Test
	public void testStreams() {
		ArrayList<Bike> bikes = generateBikeListSortedDescending();
		/**
		 * Streams (Java 1.8) provide a sequential stream of objects which can
		 * be processed functionally.
		 *
		 * Operations on streams are called "aggregate operations" in the java
		 * docs. Aggregate (stream) operations are pipeline based - think
		 * reactive programming.
		 */
		List<Bike> filtered2 = bikes.parallelStream().filter(b -> {
			/**
			 * Because we are on a parallel stream, this filtering will be done
			 * concurrently.
			 *
			 * Note : this function was manually split into two lines to show
			 * that anonymous functions can be implemented with a block rather
			 * than as a single line.
			 */
			int gear = b.getGear();
			return gear % 2 == 0;
		}).sorted((b, b2) -> Integer.compare(b.getGear(), b2.getGear()))
		.collect(Collectors.toList());

		assertTrue(filtered2.size() == 49);
		assertTrue(filtered2.get(0).getGear() == 2, "Should be sorted by gear, ascending");

				// Map
		int[] gearMap = bikes.stream().mapToInt(bike -> bike.getGear()).toArray();

		assertTrue(gearMap[0] == 99);
		assertTrue(gearMap[gearMap.length - 1] == 1);

	}
}
