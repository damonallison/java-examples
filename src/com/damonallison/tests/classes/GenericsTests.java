package com.damonallison.tests.classes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.util.ArrayList;

import org.junit.Test;

import com.damonallison.classes.Pair;
import com.damonallison.classes.SuperPair;

public class GenericsTests {


	/**
	 * An example of a generic method.
	 *
	 * A generic method includes a type parameter, inside angle brackets, before the return type.
	 *
	 * K is a bounded type parameter. The value used for K must implement or extend the given type(s).
	 * Bounded type parameters allow you to invoke methods on the type which are "in bounds" - meaning
	 * declared on the given type or one of it's supertypes.
	 *
	 * In this example, K is bounded to "Number" and "Serializable". This is redundant since Number
	 * already implements Serializable. However, this shows you how to require a type parameter to
	 * conform to multiple types (or multiple bounds).
	 *
	 * Note if you use multiple bounds, the class must be bounded first:
	 * {@code <K extends Class & Interface & Interface & Interface}
	 */
	private <K extends Number & Comparable<K> & Serializable, V> boolean compare(Pair<K, V>pair1, Pair<K, V>pair2) {
		return pair1.getKey().compareTo(pair2.getKey()) == 0 &&
				pair1.getValue().equals(pair2.getValue());
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
		assertTrue(this.<Integer, String>compare(p1, p2));
	}

	// If you are dealing with legacy code that works with non-generic
	// types (like java's pre-1.5 collections), you may need to add
	// "unchecked" and/or rawtypes to the list of {@link SuppressWarnings}
	@SuppressWarnings({"unchecked", "rawtypes"})
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
	 * It's important to note that generic types with different parameters are *not* related, even if their parameters are related.
	 * <pre>
	 * For example: {@code Box<Number> and Box<Integer> are not related. You cannot pass a Box<Integer> value into a method declared :
	 *
	 * public void printBox(Box<Number> num);
	 *
	 * Even tho Integer extends Number. The common parent of Box<Number> and Box<Integer> is Object.
	 * </pre>
	 *
	 * You can subtype a generic class by extending it. {@link SuperPair} extends {@link Pair} to illustrate this point.
	 * As long as you don't vary the type argument, the subtyping relationship is preserved between the types.
	 */
	@Test
	public void testGenericInheritance() {
		SuperPair<String, String> sp = new SuperPair<>("damon", "allison");
		assertEquals("SuperPair[damon,allison]", sp.toString());
		assertEquals("Key = Damon Value = Allison", printPair(sp));

	}


	/**
	 * Wildcards represent an unknown type.
	 */
	public void testWildcards() {
	}
}
