package com.damonallison.classes;

import java.util.function.Function;

public class DataStructure {

	// Create an array
	public final static int SIZE = 15;
	private int[] arrayOfInts = new int[SIZE];

	public DataStructure() {
		// fill the array with ascending integer values
		for (int i = 0; i < SIZE; i++) {
			arrayOfInts[i] = i;
		}
	}

	public int[] getArrayOfInts() {
		return arrayOfInts;
	}

	public void printEven() {

		// Print out values of even indices of the array
		DataStructureIterator iterator = new EvenIterator();
		while (iterator.hasNext()) {
			System.out.print(iterator.next() + " ");
		}
		System.out.println();
	}
	public static boolean isEvenIndex(int x) {
		return x % 2 == 0;
	}

	public static boolean isOddIndex(int x) {
		return !isEvenIndex(x);
	}

	public interface DataStructureIterator extends java.util.Iterator<Integer> { }

	// Inner class implements the DataStructureIterator interface,
	// which extends the Iterator<Integer> interface

	public class EvenIterator implements DataStructureIterator {

		// Start stepping through the array from the beginning
		private int nextIndex = 0;

		@Override
		public boolean hasNext() {

			// Check if the current element is the last in the array
			return (nextIndex <= SIZE - 1);
		}

		@Override
		public Integer next() {

			// Record a value of an even index of the array
			Integer retValue = Integer.valueOf(arrayOfInts[nextIndex]);

			// Get the next even element
			nextIndex += 2;
			return retValue;
		}
	}

	// Define a method named print(DataStructureIterator iterator). Invoke this method
	// with an instance of the class EvenIterator so that it performs the same function
	// as {@code printEven}
	public void print(DataStructureIterator iterator) {
		while (iterator.hasNext()) {
			System.out.print(iterator.next() + " ");
		}
		System.out.println();
	}

	// Define a method named print(java.util.Function<Integer, Boolean> iterator) that performs
	// the same function as print(DataStructureIterator iterator).
	// Invoke this method with a lambda expression to print elements that have an even index value.
	// Invoke this method again with a lambda expression to print elements that have an odd index value.

	public void print(Function<Integer, Boolean> iterator) {
		for (int i = 0; i < SIZE; i++) {
			if (iterator.apply(i)) {
				System.out.print(i + " ");
			}
		}
		System.out.println();
	}
}