package com.epicode;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BikeFactoryTest {

    @Test
    public void testMountainBikeFactoryCreatesBike() {
        MountainBikeFactory factory = new MountainBikeFactory();
        BikeBuilder builder = new BikeBuilder("m1", "GT", true);
        Bike bike = factory.createBike(builder);
        assertNotNull(bike);
        assertEquals(BikeType.mountain, bike.getType());
    }

    @Test
    public void testFactoryInvalidBuilderThrows() {
        MountainBikeFactory factory = new MountainBikeFactory();
        assertThrows(InvalidBuilderException.class, () -> factory.createBike(new BikeBuilder("", "GT", true)));
    }

    @Test
    public void testFactoryRuntimeExceptionHandled() {
        MountainBikeFactory factory = new MountainBikeFactory();
        BikeBuilder builder = new BikeBuilder("m2", "GT", true) {
            @Override
            public String getModel() {
                throw new RuntimeException("Simulated failure");
            }
        };
        assertThrows(RentalException.class, () -> factory.createBike(builder));
    }
}