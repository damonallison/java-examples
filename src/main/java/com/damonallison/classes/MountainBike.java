package com.damonallison.classes;

public class MountainBike extends Bike {

    private int maxElevation;

    /**
     * Constructors are <i>not</i> inherited from their superclass.
     *
     * @param speed The speed the bike is currently traveling
     * @param gear The gear the bike is in
     * @param wheelCount The number of wheels the bike has
     * @param maxElevation The recommended max elevation the MountainBike should travel in
     */
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

    @Override
    public boolean isInHighGear() {
        return this.getGear() > 20;
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

        @Override
        public MountainBike build() {
            return new MountainBike(this.speed, //
                    this.gear, //
                    this.wheelCount, //
                    this.maxElevation);
        }
    }
}
