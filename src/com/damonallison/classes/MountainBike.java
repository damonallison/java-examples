package com.damonallison.classes;

public class MountainBike extends Bike {

	private int maxElevation;

	private MountainBike(int speed, int gear, int wheelCount, int maxElevation) {
		super(speed, gear, wheelCount);
		this.maxElevation = maxElevation;
		validateState();
	}

	private void validateState() throws IllegalArgumentException {
		if (maxElevation < 0) {
			throw new IllegalArgumentException("maxElevation must be >= 0");
		}
	}

	public int getMaxElevation() {
		return maxElevation;
	}

	public static class MountainBikeBuilder extends BikeBuilder {
		private int maxElevation;

		private MountainBikeBuilder() {

		}

		public static MountainBikeBuilder newBuilder() {
			return new MountainBikeBuilder();
		}

		public MountainBikeBuilder setMaxElevation(int maxElevation) {
			this.maxElevation = maxElevation;
			return this;
		}

		public MountainBike build() {
			return new MountainBike(this.speed, //
					this.gear, //
					this.wheelCount, //
					this.maxElevation);
		}
	}
}
