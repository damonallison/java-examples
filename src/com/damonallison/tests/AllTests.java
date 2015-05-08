package com.damonallison.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.Test;

import com.damonallison.classes.Bike;
import com.damonallison.utils.DaysOfTheWeek;
import com.damonallison.utils.Language;

public class AllTests {

	
//	final Language lang = new Language();
//	lang.switchExample(null);
//	lang.switchExample("damon");
//	lang.switchExample("nobody home");
//	lang.varArgsExample("Damon", "Allison");
//			
//	final CoolList<Integer> cl = new CoolList<Integer>(10);
//	cl.add(1);
//	cl.add(200);
//	cl.add(300);
//	System.out.println("printing cl " + cl);
//	
//	// Classes
//	Bike b = new Bike(0, 1, 2);
//	b.deltaSpeed(10);
//	assert(b.getSpeed() == 10);
//	
//	MountainBike mb = new MountainBike(10, 1, 2, 1000);

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
		
		String[] expected = new String[] {"damon", "allison"};
		assertArrayEquals(expected,
				lang.varArgsExample("damon", "allison"));
		
	}
	
	@Test
	public void testInnerClass() {
		Bike b = new Bike(10, 10, 10);
		Bike b2 = new Bike(10, 10, 10);
		
		/**
		 * Instantiating an instance of an inner class requires 
		 * an instance of the outer class. 
		 */
		Bike.Mechanic m = b.new Mechanic();
		m.name = "Damon";
		assertEquals("Mechanic Damon for bike with speed 10", m.toString());
	}
	
	@Test
	public void testAnonymousClasses() throws Exception {

		// Anonymous classes can only capture final or "effectively final" variables.
		// Don't rely on "effectively final". If they are going to be captured,
		// just make them final.
		final Integer capturedInt = 100;
		Callable<Integer>callable = new Callable<Integer>() {
			
			@Override
			public Integer call() {
				return capturedInt;
			}
			
		};
		assertTrue(callable.call() == 100);
	}
	

	/**
	 * A helper function that will perform a filter based on a predicate.
	 * This has to be baked into the system library somewhere (streams),
	 * but here we are..
	 * 
	 * Note that in order to use a lambda, the lambda must be used with a 
	 * @FunctionalInterface. A functional interface has a single abstract
	 * method. Predicate is a functional interface with a single "test"
	 * abstract method.
	 * 
	 * @param list
	 * @param pred
	 * @return
	 */
	public <T> ArrayList<T> filter(ArrayList<T>list, Predicate<T>pred) {
		Objects.requireNonNull(list);
		Objects.requireNonNull(pred);
		
		if (list == null || pred == null) {
			return null;
		}
		ArrayList<T> ret = new ArrayList<T>();
		for (T item : list) {
			if (pred.test(item)) {
				ret.add(item);
			}
		}
		return ret;
	}
	
	@Test
	public void testLambdaExpressions() {
		final ArrayList<Bike> bikes = new ArrayList<Bike>();
		for (int i = 1; i < 100; i++) {
			bikes.add(new Bike(i, i, i));
		}
		
		// Filter
		ArrayList<Bike>filtered = this.filter(bikes, 
				b -> b.getGear() % 2 == 0);
		assertTrue(filtered.size() == 49); // Evens 2 - 98
		
		// Streams look promising. 
		List<Bike>filtered2 = bikes.stream()
				.filter(b -> {
					// manually making this a multiple statement block
					// to prove we can use generic blocks as functions.
					int gear = b.getGear();
					return gear % 2 == 0;	
				})
				.collect(Collectors.toList());
		assertTrue(filtered2.size() == 49);
		
		// Map
		int[] gearMap = bikes.stream()
				.mapToInt(bike -> bike.getGear())
				.toArray();
				
		assertTrue(gearMap[gearMap.length - 1] == 99);
	}
}
