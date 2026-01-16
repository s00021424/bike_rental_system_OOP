package com.epicode;

import java.io.*;
import java.time.LocalDateTime;
import java.util.logging.Logger;


/**
 * Responsible for recording bike creation, rentals, and returns to a log file.
 * All file I/O is safely shielded and errors are logged.
 */
@Secured("Audit repository with file storage")
@RoleType("Repository")
public class BikeAuditRepository {
    private static final Logger logger = Logger.getLogger(BikeAuditRepository.class.getName());
    private final File file;

    public BikeAuditRepository(String filePath) {
        this.file = new File(filePath);
        initialize();
    }
    @Sanitized
    @Logged
    private void initialize() {
        try {
            if (file.getParentFile() != null) file.getParentFile().mkdirs();
            file.createNewFile();
        } catch (IOException e) {
            logger.severe("Failed to initialize audit file: " + e.getMessage());
            throw new StorageException("System storage unavailable", e);
        }
    }

    public void recordCreation(Bike bike, BikeCatalog catalog) {
        writeToFile(formatCreationEntry(bike, catalog));
    }

    public void recordRental(Bike bike, String firstName, String lastName) {
        writeToFile(formatRentalEntry(bike, firstName, lastName));
    }

    public void recordReturn(Bike bike, String firstName, String lastName) {
        writeToFile(formatReturnEntry(bike, firstName, lastName));
    }

    @Logged
    private void writeToFile(String entry) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            writer.write(entry);
            writer.newLine();
        } catch (IOException e) {
            logger.severe("Failed to write audit entry: " + e.getMessage());
            throw new StorageException("Unable to record operation", e);
        }
    }

    private String formatCreationEntry(Bike bike, BikeCatalog catalog) {
        return String.format("[%s] CREATED | Bike=%s | Type=%s | Catalog=%s",
                LocalDateTime.now(), bike.getId(), bike.getType(), catalog);
    }

    private String formatRentalEntry(Bike bike, String firstName, String lastName) {
        return String.format("[%s] RENTED | Bike=%s | First Name=%s | Last Name=%s",
                LocalDateTime.now(), bike.getId(), firstName, lastName);
    }

    private String formatReturnEntry(Bike bike, String firstName, String lastName) {
        return String.format("[%s] RETURNED | Bike=%s | First Name=%s | Last Name=%s",
                LocalDateTime.now(), bike.getId(), firstName, lastName);
    }
}