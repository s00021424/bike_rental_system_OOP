package com.epicode;

/**
 * Represents a specific type of bike.
 * Extends abstract Bike class and defines getType().
 */
@Secured("Shielding, logging, validation verified")
@RoleType("Bike")
public class MountainBike extends Bike {
    public MountainBike(BikeBuilder builder) {
        super(builder);
    }

    @Override
    public BikeType getType() {
        return BikeType.mountain;
    }

    public static void main(String[] args) {
        BikeBuilder bikeBuilder = new BikeBuilder("235cgd", "GT3", true).setLights(true).setBasket(true);
        MountainBike mountainBike = new MountainBike(bikeBuilder);
        mountainBike.showDetails();
    }
}