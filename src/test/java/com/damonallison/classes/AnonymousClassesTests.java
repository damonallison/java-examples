package com.damonallison.classes;

import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnonymousClassesTests {
    /**
     * Anonymous classes allow you to create an unnamed class instance inline.
     * <p>
     * Anonymous classes are typically used when only one or two methods need to
     * be overridden. If multiple methods need to be overridden, an actual new,
     * named type is preferred.
     */
    @Test
    public void testAnonymousClasses() throws Exception {

        /**
         * Anonymous classes can capture any final or "effectively final"
         * variables.
         */
        final int x = 10;

        /**
         * Callable is a generic interface that exposes a single method, "call".
         * Callable is a functional interface and to represent a function that
         * returns a single value.
         *
         * Anonymous classes have the following syntax. They are used like
         * constructor arguments. The anonymous class will implement the
         * declared interface or override the declared class. If you are
         * implementing an interface, a no-arg constructor is used. If you are
         * implementing a class, you must use a constructor overload for the
         * class you are overriding.
         */
        Callable<Boolean> callable = new Callable<Boolean>() {

            // Note there is no way for us to expose "name" here.
            private String name = "damon";

            /**
             * Anonymous classes *could* create new public interface elements,
             * but since runner is implementing a particular interface or type
             * (e.g., Callable), the caller won't have those new methods on the
             * interface. Avoid putting new methods on an anonymous class.
             */
            public boolean myNewFunction() {
                return true;
            }

            // NOTE : you cannot declare constructors in an anonymous class.
            @Override
            public Boolean call() {
                myNewFunction();
                name = "executed " + name;
                return x >= 10;
            }
        };
        assertTrue(callable.call());
    }

}
