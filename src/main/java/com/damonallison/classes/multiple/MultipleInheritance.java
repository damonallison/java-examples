package com.damonallison.classes.multiple;

/**
 * This class inherits two interfaces, both with the same default method.
 * <p>
 * Which default method will be used? Neither - you get a compile error.
 * If you implement multiple interfaces with the same default method, you must
 * override the method to guarantee the compiler knows which method to invoke.
 */
public class MultipleInheritance implements Interface1, Interface2 {

    /**
     * Both {@link Interface1} and {@link Interface2} implement {@code getValue()}.
     * <p>
     * In order to prevent compile errors, you must override the default
     * implementations.
     *
     * You can invoke a supertype's implementation by invoking it explicitly with
     * Type.super.method()
     */
    @Override
    public int getValue() {
        // shows an example of calling into interface1's implementation.
        return Interface1.super.getValue();
    }
    public int getValueFromi2() {
        return Interface2.super.getValue();
    }
}
