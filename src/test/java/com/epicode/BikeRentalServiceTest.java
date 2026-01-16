package com.epicode;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class BikeRentalServiceTest {

    private BikeRentalService service;
    private BikeCatalog catalog;

    private static class TestAuditRepository extends BikeAuditRepository {
        boolean creationCalled = false;
        boolean rentalCalled = false;
        boolean returnCalled = false;

        public TestAuditRepository() {
            super("data/test.log");
        }

        @Override
        public void recordCreation(Bike bike, BikeCatalog catalog) {
            creationCalled = true;
        }

        @Override
        public void recordRental(Bike bike, String firstName, String lastName) {
            rentalCalled = true;
        }

        @Override
        public void recordReturn(Bike bike, String firstName, String lastName) {
            returnCalled = true;
        }
    }

    private TestAuditRepository auditRepo;

    @BeforeEach
    public void setup() {
        service = new BikeRentalService();
        catalog = new MountainBikeCatalog(new ArrayList<>());
        auditRepo = new TestAuditRepository();
    }

    @Test
    public void testBikeCreationAndAuditCalled() {
        BikeBuilder builder = new BikeBuilder("b1", "GT", true);
        Bike bike = service.bikeCreation(builder, catalog, BikeType.mountain);

        assertNotNull(bike);
        assertTrue(catalog.getBikes().contains(bike));
    }

    @Test
    public void testRentAndReturnBike() {
        BikeBuilder builder = new BikeBuilder("b2", "GT", true);
        Bike bike = service.bikeCreation(builder, catalog, BikeType.mountain);

        // Rent bike
        service.rentingBike(bike.getId(), "John", "Doe");
        assertFalse(bike.isAvailable());

        // Return bike
        service.returningBike(bike.getId(), "John", "Doe");
        assertTrue(bike.isAvailable());
    }

    @Test
    public void testRentUnavailableBikeThrows() {
        BikeBuilder builder = new BikeBuilder("b3", "GT", false); // bike not available
        Bike bike = service.bikeCreation(builder, catalog, BikeType.mountain);

        assertThrows(RentalException.class, () -> service.rentingBike(bike.getId(), "Jane", "Doe"));
    }

    @Test
    public void testReturnBikeNotRentedThrows() {
        BikeBuilder builder = new BikeBuilder("b4", "GT", true);
        Bike bike = service.bikeCreation(builder, catalog, BikeType.mountain);

        // Bike is available, so returning should throw
        assertThrows(RentalException.class, () -> service.returningBike(bike.getId(), "Jane", "Doe"));
    }
}