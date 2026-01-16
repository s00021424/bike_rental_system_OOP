package com.epicode;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Abstract class representing a collection of bikes (catalog).
 * Provides methods to add, remove, and list bikes safely.
 * Implements BikeComponent and iterable interface via custom Iterator.
 */
@Secured("BikeCatalog shielding and logging applied")
@RoleType("Catalog")
public abstract class BikeCatalog implements BikeComponent, BikeCollection<Bike> {
    private static final Logger logger = Logger.getLogger(BikeCatalog.class.getName());
    private final ArrayList<Bike> bikes;


    /**
     * Constructor with an initial list of bikes.
     * @param bikes initial bike list
     * @throws InvalidCatalogException if bikes list is null
     */
    @Sanitized
    public BikeCatalog(ArrayList<Bike> bikes) {
        if (bikes == null) {
            throw new InvalidCatalogException("Bike list cannot be null");
        }
        this.bikes = bikes;
    }

    public ArrayList<Bike> getBikes() {
        return new ArrayList<>(bikes);
    }

    /**
     * Adds a bike to the catalog safely.
     * @param bike the bike to add
     * @throws InvalidBikeException if bike is null
     */
    @Logged
    public void addBike(Bike bike) {
        if (bike == null) {
            logger.warning("Attempted to add null bike to catalog");
            throw new InvalidBikeException("Cannot add null bike to catalog");
        }
        bikes.add(bike);
    }

    /**
     * Removes a bike from the catalog safely.
     * @param bike the bike to remove
     * @throws InvalidBikeException if bike is null
     * @throws BikeNotFoundException if bike does not exist in catalog
     */
    @Logged
    public void removeBike(Bike bike) {
        if (bike == null) {
            logger.warning("Attempted to remove null bike from catalog");
            throw new InvalidBikeException("Cannot remove null bike from catalog");
        }

        boolean removed = bikes.remove(bike);
        if (!removed) {
            logger.warning("Attempted to remove non-existent bike: " + bike.getId());
            throw new BikeNotFoundException("Bike not found in catalog");
        }
    }

    @Override
    public void showDetails() {
        for (Bike bike : bikes) {
            bike.showDetails();  // Already safe, no try-catch needed
        }
    }

    @Override
    public Iterator<Bike> createIterator() {
        return new BikeIterator(bikes);
    }

    @Override
    public int getSize() {
        return bikes.size();
    }

    @Logged
    @Override
    public Bike getElementAt(int index) {
        if (index < 0 || index >= bikes.size()) {
            logger.warning("Selection attempt with invalid index: " + index);
            throw new InvalidSelectionException("Invalid bike index: " + index);
        }
        return bikes.get(index);
    }

    /**
     * Prints the list of bikes in the catalog using the provided iterator.
     * Handles empty lists and null iterators gracefully.
     *
     * @param bikeIterator iterator of bikes to list
     */
    public void listBikes(Iterator<Bike> bikeIterator) {
        if (bikeIterator == null) {
            System.out.println("Bike iterator not available.");
            return;
        }

        if (bikes.isEmpty()) {
            System.out.println("No bikes available.");
            return;
        }

        int index = 0;
        while (bikeIterator.hasNext()) {
            Bike bike = bikeIterator.next();
            System.out.println(index + ". " + bike);
            index++;
        }
    }
}