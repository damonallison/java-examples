package com.damonallison.classes;

public class MountainBike extends Bike {

	private int maxElevation;
	
	public MountainBike(int speed, int gear, int wheelCount, int maxElevation) {
		super(speed, gear, wheelCount);
		this.maxElevation = maxElevation;
	}
	
	public int getMaxElevation() {
		return maxElevation;
	}
}
