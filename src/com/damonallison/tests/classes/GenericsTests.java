package com.damonallison.tests.classes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.damonallison.classes.Pair;
import com.damonallison.classes.SuperPair;

/**
 * Examples of defining and using generic type parameters.
 *
 * Type erasure is the process the Java compiler performs when producing
 * bytecode when dealing with generic types and generic type parameters.
 *
 * The compiler essentially "erases" the type parameters and provides
 * appropriate casts as necessary to produce type safety. Type erasure has the
 * following characteristics:
 *
 * * Replaces generic type parameters with their bounds or "Object" for
 * unbounded types. The resulting bytecode contains only ordinary classes,
 * interfaces, and methods. Generics are *not* incuded in the resulting
 * bytecode!
 *
 * * Inserts type casts as necessary to preserve type safety.
 *
 * * Ensures that no new classes are created for parameterized types. Generics
 * incur no runtime overhead.
 *
 *
 */
public class GenericsTests {

	/**
	 * An example of a generic method.
	 *
	 * A generic method includes a type parameter, inside angle brackets, before
	 * the return type.
	 *
	 * K is a bounded type parameter. The value used for K must implement or
	 * extend the given type(s). Bounded type parameters allow you to invoke
	 * methods on the type which are "in bounds" - meaning declared on the given
	 * type or one of it's supertypes.
	 *
	 * In this example, K is bounded to "Number" and "Serializable". This is
	 * redundant since Number already implements Serializable. However, this
	 * shows you how to require a type parameter to conform to multiple types
	 * (or multiple bounds).
	 *
	 * Note if you use multiple bounds, the class must be bounded first:
	 * {@code <K extends Class & Interface & Interface & Interface}
	 */
	private <K extends Number & Comparable<K> & Serializable, V> boolean compare(
			Pair<K, V> pair1, Pair<K, V> pair2) {
		return pair1.getKey().compareTo(pair2.getKey()) == 0
				&& pair1.getValue().equals(pair2.getValue());
	}

	@Test
	public void testGenericMethods() {
		Pair<Integer, String> p1 = new Pair<>(1, "a");
		Pair<Integer, String> p2 = new Pair<>(1, "a");

		// If the compiler can infer the types, which it typically can, you can
		// omit the types in the method call.
		assertTrue(this.compare(p1, p2));

		// If the compiler *cannot* infer the types, you can help the compiler
		// by explicitly providing the types.
		assertTrue(this.<Integer, String> compare(p1, p2));
	}

	// If you are dealing with legacy code that works with non-generic
	// types (like java's pre-1.5 collections), you may need to add
	// "unchecked" and/or rawtypes to the list of {@link SuppressWarnings}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Test
	public void testUnchecked() {

		ArrayList c = new ArrayList();
		c.add("damon");
		assertTrue(c.size() == 1);

	}

	private String printPair(Pair<String, String> p1) {
		return String.format("Key = %s Value = %s", p1.getKey(), p1.getValue());
	}

	/**
	 *
	 * Inheritance:
	 *
	 *
	 * It's important to note that generic types with different parameters are
	 * *not* related, even if their parameters are related.
	 *
	 * <pre>
	 * For example: {@code Box<Number> and Box<Integer> are not related. You cannot pass a Box<Integer> value into a method declared :
	 * 
	 * public void printBox(Box<Number> num);
	 * 
	 * Even tho Integer extends Number. The common parent of Box<Number> and Box<Integer> is Object.
	 * </pre>
	 *
	 * You can subtype a generic class by extending it. {@link SuperPair}
	 * extends {@link Pair} to illustrate this point. As long as you don't vary
	 * the type argument, the subtyping relationship is preserved between the
	 * types.
	 */
	@Test
	public void testGenericInheritance() {
		SuperPair<String, String> sp = new SuperPair<>("damon", "allison");
		assertEquals("SuperPair[damon,allison]", sp.toString());
		assertEquals("Key = damon Value = allison", printPair(sp));

	}

	/**
	 * The following method takes a parameter (lst) which has an unbounded
	 * wildcard.
	 *
	 * An unbounded wildcard. Useful if you don't need to know or use the type
	 * parameter.
	 *
	 * A wildcard is preferred over List<Object>. If you used List<Object>, you
	 * couldn't print a list of List<Integer> since List<Integer> is not a
	 * subtype of List<Object>
	 *
	 */
	private void printListWithWildcard(List<?> lst) {

	}

	/**
	 * This will *not* print a list of List<Integer> since List<Integer> is not
	 * a subtype of List<Object>.
	 */
	private void printListWithoutWildcard(List<Object> lst) {

	}

	/**
	 * Upper bound wildcards specify that the type parameter must be equal to or
	 * lower in the type hierarchy. (Object being the top of the hierarchy).
	 */
	private void printListWithUpperBoundedWildcard(
			List<? extends Serializable> lst) {

	}

	/**
	 * Lower bound wildcards specify that the type parameter must be equal to or
	 * higher in the type hierarchy. (Object being the top of the hierarchy).
	 *
	 * In this example, the type parameter must be Integer, Number, or Object.
	 */
	private void printListWithLowerBoundedWildcard(List<? super Integer> lst) {

	}

	/**
	 * Wildcards represent an unknown type. Wildcards can be upper or lower
	 * bounded.
	 *
	 * When to use upper and lower bounded variables? * Define "in" variables
	 * with upper bounded wildcards (extends). This forces data coming into the
	 * application to be at least of a certain type. * Define "out" variables
	 * with a lower bounded wildcards (super). This forces data going out from
	 * the application to be at least of a certain type. * If you don't need to
	 * know the exact type (or only need methods defined on {@link Object}, use
	 * an unbounded wildcard. * If your code needs to access the variable as
	 * both "in" and "out" fashions, don't use a wilcard!
	 *
	 * * Don't use a wildcard for a method's return type! It forces the caller
	 * to deal with wildcards!
	 *
	 * Wildcards are important when dealing with generic types. Because
	 * List<Object> is not a supertype of List<Integer>, List<?> creates a
	 * common parent for both of these types in the type hierarchy.
	 *
	 * List<?> List<Object> List<Integer>
	 */
	public void testWildcards() {

		// Wildcards allow any type.
		this.printListWithWildcard(new ArrayList<List<Integer>>()); // OK

		// You can't use "Object" in place of a wildcard when dealing with
		// generic type parameters.
		// List<Integer> is not a subtype of List<Object>, for example:
		this.printListWithoutWildcard(new ArrayList<Object>());
		// this.printListWithoutWildcard(new ArrayList<Integer>()); // Fails :
		// ArrayList<Integer> is not a subtype of List<Object>!

		this.printListWithUpperBoundedWildcard(new ArrayList<String>());

		this.printListWithLowerBoundedWildcard(new ArrayList<Number>());

		List<? extends Integer> intList = new ArrayList<>();
		List<? extends Number> numList = intList; // Allowed since <? extends
		// Number> is a supertype of
		// <? extends Integer>

	}
}