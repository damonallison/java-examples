package com.damonallison.classes.generics;

import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WildcardFunctionsTests {

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

        //
        // Unbounded wildcards allow any type.
        //
        WildcardFunctions.printListWithWildcard(new ArrayList<List<Integer>>());

        //
        // You can't use "Object" in place of a wildcard when dealing with
        // generic type parameters.
        //
        // List<Integer> is not a subtype of List<Object>, for example. You *must*
        // use List<Object>
        //
        // For example:
        //
        // this.printListWithoutWildcard(new ArrayList<Integer>()); // Fails :
        // ArrayList<Integer> is not a subtype of List<Object>!
        //
        WildcardFunctions.printListWithoutWildcard(new ArrayList<Object>());

        //
        // Upper bound allows you to use the bounded type (i.e., Serializable)
        // or any type that inherits from the bounded type. (i.e., String)
        //
        WildcardFunctions.printListWithUpperBoundedWildcard(new ArrayList<String>());

        //
        // Lowe bound allows you to use the bounded type (i.e., Integer) or any
        // super type.
        //
        WildcardFunctions.printListWithLowerBoundedWildcard(new ArrayList<Number>());


        List<? extends Integer> intList = new ArrayList<>();
        List<? extends Number> numList = intList; // Allowed since Number is a super type of Integer.
        assertEquals(numList, intList);

        //
        // Why can't we simply use List<Number>?
        //-
        // List<Number> is not a supertype of List<Integer>, therefore
        // the following assignment will fail.
        //
        // List<Integer> intList2 = new ArrayList<>();
        // List<Number> numList2 = intList2;
        //
    }

    @Test
    public void upperBoundWildcardTest() {
        assertEquals(6.0, WildcardFunctions.sum(List.of(1, 2, 3)));
    }


}
