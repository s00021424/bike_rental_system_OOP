package com.epicode;

import java.util.logging.Logger;

/**
 * Factory class for creating a specific type of bike.
 * Shields and logs errors during creation.
 */
@Secured("Factory creation shielding and logging verified")
@RoleType("Factory")
public class ElectricBikeFactory extends BikeFactory {
    private static final Logger logger = Logger.getLogger(ElectricBikeFactory.class.getName());

    /**
     * Creates a bike of the specific type using the provided builder.
     * Validates the builder and handles unexpected runtime exceptions.
     *
     * @param bikeBuilder the builder with bike data
     * @return the created bike
     * @throws RentalException if bike creation fails due to runtime errors
     */
    @Sanitized
    @Logged
    @Override
    public ElectricBike createBike(BikeBuilder bikeBuilder) {
        try {
            validateBuilder(bikeBuilder);
            return new ElectricBike(bikeBuilder);
        } catch (RuntimeException e) {
            logger.severe("Failed to create electric bike: " + e.getMessage());
            throw new RentalException("Unable to create electric bike");
        }
    }
}