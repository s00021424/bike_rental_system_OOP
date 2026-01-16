package com.epicode;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class IteratorsTest {

    @Test
    public void testBikeIterator() {
        ArrayList<Bike> bikes = new ArrayList<>();
        Bike bike = new MountainBike(new BikeBuilder("b1", "GT", true));
        bikes.add(bike);
        BikeIterator iterator = new BikeIterator(bikes);

        assertTrue(iterator.hasNext());
        assertEquals(bike, iterator.next());
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    public void testCatalogIterator() {
        ArrayList<BikeCatalog> catalogs = new ArrayList<>();
        BikeCatalog catalog = new MountainBikeCatalog(new ArrayList<>());
        catalogs.add(catalog);
        CatalogIterator iterator = new CatalogIterator(catalogs);

        assertTrue(iterator.hasNext());
        assertEquals(catalog, iterator.next());
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }
}
