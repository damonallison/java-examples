package com.damonallison.classes.generics;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GenericExercises {

    /**
     * Generics exercises:
     * https://docs.oracle.com/javase/tutorial/java/generics/QandE/generics-questions.html
     *
     * Write a generic method to count the number of elements in a collection
     * that have a specific property (for example, odd integers, prime numbers,
     * palindromes).
     *
     * Answer:
     * We declare a generic type T to be a generic type. We accept a collection of that
     * type, with a Predicate to test each element in the collection, returning a count
     * of all elements within the collection that pass the predicate.
     */
    private static <T> Long filter(Collection<T> elements, Predicate<T> pred) {
        return elements.stream().filter(pred).count();
    }

    @Test
    void testGenericsExercises1() {
        List<Integer> nums = Lists.newArrayList(1, 2, 3);
        assertEquals(Long.valueOf(1), filter(nums, num -> num % 2 == 0));
    }

    /**
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
        exchange(ints, 0, 2);
        assertArrayEquals(new Integer[]{3, 2, 1}, ints);
    }


    /**
     * Exercise 4:
     *
     * If the compiler erases all type parameters at compile time, why use generics?
     *
     * * Type safety. The compiler will enforce type safety and casts as necessary,
     *   freeing you from doing it.
     */

    /**
     * Exercise 8 :
     *
     * Write a generic method to find the maximal element in the range [begin, end] of a list.
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
                findMax(Lists.newArrayList(4, 2, 3, 1), 1, 4));
    }
}
