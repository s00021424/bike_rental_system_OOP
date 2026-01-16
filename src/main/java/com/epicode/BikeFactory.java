package com.epicode;

/**
 * Abstract base class representing a generic factory.
 * Provides abstract method to create bikes safely.
 */
@Secured("Exception shielding")
@RoleType("Factory")
public abstract class BikeFactory {
    public abstract Bike createBike(BikeBuilder bikeBuilder);

    /**
     * @throws InvalidBuilderException if builder is null or has invalid fields.
     */
    @Sanitized
    protected void validateBuilder(BikeBuilder builder) {
        if (builder == null) {
            throw new InvalidBuilderException("BikeBuilder cannot be null");
        }
        if (builder.getId() == null || builder.getId().isBlank()) {
            throw new InvalidBuilderException("Bike ID cannot be null or empty");
        }
        if (builder.getModel() == null || builder.getModel().isBlank()) {
            throw new InvalidBuilderException("Bike model cannot be null or empty");
        }
    }
}