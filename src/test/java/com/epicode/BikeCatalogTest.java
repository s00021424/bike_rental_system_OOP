package com.epicode;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class BikeCatalogTest {

    @Test
    public void testAddAndRemoveBike() {
        BikeCatalog catalog = new MountainBikeCatalog(new ArrayList<>());
        Bike bike = new MountainBike(new BikeBuilder("b1", "GT", true));
        catalog.addBike(bike);
        assertEquals(1, catalog.getBikes().size());

        catalog.removeBike(bike);
        assertEquals(0, catalog.getBikes().size());
    }

    @Test
    public void testAddNullBikeThrows() {
        BikeCatalog catalog = new MountainBikeCatalog(new ArrayList<>());
        assertThrows(InvalidBikeException.class, () -> catalog.addBike(null));
    }

    @Test
    public void testGetElementAtValidAndInvalid() {
        BikeCatalog catalog = new MountainBikeCatalog(new ArrayList<>());
        Bike bike = new MountainBike(new BikeBuilder("b2", "GT", true));
        catalog.addBike(bike);

        assertEquals(bike, catalog.getElementAt(0));
        assertThrows(InvalidSelectionException.class, () -> catalog.getElementAt(1));
    }
}