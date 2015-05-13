package com.damonallison.classes;

/**
 * The bike class shows various class features that are a bit more advanced.
 * 
 * - Initializer Blocks - Inner classes / Builder pattern / fluid interface -
 * Finalizers
 * 
 * @author dallison
 *
 */
public class Bike {

	private int speed;
	private int gear;
	private int wheelCount;

	// Variables can be initialized to the results of a method call.
	private static final boolean CREATED = initializeClassVariable();

	protected static final boolean initializeClassVariable() {
		return true;
	}

	/**
	 * Static initializer block.
	 * 
	 * Executed once, before any instance is created.
	 */
	static {
		System.out.println("static initializer block");
	}

	/**
	 * Initializer blocks.
	 * 
	 * Initializer blocks are injected and executed before every constructor
	 * invocation.
	 * 
	 * A better pattern is to have a designated initializer and make all other
	 * constructors invoke it.
	 */
	{
		System.out.println("initializer block");
		speed = 0;
		gear = 0;
		wheelCount = 0;
	}

	/**
	 * Notice the constructor is private - all building must be done by
	 * BikeBuilder.
	 */
	protected Bike(int speed, int gear, int wheelCount) {

		assert (CREATED);

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

	public static class BikeBuilder {
		protected int speed;
		protected int gear;
		protected int wheelCount;

		/**
		 * Any required fields could be passed to the constructor to prevent an
		 * invalid object from being initialized.
		 */
		public BikeBuilder() {

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
	 *
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
	 * Therefore, you cannot delcare two methods with the same signature varying
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
}
