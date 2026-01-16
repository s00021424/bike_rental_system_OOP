package com.epicode;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 * Class representing a collection of BikeCatalogs.
 * Supports adding, removing, listing, and selecting catalogs safely.
 */
@Secured("Inventory handling: shielding and logging")
@RoleType("Inventory")
public class BikeInventory implements BikeCollection<BikeCatalog> {
    private static final Logger logger = Logger.getLogger(BikeInventory.class.getName());
    private final ArrayList<BikeCatalog> catalogs;


    /**
     * Constructor with initial catalog list.
     * @param catalogs initial catalogs
     * @throws InvalidCatalogException if catalogs is null
     */
    @Sanitized
    public BikeInventory(ArrayList<BikeCatalog> catalogs) {
        if (catalogs == null) {
            throw new InvalidCatalogException("Catalog list cannot be null");
        }
        this.catalogs = catalogs;
    }

    public ArrayList<BikeCatalog> getCatalogs() {
        return new ArrayList<>(catalogs);
    }


    /**
     * Adds a catalog safely.
     * @param catalog catalog to add
     * @throws InvalidCatalogException if catalog is null
     */
    @Sanitized
    @Logged
    public void addCatalog(BikeCatalog catalog) {
        if (catalog == null) {
            logger.warning("Attempted to add null catalog");
            throw new InvalidCatalogException("Cannot add null catalog");
        }
        catalogs.add(catalog);
        logger.info("Catalog added successfully: " + catalog);
    }

    /**
     * Removes catalog at index safely.
     * @param index index to remove
     * @throws InvalidSelectionException if index is invalid
     */
    @Sanitized
    @Logged
    public void removeCatalog(int index) {
        if (index < 0 || index >= catalogs.size()) {
            logger.warning("Attempted to remove invalid catalog");
            throw new InvalidSelectionException("Invalid catalog index: " + index);
        }
        BikeCatalog removed = catalogs.remove(index);
        logger.info("Catalog removed successfully: " + removed);
    }

    @Override
    public Iterator<BikeCatalog> createIterator() {
        return new CatalogIterator(catalogs);
    }

    @Override
    public int getSize() {
        return catalogs.size();
    }

    @Sanitized
    @Logged
    @Override
    public BikeCatalog getElementAt(int index) {
        if (index < 0 || index >= catalogs.size()) {
            logger.warning("Attempted to select with invalid index: " + index);
            throw new InvalidSelectionException("Invalid catalog index: " + index);
        }
        return catalogs.get(index);
    }

    /**
     * Prints the list of catalogs using the provided iterator.
     * Handles empty catalog lists and null iterators gracefully.
     *
     * @param catalogIterator iterator of catalogs to list
     */
    @Sanitized
    public void listCatalogs(Iterator<BikeCatalog> catalogIterator) {
        if (catalogIterator == null) {
            System.out.println("Catalog iterator not available.");
            return;
        }

        if (catalogs.isEmpty()) {
            System.out.println("No catalogs available.");
            return;
        }

        int index = 0;
        while (catalogIterator.hasNext()) {
            BikeCatalog catalog = catalogIterator.next();
            System.out.println(index + ". " + catalog);
            index++;
        }
    }
}