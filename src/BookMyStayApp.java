// HotelRoomSystem.java

abstract class Room {
    protected String type;
    protected int beds;
    protected double sizeSqMeters;
    protected double pricePerNight;

    public Room(String type, int beds, double sizeSqMeters, double pricePerNight) {
        this.type = type;
        this.beds = beds;
        this.sizeSqMeters = sizeSqMeters;
        this.pricePerNight = pricePerNight;
    }

    public String getDescription() {
        return String.format("%s Room (%d bed%s) - %.1f m² - $%.2f/night",
                type, beds, beds == 1 ? "" : "s", sizeSqMeters, pricePerNight);
    }

    // Abstract method - forces concrete classes to implement if needed
    public abstract String getSpecialFeatures();
}

class SingleRoom extends Room {
    public SingleRoom() {
        super("Single", 1, 18.5, 89.99);
    }

    @Override
    public String getSpecialFeatures() {
        return "Compact design, ideal for solo travelers";
    }
}

class DoubleRoom extends Room {
    public DoubleRoom() {
        super("Double", 2, 28.0, 139.50);
    }

    @Override
    public String getSpecialFeatures() {
        return "Queen-size bed option, work desk included";
    }
}

class SuiteRoom extends Room {
    public SuiteRoom() {
        super("Suite", 2, 52.0, 249.00);
    }

    @Override
    public String getSpecialFeatures() {
        return "Separate living area, bathtub, panoramic view";
    }
}

public class BookMyStayApp {
    // Simple availability tracking (intentionally not using data structures yet)
    private static int singleRoomsAvailable = 12;
    private static int doubleRoomsAvailable = 8;
    private static int suiteRoomsAvailable = 3;

    public static void main(String[] args) {
        System.out.println("=== Welcome to Mini Hotel Booking System ===\n");

        // Create room objects (domain objects)
        Room single = new SingleRoom();
        Room doubleRm = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Display room catalog
        System.out.println("Available Room Types:");
        System.out.println("---------------------");

        printRoomInfo(single, singleRoomsAvailable);
        printRoomInfo(doubleRm, doubleRoomsAvailable);
        printRoomInfo(suite, suiteRoomsAvailable);

        System.out.println("\nThank you for using our system. Goodbye!");
    }

    private static void printRoomInfo(Room room, int availableCount) {
        System.out.println(room.getDescription());
        System.out.println("  → " + room.getSpecialFeatures());
        System.out.println("  → Currently available: " + availableCount + " room" +
                (availableCount == 1 ? "" : "s"));
        System.out.println();
    }
}