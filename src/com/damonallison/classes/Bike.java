package com.damonallison.classes;

/**
 * The bike class shows various class features that are a bit more advanced.
 * 
 * <ul>
 * <li>
 * Initializer blocks : static and instance initializer blocks are executed
 * prior to the first usage of a class (static initializer blocks) or an
 * instance of the class (instance initializer blocks)
 * <li>
 * Nested classes (static and inner classes).
 *
 * IMPORTANT : Serialization of inner and nested classes is *strongly* discouraged.
 * Inner classes are compiled into "synthetic constructs" that differ between JVM
 * implementations. If you serialize an inner class, it may not be compatible with another JVM implementation.
 * <li>
 * Builder pattern
 * <li>
 * Fluid Interface : A fluid interface returns the current object instance
 * itself which allows you to chain multiple calls together.
 * 
 * <code>
 *   Bike.Builder b = new Bike.BikeBuilder();
 *   b.setSpeed(10)
 *    .setWheelCount(2)
 *    .setGear(100);
 * </code>
 * <li>
 * Finalizers
 * 
 * @author Damon Allison
 */
public class Bike implements IBike {


	private int speed;
	private int gear;
	private int wheelCount;

	// Variables can be initialized to the results of a method call.
	// These are set before the static initializer block (for static
	// variables) or constructor (for instance variables).
	public static final boolean CLASS_CREATED = initializeClassVariable();
	public static final boolean STATIC_INITIALIZER_INVOKED;

	public final boolean INSTANCE_CREATED = initializeInstanceVariable();
	public final boolean INITIALIZER_INVOKED;

	private static boolean initializeClassVariable() {
		return true;
	}

	private boolean initializeInstanceVariable() {
		return true;
	}

	public static boolean staticInitializerInvoked() {
		return STATIC_INITIALIZER_INVOKED;
	}
	public boolean initializerInvoked() {
		return INITIALIZER_INVOKED;
	}

	/**
	 * Static initializer block
	 * 
	 * Static initializer blocks are executed before any instance is created. If
	 * there are multiple static initializer blocks, they are executed in the
	 * order they appear in the source code.
	 */
	static {
		STATIC_INITIALIZER_INVOKED = true;
	}

	static {
		// This static initializer will execute second.
	}

	/**
	 * Initializer blocks.
	 * 
	 * Initializer blocks are injected and executed before every constructor
	 * invocation.
	 * 
	 * Like static initializer blocks, they are executed in the order they are
	 * defined in the source.
	 * 
	 * A better pattern is to have a designated initializer and make all other
	 * constructors invoke it.
	 */
	{
//		System.out.println("initializer 1");
		speed = 0;
		gear = 0;
		wheelCount = 0;
		INITIALIZER_INVOKED = true;
	}

	{
//		System.out.println("initializer 2");
	}

	protected Bike(int speed, int gear, int wheelCount) {

		assert (CLASS_CREATED);
		assert (INSTANCE_CREATED);
		this.speed = speed;
		this.gear = gear;
		this.wheelCount = wheelCount;

		validateState();
	}

	private void validateState() throws IllegalArgumentException {
		// Is there no way in java to have an unsigned int?
		if (speed < 0) {
			throw new IllegalArgumentException("speed must be >= 0");
		}
		if (gear < 1) {
			throw new IllegalArgumentException("gear must be >= 1");
		}
		if (wheelCount < 0) {
			throw new IllegalArgumentException("wheelCount must be >= 0");
		}
	}

	protected void finalize() {
		System.out.println("finalize");
	}

	/**
	 * Inner class example. This class is exposed publicly and can be used
	 * (instantiated) outside of the bike class, but only from an instance of
	 * the Bike class.
	 * 
	 * This feels like a hack and should probably be avoided.
	 * 
	 * Bike b = new Bike(); Mechanic m = b.new.Mechanic();
	 * 
	 * Nested classes have access to members of it's enclosing class. To refer
	 * to a member of the enclosing class, use EnclosingClass.this.*
	 * 
	 * For example, to access Bike's members, we use Bike.this.* You could
	 * access the variable as "speed", but it's clearer to the reader what scope
	 * the variable is in if you use Bike.this.*
	 */
	public class Mechanic {
		public String name;

		@Override
		public String toString() {
			return "Mechanic " + this.name + " for bike with speed "
					+ Bike.this.speed;
		}

	}

	public int getSpeed() {
		return this.speed;
	}

	public int getGear() {
		return this.gear;
	}

	public int getWheelCount() {
		return this.wheelCount;
	}

	/**
	 * Method overload does *not* consider return type when overloading methods.
	 * Therefore, you cannot decl two methods with the same signature varying
	 * only in return type.
	 * 
	 * Overloaded methods should be used sparingly, they can make the code much
	 * less readable.
	 * 
	 * @param amount
	 */
	public void deltaSpeed(long amount) {
		speed += amount;
	}

	public void deltaSpeed(int amount) {
		speed += amount;
	}

	/**
	 * BikeBuilder is declared as a nested static class.
	 * 
	 * Why nested? Because it's a helper class for {@code Bike}
	 * Why static? So we do not need an instance of {@code Bike} to use it.
	 *
	 * A static nested class behaves as any other top level class. The only
	 * difference is that it is nested within another class for packaging
	 * purposes.
	 */
	public static class BikeBuilder {
		protected int speed;
		protected int gear;
		protected int wheelCount;

		protected BikeBuilder() {
		}

		public static BikeBuilder newBuilder() {
			return new BikeBuilder();
		}

		public BikeBuilder setSpeed(int speed) {
			this.speed = speed;
			return this;
		}

		public BikeBuilder setGear(int gear) {
			this.gear = gear;
			return this;
		}

		public BikeBuilder setWheelCount(int wheelCount) {
			this.wheelCount = wheelCount;
			return this;
		}

		public Bike build() {
			return new Bike(this.speed, this.gear, this.wheelCount);
		}
	}

}
