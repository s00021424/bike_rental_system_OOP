package com.epicode;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BikeBuilderTest {

    @Test
    public void testValidBuilder() {
        BikeBuilder builder = new BikeBuilder("123", "GT", true)
                .setLights(true)
                .setBasket(true)
                .setGPS(true);
        assertEquals("123", builder.getId());
        assertEquals("GT", builder.getModel());
        assertTrue(builder.isAvailable());
        assertTrue(builder.hasLights());
        assertTrue(builder.hasBasket());
        assertTrue(builder.hasGPS());
    }

    @Test
    public void testInvalidBuilderThrows() {
        assertThrows(InvalidBuilderException.class, () -> new BikeBuilder(null, "GT", true));
        assertThrows(InvalidBuilderException.class, () -> new BikeBuilder("123", "", true));
    }
}