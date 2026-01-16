# Bike Rental Management System

## Overview

The **Bike Rental Management System** is an object-oriented Java application designed to manage bike rentals efficiently. It supports multiple bike types and catalogs, allows users to rent and return bikes, and maintains detailed audit logs for all operations.

**Core Features:**

- Multiple bike types: **Mountain**, **Electric**, **Folding**, **Road**
- Catalog management: separate catalogs per bike type
- Bike inventory management
- User-friendly rental and return workflow
- Input validation and sanitization for user data
- Logging and auditing of bike creation, rentals, and returns
- Exception handling for business rules (e.g., bike unavailable, invalid selections)

---

## Technologies and Patterns Used

**Technologies:**

- **Java** – core language for OOP implementation  
- **Java Collections** – `ArrayList`, `HashMap` for managing bikes and catalogs  
- **Java Logging API** – `java.util.logging.Logger` for logging and auditing  
- **File I/O** – persistent audit log storage  

**Design Patterns and OOP Principles:**

1. **Builder Pattern** – `BikeBuilder` ensures safe and flexible construction of bikes with optional features (lights, GPS, basket).  
2. **Factory Pattern** – `BikeFactory` and its subclasses (`MountainBikeFactory`, `ElectricBikeFactory`, etc.) encapsulate object creation, enforcing type safety and shielding.  
3. **Iterator Pattern** – `BikeIterator` and `CatalogIterator` provide uniform traversal of bikes and catalogs without exposing underlying data structures.  
4. **Composite Pattern** – `BikeCatalog` and `BikeInventory` allow hierarchical management of bikes and catalogs while treating individual bikes and collections uniformly.  
5. **Exception Handling & Custom Exceptions** – Specific runtime exceptions (`BikeUnavailableException`, `InvalidBuilderException`, etc.) enforce business rules and improve error clarity.  
6. **Annotations for Logging, Sanitization, and Security** – `@Logged`, `@Sanitized`, `@Secured`, and `@RoleType` provide modular metadata to enhance readability and maintainability.  

**Justification:**

- **Builder + Factory** ensures clean separation between bike configuration and instantiation.  
- **Iterator** enables flexible bike and catalog traversal also without exposing internal collections
- **Custom Exceptions** improve user feedback and allow precise error handling.  
- **Logging & Auditing** ensures traceability and accountability for rental operations.  

---

## Testing

- Unit tests were implemented using **JUnit 5** and **Mockito**.
- Core components tested:
  - `BikeBuilder`, `BikeCatalog`, `BikeInventory`, `BikeRentalService`, `Bike`, `InputValidator`
  - Iterators: `BikeIterator` and `CatalogIterator`
  - Main application flow (`RentalApp`) with simulated user input using Mockito and `ByteArrayInputStream`
- Tests cover **valid/invalid inputs, exception handling, and audit logging simulation**.

### Test Suite
- All tests can be run at once via `AllTests.java`.
- Alternatively, run all tests using IntelliJ's "Run All Tests" or Maven/Gradle commands.

---

## Setup and Execution

**Prerequisites:**

- Java 17+ installed
- IDE or command line access

**Steps:**

1. **Open the project** in IntelliJ IDEA:
   - `File → Open → [Select project folder]`

2. **Configure the SDK** (if not already set):
   - `File → Project Structure → Project → Project SDK → Add JDK 17+`

3. **Compile the project**:
   - IntelliJ automatically compiles when you build/run
   - Or manually: `Build → Build Project` (Ctrl+F9 / Cmd+F9)

4. **Use the following snippet as a starting point**

    ```public static void main(String[] args) {
    BikeRentalService bikeRentalService = new BikeRentalService();

    ArrayList<Bike> mountainBikes = new ArrayList<>();
    MountainBikeCatalog mountainCatalog = new MountainBikeCatalog(mountainBikes);

    // Create a sample bike using the builder
    BikeBuilder builder = new BikeBuilder("123abc", "GT3", true)
                                .setGPS(true)
                                .setLights(true);

    bikeRentalService.bikeCreation(builder, mountainCatalog, BikeType.mountain);

    ArrayList<BikeCatalog> catalogs = new ArrayList<>();
    catalogs.add(mountainCatalog);
    BikeInventory inventory = new BikeInventory(catalogs);

    RentalApp app = new RentalApp(bikeRentalService, inventory);
    app.start();

Note: This main() is an example. You can add more bikes, catalogs, or customize the startup behavior according to your needs.

5. **Run the application**:
   - Open `RentalApp.java`
   - Right-click → `Run 'RentalApp.main()'`
   - Or click the green run button near the `main` method
   - The console will open for interactive menu prompts

    **Optional: Run via terminal inside IntelliJ**:
   ```bash
   javac -d out src/com/epicode/*.java
   java -cp out com.epicode.RentalApp

6. Follow the console prompts to rent or return bikes

**File Storage**
Audit logs are stored in:
```
- data/bikes.log
- data/rentals.log
```
Make sure the data/ folder exists and is writable.

---

## UML diagrams

### View-Only

**UML class diagram**
- ![UML class diagram](docs/bike-rental-class-diagram.drawio.svg)

**UML architectural (component) diagram**
- ![UML architectural diagram](docs/bike-rental-architectural-diagram.drawio.svg)

### Editable

- [UML class diagram](docs/bike-rental-class-diagram.drawio)
- [UML architectural diagram](docs/bike-rental-architectural-diagram.drawio)

---

## Known Limitations

- No **graphical user interface**; interaction is console-based.  
- Audit logs are plain text, **not a database**, so querying and reporting are limited.  
- No **user authentication** or account management.  
- Concurrent access is **not fully synchronized**; multiple users cannot safely use the system simultaneously.  
- Bike availability is managed **in-memory**; the system state resets on application restart unless persisted manually.  

---

## Future Work

- Add a **GUI** or web interface for enhanced user experience.  
- Integrate a **database** for persistent storage of bikes, catalogs, and rentals.  
- Implement **user accounts** with authentication and rental history.  
- Add **multi-threading support** for simultaneous rentals.  
- Implement **reporting and analytics** for bike usage and rental patterns.  
- Add dynamic pricing, and notifications.  
