package com.epicode;

/**
 * Represents a specific type of bike.
 * Extends abstract Bike class and defines getType().
 */
@Secured("Shielding, logging, validation verified")
@RoleType("Bike")
public class RoadBike extends Bike {
    public RoadBike(BikeBuilder builder) {
        super(builder);
    }

    @Override
    public BikeType getType() {
        return BikeType.road;
    }
}