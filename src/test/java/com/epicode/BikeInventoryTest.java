package com.epicode;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

public class BikeInventoryTest {

    @Test
    public void testAddAndRemoveCatalog() {
        BikeInventory inventory = new BikeInventory(new ArrayList<>());
        BikeCatalog catalog = new MountainBikeCatalog(new ArrayList<>());
        inventory.addCatalog(catalog);
        assertEquals(1, inventory.getCatalogs().size());

        inventory.removeCatalog(0);
        assertEquals(0, inventory.getCatalogs().size());
    }

    @Test
    public void testRemoveInvalidIndexThrows() {
        BikeInventory inventory = new BikeInventory(new ArrayList<>());
        assertThrows(InvalidSelectionException.class, () -> inventory.removeCatalog(0));
    }

    @Test
    public void testGetElementAt() {
        BikeInventory inventory = new BikeInventory(new ArrayList<>());
        BikeCatalog catalog = new MountainBikeCatalog(new ArrayList<>());
        inventory.addCatalog(catalog);
        assertEquals(catalog, inventory.getElementAt(0));
        assertThrows(InvalidSelectionException.class, () -> inventory.getElementAt(1));
    }
}