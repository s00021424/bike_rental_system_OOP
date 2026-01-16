package com.epicode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class RentalAppTest {

    private BikeRentalService mockService;
    private BikeInventory inventory;
    private RentalApp app;

    @BeforeEach
    public void setup() {
        mockService = mock(BikeRentalService.class);

        ArrayList<BikeCatalog> catalogs = new ArrayList<>();
        BikeCatalog catalog = new MountainBikeCatalog(new ArrayList<>());
        catalogs.add(catalog);

        inventory = new BikeInventory(catalogs);

        app = new RentalApp(mockService, inventory);
    }

    @Test
    public void testStartImmediateExit() {
        // Simulate user input: "0" -> exit immediately
        String simulatedInput = "0\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        app.start(); // uses its internal Scanner(System.in)

        // Verify that rental service was never called
        verify(mockService, never()).rentingBike(anyString(), anyString(), anyString());
    }

    @Test
    public void testHandleRentFlow() {
        // Simulate full rent flow: first name, last name, catalog 0, bike 0, exit
        // Note: catalog.getBikes() is empty, so it will just loop once then exit
        String simulatedInput = "John\nDoe\n0\n0\n0\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        app.start(); // uses its internal Scanner(System.in)

        // No exceptions, program terminates without hanging
        verify(mockService, never()).rentingBike(anyString(), anyString(), anyString());
    }
}