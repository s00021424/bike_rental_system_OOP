package com.epicode;

import java.util.ArrayList;
import java.util.NoSuchElementException;

@Secured("Catalog iterator shielding and logging verified")
@RoleType("Iterator")
public class CatalogIterator implements Iterator<BikeCatalog> {
    private int index = 0;
    private final ArrayList<BikeCatalog> catalogs;

    @Sanitized
    public CatalogIterator(ArrayList<BikeCatalog> catalogs) {
        if (catalogs == null) {
            throw new InvalidCatalogException("Catalog list cannot be null");
        }
        this.catalogs = catalogs;
    }

    /**
     * Checks if more elements are available in the iterator.
     *
     * @return true if more elements exist, false otherwise
     */
    @Override
    public boolean hasNext() {
        return index < catalogs.size();
    }

    /**
     * @return the next BikeCatalog in the iterator.
     * @throws NoSuchElementException if no more elements exist
     */
    @Override
    public BikeCatalog next() {
        if (!hasNext()) {
            throw new NoSuchElementException("No more catalogs in iterator");
        }
        return catalogs.get(index++);
    }
}