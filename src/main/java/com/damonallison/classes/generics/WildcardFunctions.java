package com.damonallison.classes.generics;

import java.io.Serializable;
import java.util.List;

/**
 * Wildcards allow you to relax type restrictions.
 *
 * Upper bounded wildcards allow you to pass a type or any subtype.
 *
 * Lower bounded wildcards allow you to pass a tyoe or any supertype.
 *
 * Unboundedd wildcards allow you to pass any type. Useful if you don't need
 * to access the actual object at all. (i.e., data structures / collections)
 *
 * @param <T>
 */
public class WildcardFunctions {

    /**
     * An upper bounded wildcard allows you to pass the actual type (Number) or
     * any subtype (Integer, Double).
     *
     * @param list The list to sum
     * @return The total of all elements in {@code list}
     */
    public static double sum(List<? extends Number> list) {
        double total = 0.0;
        for (Number n: list) {
            total += n.doubleValue();
        }
        return total;
    }

    /**
     *
     */
    public static <T> List<T> toList(T elt) {
        return new List.of(elt);
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
    public static void printListWithWildcard(List<?> lst) {

    }

    /**
     * This will *not* print a list of List<Integer> since List<Integer> is not
     * a subtype of List<Object>.
     */
    public static void printListWithoutWildcard(List<Object> lst) {

    }

    /**
     * Upper bound wildcards specify that the type parameter must be equal to or
     * lower in the type hierarchy. (Object being the top of the hierarchy).
     */
    public static void printListWithUpperBoundedWildcard(
            List<? extends Serializable> lst) {

    }

    /**
     * Lower bound wildcards specify that the type parameter must be equal to or
     * higher in the type hierarchy. (Object being the top of the hierarchy).
     * <p>
     * In this example, the type parameter must be Integer, Number, or Object.
     */
    public static void printListWithLowerBoundedWildcard(List<? super Integer> lst) {

    }

}
