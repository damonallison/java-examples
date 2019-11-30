package com.damonallison.classes;

import com.damonallison.classes.generics.Pair;
import com.damonallison.classes.generics.SuperPair;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Examples of defining and using generic type parameters. Generics allow you to
 * specify type constraints on variables or class interfaces. Generics are a
 * compile time type checking feature. When java is compiled, type erasure
 * erases the generic type parameters.
 * <p>
 * Type erasure is the process the Java compiler performs when producing
 * bytecode when dealing with generic types and generic type parameters.
 * <p>
 * The compiler essentially "erases" the type parameters and provides
 * appropriate casts as necessary to produce type safety. Type erasure has the
 * following characteristics:
 * <p>
 * * Replaces generic type parameters with their bounds or "Object" for
 * unbounded types. The resulting bytecode contains only ordinary classes,
 * interfaces, and methods. Generics are *not* included in the resulting
 * bytecode!
 * <p>
 * * Inserts type casts as necessary to preserve type safety.
 * <p>
 * * Ensures that no new classes are created for parameterized types. Generics
 * incur no runtime overhead.
 */
class GenericsTests {

    /**
     * An example of a generic method.
     * <p>
     * A generic method includes a type parameter, inside angle brackets, before
     * the return type.
     * <p>
     * K is a bounded type parameter. The value used for K must implement or
     * extend the given type(s). Bounded type parameters allow you to invoke
     * methods on the type which are "in bounds" - meaning declared on the given
     * type or one of it's supertypes.
     * <p>
     * In this example, K is bounded to "Number" and "Serializable". This is
     * redundant since Number already implements Serializable. However, this
     * shows you how to require a type parameter to conform to multiple types
     * (or multiple bounds).
     * <p>
     * Note if you use multiple bounds, the "class" type must be bounded first:
     * {@code <K extends Class & Interface & Interface & Interface}
     */
    private <K extends Number & Comparable<K> & Serializable, V> boolean compare(
            Pair<K, V> pair1, Pair<K, V> pair2) {
        return pair1.getKey().compareTo(pair2.getKey()) == 0
                && pair1.getValue().equals(pair2.getValue());
    }

    @Test
    void testGenericMethods() {
        Pair<Integer, String> p1 = new Pair<>(1, "a");
        Pair<Integer, String> p2 = new Pair<>(1, "a");

        // If the compiler can infer the types, which it typically can, you can
        // omit the types in the method call.
        assertTrue(this.compare(p1, p2));

        // If the compiler *cannot* infer the types, you can help the compiler
        // by explicitly providing the types.
        assertTrue(this.compare(p1, p2));
    }

    // If you are dealing with legacy code that works with non-generic
    // types (like java's pre-1.5 collections), you may need to add
    // "unchecked" and/or rawtypes to the list of {@link SuppressWarnings}
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Test
    void testUnchecked() {

        ArrayList c = new ArrayList();
        c.add("damon");
        assertEquals(1, c.size());

    }

    private String printPair(Pair<String, String> p1) {
        return String.format("Key = %s Value = %s", p1.getKey(), p1.getValue());
    }

    /**
     * Inheritance:
     * <p>
     * <p>
     * It's important to note that generic types with different parameters are
     * *not* related, even if their parameters are related.
     *
     * <pre>
     * For example: {@code Box<Number> and Box<Integer> are not related. You cannot pass a Box<Integer> value into a method declared :
     *
     * void printBox(Box<Number> num);
     *
     * Even tho Integer extends Number. The common parent of Box<Number> and Box<Integer> is Object.
     * </pre>
     * <p>
     * You can subtype a generic class by extending it. {@link SuperPair }
     * extends {@link Pair} to illustrate this point. As long as you don't vary
     * the type argument, the subtyping relationship is preserved between the
     * types.
     */
    @Test
    void testGenericInheritance() {
        SuperPair<String, String> sp = new SuperPair<>("damon", "allison");
        assertEquals("SuperPair[damon,allison]", sp.toString());
        assertEquals("Key = damon Value = allison", printPair(sp));

    }

