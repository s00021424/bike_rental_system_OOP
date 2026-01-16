package com.epicode;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Iterator over a list of bikes.
 * Implements hasNext() and next() according to Iterator interface.
 */
@Secured("Bike iterator shielding and logging verified")
@RoleType("Iterator")
public class BikeIterator implements Iterator<Bike> {
    private int index = 0;
    private final ArrayList<Bike> bikes;

    @Sanitized
    public BikeIterator(ArrayList<Bike> bikes) {
        if (bikes == null) {
            throw new InvalidCatalogException("Bike list cannot be null");
        }
        this.bikes = bikes;
    }

    /**
     * Checks if more elements are available in the iterator.
     *
     * @return true if more elements exist, false otherwise
     */
    @Override
    public boolean hasNext() {
        return index < bikes.size();
    }

    /**
     * @return the next Bike in the iterator.
     * @throws NoSuchElementException if no more elements exist
     */
    @Override
    public Bike next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more bikes in iterator");
        }
        return bikes.get(index++);
    }
}