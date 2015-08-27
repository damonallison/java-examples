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
 * <li>
 * Overriding {@link Object} members.
 *
 * Important note : methods called from constructors should be marked as {code final}. If they are not,
 * a derived class can override the method creating unpredictable results. In this example, {@link MountainBike}
 * subclasses {@link Bike}. Bike's constructor calls {@code validateState}. If validateState was not private and
 * was overridden, {@link MountainBike} could provide an implementation that would allow us to create invalid
 * Bike objects. Therefore, {@code validateState} is private.
 * </ul>
 */
public class Bike extends AbstractBike implements IBike, Cloneable {

	private int speed;
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
		wheelCount = 0;
		INITIALIZER_INVOKED = true;
	}

	{
		//		System.out.println("initializer 2");
	}

	protected Bike(int speed, int gear, int wheelCount) {
		super(gear);
		assert (CLASS_CREATED);
		assert (INSTANCE_CREATED);
		this.speed = speed;
		this.wheelCount = wheelCount;
		validateState();
	}

	private void validateState() throws IllegalArgumentException {
		// Is there no way in java to have an unsigned int?
		if (this.getSpeed() < 0) {
			throw new IllegalArgumentException("speed must be >= 0");
		}
		if (this.getGear() < 1) {
			throw new IllegalArgumentException("gear must be >= 1");
		}
		if (this.getWheelCount() < 0) {
			throw new IllegalArgumentException("wheelCount must be >= 0");
		}
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

	@Override
	public int getSpeed() {
		return this.speed;
	}

	@Override
	public int getWheelCount() {
		return this.wheelCount;
	}

	@Override
	public boolean isInHighGear() {
		return this.getGear() > 5;
	}

	/**
	 * Method overload does *not* consider return type when overloading methods.
	 * Therefore, you cannot decl two methods with the same signature varying
	 * only in return type.
	 *
	 * Overloaded methods should be used sparingly, they can make the code much
	 * less readable.
	 *
	 * @param amount The amount to change the speed.
	 */
	public void deltaSpeed(long amount) {
		speed += amount;
	}

	public void deltaSpeed(int amount) {
		speed += amount;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (INITIALIZER_INVOKED ? 1231 : 1237);
		result = prime * result + (INSTANCE_CREATED ? 1231 : 1237);
		result = prime * result + this.getGear();
		result = prime * result + this.getSpeed();
		result = prime * result + this.getWheelCount();
		return result;
	}


	/**
	 * WARNING : when a class extends a concrete class that implements equals
	 * and adds a significant field, a correct implementation of equals cannot
	 * be constructed. The only alternative is to use composition rather than
	 * inheritance.
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Bike)) {
			return false;
		}
		Bike other = (Bike) obj;
		if (this.getGear() != other.getGear()) {
			return false;
		}
		if (this.getSpeed() != other.getSpeed()) {
			return false;
		}
		if (this.getWheelCount() != other.getWheelCount()) {
			return false;
		}
		return true;
	}

	/**
	 * Creates a deep copy of this object.
	 */
	@Override
	public Object clone() {
		return BikeBuilder.newBuilder()
				.setGear(this.getGear())
				.setSpeed(this.getSpeed())
				.setWheelCount(this.getWheelCount())
				.build();
	}

	@Override public String toString() {
		return "Bike speed=" + this.getSpeed() + " gear=" + this.getGear() + " wheelCount=" + this.getWheelCount();
	}

	/**
	 * Finalize *may* be called by the JVM. If or when it is called is uncertain.
	 * Because of this uncertainty, you should *not* rely on this method to free
	 * resources.
	 */
	@Override
	protected void finalize() {
		System.out.println("finalize");
	}


	/**
	 * WARNING : when a class extends a concrete {@link Comparable} class and
	 * adds a significant field, a correct implementation of {@code compareTo}
	 * cannot be constructed. The only alternative is to use composition
	 * rather than inheritance.
	 *
	 * This implementation compares based on speed only. A true implementation
	 * would compare based on all fields.
	 *
	 * @return int indicating if this object is less than, equal to,
	 *         or greater than the object being compared to.
	 */
	@Override
	public int compareTo(IBike obj) {
		final int BEFORE = -1;
		final int EQUAL = 0;
		final int AFTER = 1;

		if (this.getSpeed() < obj.getSpeed()) {
			return BEFORE;
		}
		if (this.getSpeed() > obj.getSpeed()) {
			return AFTER;
		}
		return EQUAL;
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
