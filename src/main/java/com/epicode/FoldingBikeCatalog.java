package com.epicode;

import java.util.ArrayList;

/**
 * Represents a specific type of catalog.
 * Extends abstract BikeCatalog class.
 */
@Secured("BikeCatalog shielding and logging applied")
@RoleType("Catalog")
public class FoldingBikeCatalog extends BikeCatalog {
    public FoldingBikeCatalog(ArrayList<Bike> bikes) {
        super(bikes);
    }

    @Override
    public String toString() {
        return "Folding Bike Catalog";
    }
}