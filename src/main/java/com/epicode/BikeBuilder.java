package com.epicode;

/**
 * Builder class for creating Bike objects.
 * Ensures mandatory fields are validated and optional features can be set fluently.
 */
@Secured("Builder validates mandatory fields for Bike creation")
@RoleType("Builder")
public class BikeBuilder {
    private final String id;
    private final String model;
    private final boolean available;

    private boolean lights = false;
    private boolean basket = false;
    private boolean GPS = false;


    /**
     * Constructor for mandatory fields.
     * @param id bike ID
     * @param model bike model
     * @param available initial availability
     * @throws InvalidBuilderException if id or model are null or blank
     */
    @Sanitized
    public BikeBuilder(String id, String model, boolean available) {
        if (id == null || id.isBlank()) {
            throw new InvalidBuilderException("Bike ID cannot be null or blank");
        }
        if (model == null || model.isBlank()) {
            throw new InvalidBuilderException("Bike model cannot be null or blank");
        }

        this.id = id;
        this.model = model;
        this.available = available;
    }

    /**
     * Fluent setters for optional features with respective getters for access.
     */

    public String getId() { return id; }
    public String getModel() { return model; }
    public boolean isAvailable() { return available; }


    public boolean hasLights() { return lights; }
    public BikeBuilder setLights(boolean lights) {
        this.lights = lights;
        return this;
    }

    public boolean hasBasket() { return basket; }
    public BikeBuilder setBasket(boolean basket) {
        this.basket = basket;
        return this;
    }

    public boolean hasGPS() { return GPS; }
    public BikeBuilder setGPS(boolean GPS) {
        this.GPS = GPS;
        return this;
    }
}