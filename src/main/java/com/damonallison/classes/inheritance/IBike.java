package com.damonallison.classes.inheritance;

/**
 * Interfaces are reference types, similar to a class, that can only contain
 *
 * <p>
 * Interfaces can contain:
 * <ul>
 * <li>Constants
 * <li>Method signatures (a.k.a, "abstract" methods)
 * <li>Default methods
 * <li>Static methods
 * <li>Nested types
 * <p>
 *
 * Interfaces can be public or package accessible. Package is the default.
 *
 * All methods on an interface are implicitly public, so you can omit the modifier.
 * All constants on an interface are implicitly public, so you can omit the modifiers.
 *
 * Default and static methods can be added to an interface without breaking backwards compatibility.
 */
public interface IBike extends Comparable<IBike> {

    /**
     * Interfaces can contain nested types. If you really want to.
     * <p>
     * Nested types do *not* have access to the parent type. It's a
     * "static nested class". You can consider it a typical class
     * that just happens to be nested within this interface for
     * encapsulation purposes only.
     */
    class Pedal {
        private int length = 0;

        public void setLength(int length) {
            this.length = length;
        }

        public int getLength() {
            return length;
        }
    }

    /**
     * Enums can be added to interfaces. If you really want to.
     */
    enum BikeSize {
        CHILD,
        FEMALE,
        MALE
    }

    /**
     * Interfaces can contain static methods. If you really want to.
     */
    static int pelotonSpeed(IBike bike1, IBike bike2) {
        return (bike1.getSpeed() + bike2.getSpeed()) / 2;
    }

    /**
     * Interfaces can contain static constants.
     * <p>
     * Interface constants are implicitly public, static, and final. These
     * modifiers don't need to be specified.
     * <p>
     * They <em>could</em> be specified here as {@code private static final int},
     * but since IntelliJ is smart and will clean it up, they are omitted.
     */
    int DEFAULT_WHEELS = 2;

    int getSpeed();

    int getGear();

    int getWheelCount();

    /**
     * Interfaces can contain default methods.
     * <p>
     * default methods allow you to add members to an existing interface without
     * breaking compatibility with clients who depend on the old interface.
     * <p>
     * Nice hack - default methods allow you to add functional interfaces on top
     * of existing interfaces without having to extend them.
     * <p>
     * Another way to add methods to an interface would be to create a new interface
     * which extends this interface. This is not as clean as simply adding a default
     * method since it adds yet another type to the type system.
     */
    default boolean isInHighGear() {
        return getGear() > 10;
    }
}
