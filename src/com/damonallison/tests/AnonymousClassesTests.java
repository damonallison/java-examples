package com.damonallison.tests;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.Callable;

import org.junit.Test;

public class AnonymousClassesTests {

	/**
	 * A simple interface that will be used to illustrate anonymous classes. An
	 * anonymous concrete class will be created that implements this interface.
	 * 
	 * <pre>
	 * IRun runner = new IRun() {
	 *   public bool run() {
	 *     return true;
	 *   }
	 * }
	 * </pre>
	 *
	 */
	private interface IRun {
		boolean run();
	}

	/**
	 * Anonymous classes allow you to create an unnamed class instance.
	 * Anonymous classes are typically used when only one or two methods need to
	 * be overridden. If multiple methods need to be overridden, an actual new,
	 * named type is preferred.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testAnonymousClasses() throws Exception {

		/**
		 * Anonymous classes can capture any final or "effectively final"
		 * variables.
		 */
		final int x = 10;

		IRun runner = new IRun() {

			private String name = "damon";

			/**
			 * Anonymous classes *could* create new public interface elements,
			 * but since runner is implementing a particular interface or type
			 * (e.g., IRun), the caller won't have those new methods on the
			 * interface. Avoid putting new methods on an anonymous class.
			 */
			public boolean myNewFunction() {
				return true;
			}

			// NOTE : you cannot declare constructors in an anonymous class.
			@Override
			public boolean run() {
				myNewFunction();
				System.out.println("running " + name);
				return x > 1;
			}
		};

		assertTrue(runner.run());

		/**
		 * Callable is a generic interface that exposes a single method, "call".
		 * Callable is pretty flexible - it's probably used all over java.
		 */
		final Integer capturedInt = 100;
		Callable<Integer> callable = new Callable<Integer>() {

			@Override
			public Integer call() {
				return capturedInt;
			}
		};
		assertTrue(callable.call() == 100);
	}

}
