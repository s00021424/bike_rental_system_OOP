package com.epicode;

/**
 * Represents a specific type of bike.
 * Extends abstract Bike class and defines getType().
 */
@Secured("Shielding, logging, validation verified")
@RoleType("Bike")
public class FoldingBike extends Bike {
    public FoldingBike(BikeBuilder builder) {
        super(builder);
    }

    @Override
    public BikeType getType() {
        return BikeType.folding;
    }
}