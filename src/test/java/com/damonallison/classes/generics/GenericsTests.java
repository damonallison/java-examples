package com.damonallison.classes.generics;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

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

    /**
     * If you are dealing with legacy code that works with non-generic
     * types (like java's pre-1.5 collections), you may need to add
     * "unchecked" and/or rawtypes to the list of {@link SuppressWarnings}.
     */
    @Test
    void testUnchecked() {

        ArrayList c = new ArrayList();
        c.add("damon");
        assertEquals(1, c.size());

    }

    /**
     * Inheritance
     * <p>
     * <p>
     * It's important to note that generic types with different type parameters are
     * *not* related, even if their parameters are related.
     *
     * <pre>
     * For example: {@code WildcardFunctions<Number> and WildcardFunctions<Integer> are not related.
     *
     * You cannot pass a WildcardFunctions<Integer> value into a method declared :
     *
     * void printBox(WildcardFunctions<Number> num);
     *
     * Even tho Integer extends Number. The common parent of WildcardFunctions<Number>
     *     and WildcardFunctions<Integer> is Object, not WildcardFunctions<Number>
     * </pre>
     * <p>
     * You can subtype a generic class by extending it. {@link SuperPair }
     * extends {@link Pair} to illustrate this point. As long as you don't vary
     * the type argument, the subtyping relationship is preserved between the
     * types.
     */
    @Test
    void testGenericInheritance() {
        List<Pair<String, String>> pairs = new ArrayList<>();
        pairs.add(new SuperPair<>("damon", "allison"));
        pairs.add(new Pair<>("damon", "allison"));

        assertEquals("SuperPair[damon,allison]", pairs.get(0).toString());
        assertEquals("key = damon value = allison", pairs.get(1).toString());

        //
        // Note: You *cannot* use generics with instanceof due to type erasure.
        //       Type erasure erases generic parameters and you are left with the
        //       rawtype.
        //
        // assertTrue(sp instanceof SuperPair<String, String>)
        //
        // You *can* use the underlying
        assertTrue(pairs.get(0) instanceof SuperPair);

        assertTrue(pairs instanceof ArrayList);
        assertEquals("ArrayList", getName(pairs));
        assertEquals("ArrayList", getName(new ArrayList<SuperPair<String, String>>()));

    }

    /**
     * Wildcards allow you to pass any type with an upper bound (extends) or lower bound (super).
     *
     * Upper bound (extends): Accepts the given type or a subtype.
     * Lower bound (super): Accepts the given type or super type.
     * @param list Any {@code List<Pair<String, String>>} list.
     * @return the name of the type. Notice the type parameters are *not* included.
     */
    private static String getName(List<? extends Pair<? extends String, ? extends String>> list) {
        return list.getClass().getSimpleName();
    }
}