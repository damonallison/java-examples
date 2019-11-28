package com.damonallison.classes;

/**
 * Abstract classes allow you to define a base implementation for like classes to derive from.
 * Abstract classes are similar to interfaces in that they cannot be created.
 * Abstract classes are different from interfaces in that they hold state and define methods.
 */
public abstract class AbstractBike {

    private int gear;

    public AbstractBike(int gear) {
        this.gear = gear;
    }

    public int getGear() {
        return gear;
    }

}
