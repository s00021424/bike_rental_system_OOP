package com.epicode;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main application class to interact with users for bike rentals and returns.
 * Handles input validation, shielding, and logging.
 */
@Secured("RentalApp shielding, input validation, and logging verified")
@RoleType("Application")
public class RentalApp {
    private final BikeRentalService bikeRentalService;
    private final BikeInventory bikeInventory;

    public RentalApp(BikeRentalService bikeRentalService, BikeInventory bikeInventory) {
        this.bikeRentalService = bikeRentalService;
        this.bikeInventory = bikeInventory;
    }

    /**
     * Starts the rental application and user menu loop.
     */
    @Sanitized
    public void start() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nWelcome to Bike Rental");
            System.out.println("1. Rent a bike");
            System.out.println("2. Return a bike");
            System.out.println("0. Exit");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> handleRent(scanner);
                case "2" -> handleReturn(scanner);
                case "0" -> {
                    System.out.println("Goodbye!");
                    return;
                }
                default -> System.out.println("Invalid option. Please enter 0, 1, or 2.");
            }
        }
    }

    /**
     * Reads and validates a user's name from the scanner input.
     * Uses InputValidator to sanitize input.
     *
     * @param prompt the message to display to the user
     * @param scanner the Scanner object to read input
     * @return a sanitized and valid name
     */
    @Sanitized
    private String readValidName(String prompt, Scanner scanner) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return InputValidator.sanitizeName(input);
            } catch (InputValidationException e) {
                System.out.println("Invalid input: " + e.getMessage() + ". Try again.");
            }
        }
    }


    /**
     * Reads and validates a bike ID from the scanner input.
     * Uses InputValidator to sanitize the ID.
     *
     * @param prompt the message to display to the user
     * @param scanner the Scanner object to read input
     * @return a sanitized and valid bike ID
     */
    @Sanitized
    private String readValidId(String prompt, Scanner scanner) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            try {
                return InputValidator.sanitizeId(input);
            } catch (InputValidationException e) {
                System.out.println("Invalid ID: " + e.getMessage() + ". Try again.");
            }
        }
    }

    /**
     * Reads an integer input safely within a specified range.
     * Keeps prompting until a valid integer between 0 and max is entered.
     *
     * @param scanner the Scanner object to read input
     * @param max the maximum allowed integer value
     * @return a safe integer between 0 and max (inclusive)
     */
    @Sanitized
    private int readSafeInteger(Scanner scanner, int max) {
        while (true) {
            String input = scanner.nextLine();
            try {
                int value = Integer.parseInt(input);
                if (value < 0 || value > max) {
                    System.out.print("Invalid selection. Enter a number between 0 and " + max + ": \n");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.print("Invalid input. Enter a number between 0 and " + max + ": \n");
            }
        }
    }

    /**
     * Reads a yes/no input from the user and returns a boolean.
     * Keeps prompting until the user enters a valid response (yes, y, no, n).
     *
     * @param scanner the Scanner object to read input
     * @param prompt the message to display to the user
     * @return true if the user entered yes or y, false if no or n
     */
    @Sanitized
    private boolean readYesNo(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim().toLowerCase();

            if (input.equals("yes") || input.equals("y")) {
                return true;
            }
            if (input.equals("no") || input.equals("n")) {
                return false;
            }

            System.out.println("Please answer yes or no.");
        }
    }

    /**
     * Handles the bike rental process for a user.
     * Guides the user through catalog selection, bike selection, and executes the rental.
     *
     * @param scanner the Scanner object to read user input
     */
    @Sanitized
    public void handleRent(Scanner scanner) {

        String safeFirstName = readValidName("Enter your first name: ", scanner);
        String safeLastName = readValidName("Enter your last name: ", scanner);

        ArrayList<BikeCatalog> catalogs = bikeInventory.getCatalogs();

        if (catalogs.isEmpty()) {
            System.out.println("No catalogs available.");
            return;
        }

        while (true) {
            BikeCatalog chosenCatalog = null;

            // -------- Catalog selection loop --------
            while (chosenCatalog == null) {
                try {
                    System.out.println("Choose a catalog:");
                    bikeInventory.listCatalogs(bikeInventory.createIterator());

                    int catalogChoice = readSafeInteger(scanner, catalogs.size() - 1);
                    chosenCatalog = catalogs.get(catalogChoice);

                    if (chosenCatalog.getBikes().isEmpty()) {
                        System.out.println("Selected catalog has no bikes.");
                        if (askGoBackToMenu(scanner)) return;
                        chosenCatalog = null;
                    }

                } catch (RentalException e) {
                    System.out.println("Error: " + e.getMessage());
                    if (askGoBackToMenu(scanner)) return;
                }
            }

            ArrayList<Bike> bikes = chosenCatalog.getBikes();

            Bike chosenBike = null;

            // -------- Bike selection loop --------
            while (chosenBike == null) {
                try {
                    System.out.println("Choose a bike:");
                    chosenCatalog.listBikes(chosenCatalog.createIterator());

                    int bikeChoice = readSafeInteger(scanner, bikes.size() - 1);
                    Bike candidate = chosenCatalog.getElementAt(bikeChoice);

                    if (!candidate.isAvailable()) {
                        System.out.println("This bike is already rented.");
                        if (askGoBackToMenu(scanner)) return;
                    } else {
                        chosenBike = candidate;
                    }

                } catch (RentalException e) {
                    System.out.println("Error: " + e.getMessage());
                    if (askGoBackToMenu(scanner)) return;
                }
            }

            // -------- Rental execution --------
            try {
                bikeRentalService.rentingBike(
                        chosenBike.getId(),
                        safeFirstName,
                        safeLastName
                );
                System.out.println("\nBike rented successfully!");
                return;

            } catch (RentalException e) {
                System.out.println("Rental failed: " + e.getMessage());
                if (askGoBackToMenu(scanner)) return;
            }
        }
    }


    /**
     * Handles the bike return process for a user.
     * Prompts the user for bike ID and executes the return process.
     *
     * @param scanner the Scanner object to read user input
     */
    public void handleReturn(Scanner scanner) {

        String safeFirstName = readValidName("Enter your first name: ", scanner);
        String safeLastName  = readValidName("Enter your last name: ", scanner);

        while (true) {
            try {
                String safeID = readValidId("Enter bike ID: ",  scanner);

                bikeRentalService.returningBike(safeID, safeFirstName, safeLastName);
                System.out.println("Bike returned successfully.");
                return;

            } catch (RentalException e) {
                System.out.println("Return failed: " + e.getMessage());
                if (askGoBackToMenu(scanner)) return;
            }
        }
    }

    /**
     * Prompts the user whether they want to go back to the main menu.
     *
     * @param scanner the Scanner object to read input
     * @return true if the user wants to go back to the main menu, false otherwise
     */
    private boolean askGoBackToMenu(Scanner scanner) {
        return readYesNo(scanner, "Go back to main menu? (yes/no): ");
    }

    public static void main(String[] args) {
        BikeRentalService bikeRentalService = new BikeRentalService();

        ArrayList<BikeCatalog> catalogs = new ArrayList<>();
        BikeInventory bikeInventory = new BikeInventory(catalogs);

        ArrayList<Bike> mountainBikeList = new ArrayList<>();
        ArrayList<Bike> electricBikeList = new ArrayList<>();
        ArrayList<Bike> roadBikeList = new ArrayList<>();
        ArrayList<Bike> foldingBikeList = new ArrayList<>();

        MountainBikeCatalog mountainBikeCatalog = new MountainBikeCatalog(mountainBikeList);
        ElectricBikeCatalog electricBikeCatalog = new ElectricBikeCatalog(electricBikeList);
        FoldingBikeCatalog foldingBikeCatalog = new FoldingBikeCatalog(foldingBikeList);
        RoadBikeCatalog roadBikeCatalog = new RoadBikeCatalog(roadBikeList);

        bikeInventory.addCatalog(mountainBikeCatalog);
        bikeInventory.addCatalog(electricBikeCatalog);
        bikeInventory.addCatalog(foldingBikeCatalog);


        BikeBuilder bikeBuilder1 = new BikeBuilder("123abc", "GT3", true)
                .setLights(true)
                .setGPS(true);

        BikeBuilder bikeBuilder2 = new BikeBuilder("456def", "TT8", false)
                .setGPS(true);

        BikeBuilder bikeBuilder3 = new BikeBuilder("789ghi", "SSR", true)
                .setLights(true)
                .setBasket(true)
                .setGPS(true);

        BikeBuilder bikeBuilder4 = new BikeBuilder("1011jkl", "ZV10", false);


        bikeRentalService.bikeCreation(bikeBuilder1, mountainBikeCatalog, BikeType.mountain);
        bikeRentalService.bikeCreation(bikeBuilder2, electricBikeCatalog, BikeType.electric);
        bikeRentalService.bikeCreation(bikeBuilder3, electricBikeCatalog, BikeType.electric);
        bikeRentalService.bikeCreation(bikeBuilder4, roadBikeCatalog, BikeType.road);

        RentalApp app = new RentalApp(bikeRentalService, bikeInventory);
        app.start();
    }
}