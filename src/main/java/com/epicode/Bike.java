package com.epicode;

import java.util.logging.Logger;

/**
 * Abstract base class representing a generic bike.
 * Encapsulates common bike properties, availability, and features.
 * Provides methods to rent and return bikes safely.
 *
 * <p>All business operations are logged, and invalid operations
 * throw custom exceptions.</p>
 */
@Secured("Shielding, logging, validation verified")
@RoleType("Bike")
public abstract class Bike implements BikeComponent {
    private String id;
    private String model;
    private boolean available;
    private boolean lights;
    private boolean basket;
    private boolean GPS;
    private static final Logger logger = Logger.getLogger(Bike.class.getName());

    /**
     * Setters and getters for private fields.
     */
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean hasLights() {
        return lights;
    }

    public void setLights(boolean lights) {
        this.lights = lights;
    }

    public boolean hasBasket() {
        return basket;
    }

    public void setBasket(boolean basket) {
        this.basket = basket;
    }

    public boolean hasGPS() {
        return GPS;
    }

    public void setGPS(boolean GPS) {
        this.GPS = GPS;
    }


    /**
     * Abstract method to get the bike type.
     * @return the BikeType of this bike.
     */
    public abstract BikeType getType();


    /**
     * Constructor using a BikeBuilder. Validates mandatory fields.
     * @param builder the BikeBuilder object
     * @throws InvalidBuilderException if builder is null or has invalid fields
     */
    @Sanitized
    public Bike(BikeBuilder builder) {
        if (builder == null) {
            throw new InvalidBuilderException("Cannot create bike: builder is null");
        }
        if (builder.getId() == null || builder.getId().isBlank()) {
            throw new InvalidBuilderException("Cannot create bike: id is invalid");
        }
        if (builder.getModel() == null || builder.getModel().isBlank()) {
            throw new InvalidBuilderException("Cannot create bike: model is invalid");
        }

        this.id = builder.getId();
        this.model = builder.getModel();
        this.available = builder.isAvailable();
        this.lights = builder.hasLights();
        this.basket = builder.hasBasket();
        this.GPS = builder.hasGPS();
    }


    /**
     * Marks this bike as rented if available.
     * @throws BikeUnavailableException if the bike is already rented
     */
    @Logged
    public void rentBike(){
        if(!isAvailable()){
            logger.warning("Attempt to rent unavailable bike: " + id);
            throw new BikeUnavailableException("Bike " + getId() + " is not available for rent");
        }
        setAvailable(false);
    }


    /**
     * Marks this bike as returned if it was rented.
     * @throws BikeNotRentedException if the bike is already available
     */
    @Logged
    public void returnBike(){
        if(isAvailable()) {
            logger.warning("Bike is already available: " + id);
            throw new BikeNotRentedException("Bike " + getId() + " is not currently rented");
        }
        setAvailable(true);
    }

    @Override
    public String toString() {
        return model;
    }


    @Override
    public void showDetails() {
        System.out.println("-------Bike details------");
        System.out.println("Type: " + getType());
        System.out.println("ID: " + id);
        System.out.println("Model: " + model);
        System.out.println("Available: " + available);
        System.out.println("Lights: " + lights);
        System.out.println("Basket: " + basket);
        System.out.println("GPS: " + GPS);
    }
}