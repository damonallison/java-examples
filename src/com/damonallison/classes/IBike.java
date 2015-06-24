package com.damonallison.classes;

/**
 * Interfaces can be public or package accessible. Package is the default.
 * 
 * @author Damon Allison
 */
public interface IBike {

	/**
	 * Enums can be added to interfaces. If you really want to..
	 * 
	 * @author Damon Allison
	 */
	enum BikeSize {
		CHILD, //
		FEMALE, //
		MALE
	}

	/**
	 * Interfaces can contain static methods.
	 * 
	 * A random static method to show that you can define static methods on an
	 * interface. If you really want to.
	 */
	static int add(int x, int y) {
		return x + y;
	}

	/**
	 * Interfaces can contain static constants.
	 * 
	 * Interface constants are implicitly public, static, and final. These
	 * modifiers don't need to be specified. They are specified here to be
	 * explicit.
	 */
	static final int DEFAULT_WHEELS = 2;

	int getSpeed();

	int getGear();

	int getWheelCount();

	/**
	 * Interfaces can contain default methods.
	 * 
	 * default methods allow you to add members to an existing interface without
	 * breaking compatibility with clients who depend on the old interface.
	 * 
	 * Nice hack - default methods allow you to add functional interfaces on top
	 * of existing interfaces without having to extend them.
	 */
	default boolean isInHighGear() {
		return getGear() > 10;
	}
}
