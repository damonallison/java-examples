package com.damonallison.language;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;

import com.damonallison.classes.Bike;
import com.damonallison.classes.Bike.BikeBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Method references allow you to simplify lambda expressions that do nothing but call
 * an existing method.
 * 
 * @author Damon Allison
 */
public class MethodReferenceTests {

	@FunctionalInterface
	interface Supplier<T> {
		T get();
	}

	@Test
	public void methodReferenceTest() {
		
		class BikeSpeedComparator implements Comparator<Bike> {
			public int compare(Bike b, Bike b2) {
				return Integer.compare(b.getSpeed(), b2.getSpeed());
			}
		}
		
		Bike[] bikes = new Bike[100];
		for (int i = 99; i >= 0; i--) {
			bikes[i] = BikeBuilder.newBuilder()
					.setGear(i + 1)
					.setSpeed(i + 1)
					.setWheelCount(2).build();
		}
		
		// Sort without method reference
		Arrays.sort(bikes, new BikeSpeedComparator());
		
		// Sort with lambda expression
		Arrays.sort(bikes, (Bike b, Bike b2) -> {
			return Integer.compare(b.getSpeed(), b2.getSpeed());
		});
		
		// Here, we create a "helper class" to show how we can use method references
		// to reference it's methods.
		class BikeCompareHelper {
			public int compareByWheelCount(Bike b, Bike b2) {
				return Integer.compare(b.getWheelCount(), b2.getWheelCount());
			}
			public HashSet<Bike> copyToHashSet(HashSet<Bike> bikes, Supplier<HashSet<Bike>>bikeFactory) {
				HashSet<Bike> newBikes = bikeFactory.get();
				for (Bike b : bikes) {
					newBikes.add(b);
				}
				return newBikes;
			}
		}
		
		BikeCompareHelper helper = new BikeCompareHelper();
		
		// With lambda expression. Here, notice the lambda expression does nothing
		// but call a single method. This is what method references simplify.
		Arrays.sort(bikes, (b, b2) -> helper.compareByWheelCount(b, b2));
		
		// With method reference.
		// This is equivalent to:
		// (b, b2) -> helper.compareByWheelCount(b, b2)
		//
		// At compile (or runtime), two things happen:
		// 1. The parameters are inferred from the compareByWheelCount method. 
		// 2. The body of the lambda calls compareByWheelCount.
		//
		// General usage : if you have a lambda expression that does nothing
		// but invoke a single method, use a method reference.
		// 
		Arrays.sort(bikes, helper::compareByWheelCount);

		HashSet<Bike> bikeSet = new HashSet<Bike>(Arrays.asList(bikes));
		
		// Here, the type expected to "copyToHash" is a Supplier<HashSet<Bike>>, which 
		// is a functional interface. We could have used the following lambda expression
		// HashSet<Bike> bikeSetCopy = helper.copyToHashSet(bikeSet, 
		//  () -> { return new HashSet<Bike>(); });
		// 
		// Instead, we use the method reference to reference the HashSet constructor.
		// The compiler infers the type of HashSet<Bike> from the definition of copyToHash.
		//
		// We could have also specified the type:
		// 		HashSet<Bike> bikeSetCopy = helper.copyToHashSet(bikeSet, HashSet<Bike>::new);

		HashSet<Bike> bikeSetCopy = helper.copyToHashSet(bikeSet, HashSet::new);
		
		assertEquals(bikeSet, bikeSetCopy);
	}
}
