package com.epicode;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BikeTest {

    @Test
    public void testRentBikeSuccessfully() {
        Bike bike = new MountainBike(new BikeBuilder("id1", "GT", true));
        assertTrue(bike.isAvailable());

        bike.rentBike();
        assertFalse(bike.isAvailable());
    }

    @Test
    public void testRentBikeUnavailableThrows() {
        Bike bike = new MountainBike(new BikeBuilder("id2", "GT", false));
        assertThrows(BikeUnavailableException.class, bike::rentBike);
    }

    @Test
    public void testReturnBikeSuccessfully() {
        Bike bike = new MountainBike(new BikeBuilder("id3", "GT", false));
        bike.setAvailable(false);
        bike.returnBike();
        assertTrue(bike.isAvailable());
    }

    @Test
    public void testReturnBikeNotRentedThrows() {
        Bike bike = new MountainBike(new BikeBuilder("id4", "GT", true));
        assertThrows(BikeNotRentedException.class, bike::returnBike);
    }

    @Test
    public void testGetType() {
        Bike bike = new MountainBike(new BikeBuilder("id5", "GT", true));
        assertEquals(BikeType.mountain, bike.getType());
    }
}