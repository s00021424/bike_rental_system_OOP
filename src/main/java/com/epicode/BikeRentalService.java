package com.epicode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;


/**
 * Service class to manage bike rentals, returns, and creation.
 * Supports logging, input validation, and shielding for unexpected exceptions.
 */
@Secured("Rental service shielding, logging, input validation verified")
@RoleType("Service System")
public class BikeRentalService {
    private static final Logger logger = Logger.getLogger(BikeRentalService.class.getName());

    private final Map<String, Bike> bikesHash = new HashMap<>();
    private final BikeAuditRepository bikeAuditCreation = new BikeAuditRepository("data/bikes.log");
    private final BikeAuditRepository bikeAuditRental = new BikeAuditRepository("data/rentals.log");
    private final ArrayList<BikeCatalog> catalogs = new ArrayList<>();
    private final BikeInventory bikeInventory = new BikeInventory(catalogs);


    /**
     * Creates a bike in the specified catalog using a builder and type.
     * @param bikeBuilder builder for bike
     * @param bikeCatalog catalog to add bike
     * @param bikeType type of bike
     * @return created bike
     * @throws RentalException if any unexpected error occurs
     */
    @Sanitized
    @Logged
    public Bike bikeCreation(BikeBuilder bikeBuilder, BikeCatalog bikeCatalog, BikeType bikeType) {

        if (bikeCatalog == null) {
            throw new CatalogNotFoundException("Catalog not found");
        }

        if (bikeBuilder == null) {
            throw new InvalidBuilderException("Bike builder cannot be null");
        }

        if (bikeType == null) {
            logger.warning("Bike type is null");
            throw new InvalidBikeTypeException("Bike type cannot be null");
        }

        BikeFactory bikeFactory;
        switch (bikeType) {
            case mountain -> bikeFactory = new MountainBikeFactory();
            case electric -> bikeFactory = new ElectricBikeFactory();
            case road -> bikeFactory = new RoadBikeFactory();
            case folding -> bikeFactory = new FoldingBikeFactory();
            default -> throw new InvalidBikeTypeException("Unknown bike type: " + bikeType);
        }

        try {
            Bike bike = bikeFactory.createBike(bikeBuilder);
            bikesHash.put(bike.getId(), bike);
            bikeCatalog.addBike(bike);
            bikeInventory.addCatalog(bikeCatalog);
            bikeAuditCreation.recordCreation(bike, bikeCatalog);
            logger.info("Bike created: " + bike.getId() + " in catalog: " + bikeCatalog);
            return bike;
        } catch (RuntimeException e) {
            // Catch only unexpected runtime errors
            logger.severe("Unexpected error during bike creation: " + e.getMessage());
            throw new RentalException("Internal error occurred during bike creation");
        }
    }


    /**
     * Rents a bike by ID to a user.
     * @param id bike ID
     * @param safeFirstName user's first name
     * @param safeLastName user's last name
     * @throws RentalException if rental fails
     */
    @Sanitized
    @Logged
    public void rentingBike(String id, String safeFirstName, String safeLastName) {
        Bike bike = bikesHash.get(id);
        if (bike == null) {
            throw new BikeNotFoundException("Bike ID not found: " + id);
        }

        if (!bike.isAvailable()) {
            throw new BikeUnavailableException("Bike already rented");
        }

        try {
            bike.rentBike();
            bikeAuditRental.recordRental(bike, safeFirstName, safeLastName);
            logger.info("Bike rented: " + id + " by " + safeFirstName + " " + safeLastName);
        } catch (BikeUnavailableException e) {
            // Controlled business exception
            throw new RentalException(e.getMessage());
        } catch (Exception e) {
            // Unexpected internal error
            logger.severe("Unexpected error during rental of bike " + id + ": " + e.getMessage());
            throw new RentalException("Internal error occurred during bike rental");
        }
    }


    /**
     * Retrieves a bike from the internal map by its ID.
     * Trims input and returns null if the ID is invalid.
     *
     * @param id bike ID to search for
     * @return the Bike object if found, or null if not found or ID invalid
     */
    @Sanitized
    public Bike getBikeById(String id) {
        if (id == null || id.isBlank()) return null;
        return bikesHash.get(id.trim());
    }


    /**
     * Returns a bike by ID from a user.
     * @param id bike ID
     * @param safeFirstName user's first name
     * @param safeLastName user's last name
     * @throws RentalException if return fails
     */
    @Sanitized
    @Logged
    public void returningBike(String id, String safeFirstName, String safeLastName) {
        Bike foundBike = getBikeById(id);
        if (foundBike == null) {
            throw new BikeNotFoundException("Bike ID " + id + " not found");
        }

        try {
            foundBike.returnBike();
            bikeAuditRental.recordReturn(foundBike, safeFirstName, safeLastName);
            logger.info("Bike returned: " + id + " by " + safeFirstName + " " + safeLastName);
        } catch (BikeNotRentedException e) {
            throw new RentalException(e.getMessage());
        } catch (Exception e) {
            logger.severe("Unexpected error during return of bike " + id + ": " + e.getMessage());
            throw new RentalException("Internal error occurred during bike return");
        }
    }
}