    /**
     * The following method takes a parameter (lst) which has an unbounded
     * wildcard.
     * <p>
     * An unbounded wildcard. Useful if you don't need to know or use the type
     * parameter.
     * <p>
     * A wildcard is preferred over List<Object>. If you used List<Object>, you
     * couldn't print a list of List<Integer> since List<Integer> is not a
     * subtype of List<Object>
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
     * <p>
     * In this example, the type parameter must be Integer, Number, or Object.
     */
    private void printListWithLowerBoundedWildcard(List<? super Integer> lst) {

    }

    /**
     * Wildcards represent an unknown type. Wildcards can be upper or lower
     * bounded.
     * <p>
     * When to use upper and lower bounded variables? * Define "in" variables
     * with upper bounded wildcards (extends). This forces data coming into the
     * application to be at least of a certain type. * Define "out" variables
     * with a lower bounded wildcards (super). This forces data going out from
     * the application to be at least of a certain type. * If you don't need to
     * know the exact type (or only need methods defined on {@link Object}, use
     * an unbounded wildcard. * If your code needs to access the variable as
     * both "in" and "out" fashions, don't use a wilcard!
     * <p>
     * * Don't use a wildcard for a method's return type! It forces the caller
     * to deal with wildcards!
     * <p>
     * Wildcards are important when dealing with generic types. Because
     * List<Object> is not a supertype of List<Integer>, List<?> creates a
     * common parent for both of these types in the type hierarchy.
     * <p>
     * List<?> List<Object> List<Integer>
     */
    @Test
    void testWildcards() {

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
        assertEquals(numList, intList);

        /*-
         * List<Number> is not a supertype of List<Integer>, therefore
         * the following assignment will fail.
         */

        // List<Integer> intList2 = new ArrayList<>();
        // List<Number> numList2 = intList2;
    }

    /*-
     * Generics exercises:
     * https://docs.oracle.com/javase/tutorial/java/generics/
     * QandE/generics-questions.html
     *
     * Write a generic method to count the number of elements in a collection
     * that have a specific property (for example, odd integers, prime numbers,
     * palindromes).
     *
     * Answer :
     * We declare a generic type T to be a generic type. We accept a collection of that
     * type, with a Predicate to test each element in the collection, returning a count
     * of all elements within the collection that pass the predicate.
     */
    private static <T> Long filter(Collection<T> elements, Predicate<T> pred) {
        return elements.stream().filter(pred).count();
    }

    @Test
    void testGenericsExercises1() {
        List<Integer> nums = Lists.newArrayList(1, 2);
        assertEquals(Long.valueOf(1),
                GenericsTests.filter(nums, num -> num % 2 == 0));
    }

    /*-
     * Exercise 3:
     *
     * Write a generic method to exchange the positions of two different elements in an array.
     */
    private static <T> void exchange(T[] arr, int pos1, int pos2) {
        T temp = arr[pos2];
        arr[pos2] = arr[pos1];
        arr[pos1] = temp;
    }

    @Test
    void testGenericsExercises3() {

        Integer[] ints = {1, 2, 3};
        GenericsTests.exchange(ints, 0, 2);
        assertArrayEquals(new Integer[]{3, 2, 1}, ints);
    }

    /*-
     * Exercise 4:
     *
     * If the compiler erases all type parameters at compile time, why use generics?
     *
     * * Allows you to have type non-object type information in methods that declare
     *   type parameters with lower or upper bounds.
     *
     * * The compiler will insert type casts as necessary, freeing you from doing it (safer).
     */

    /*-
     * Exercise 8 :
     *
     * Write a generic method to find the maximal element in the range [begin, end) of a list.
     *
     * * The return value is an "out" parameter. Out parameters are defined with a lower bound.
     * * The input parameter lst is an "in" parameter. In parameters are defined with a type parameter having an upper bound.
     *   The upper bound allows us to use methods on upper bound type (Comparable in this case).
     *
     *
     */
    private static <T extends Comparable<? super T>> T findMax(
            List<? extends T> lst, int begin, int end) {
        return lst.subList(begin, end).stream().sorted()
                .collect(Collectors.toList()).get(end - begin - 1);
    }

    @Test
    void testGenericsExercises8() {
        assertEquals(Integer.valueOf(3),
                GenericsTests.findMax(Lists.newArrayList(4, 2, 3, 1), 1, 4));
    }

}