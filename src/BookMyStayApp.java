// HotelRoomSystem with Centralized Inventory (Step 2)

import java.util.HashMap;
import java.util.Map;

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

    public String getType() {
        return type;
    }

    public String getDescription() {
        return String.format("%s Room (%d bed%s) - %.1f m² - $%.2f/night",
                type, beds, beds == 1 ? "" : "s", sizeSqMeters, pricePerNight);
    }

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

/**
 * Centralized inventory management using HashMap
 * Single source of truth for room availability
 */
class RoomInventory {
    // Key = room type (e.g. "Single", "Double", "Suite")
    // Value = number of rooms currently available
    private final Map<String, Integer> availability;

    public RoomInventory() {
        availability = new HashMap<>();

        // Initial stock - in real system this would come from database/config
        availability.put("Single", 12);
        availability.put("Double", 8);
        availability.put("Suite", 3);
    }

    /**
     * Get current available count for a room type
     * @return number available, or 0 if type doesn't exist
     */
    public int getAvailableCount(String roomType) {
        return availability.getOrDefault(roomType, 0);
    }

    /**
     * Attempt to reserve one room of the given type
     * @return true if reservation was successful (room was available)
     */
    public boolean reserveRoom(String roomType) {
        Integer current = availability.get(roomType);
        if (current != null && current > 0) {
            availability.put(roomType, current - 1);
            return true;
        }
        return false;
    }

    /**
     * Return/check-in one room (increase availability)
     */
    public void returnRoom(String roomType) {
        Integer current = availability.get(roomType);
        if (current != null) {
            availability.put(roomType, current + 1);
        } else {
            // In real system might throw exception or log error
            availability.put(roomType, 1);
        }
    }

    /**
     * Add a new room type or update initial stock
     */
    public void registerRoomType(String roomType, int initialCount) {
        availability.put(roomType, initialCount);
    }

    /**
     * Print current inventory status
     */
    public void printInventory() {
        System.out.println("Current Room Inventory:");
        System.out.println("------------------------");
        for (Map.Entry<String, Integer> entry : availability.entrySet()) {
            System.out.printf("%-12s : %d room%s available%n",
                    entry.getKey(), entry.getValue(), entry.getValue() == 1 ? "" : "s");
        }
        System.out.println();
    }
}

public class BookMyStayApp {

    public static void main(String[] args) {
        System.out.println("=== Hotel Booking System - Centralized Inventory ===\n");

        // Domain objects (what rooms ARE)
        Room single = new SingleRoom();
        Room doubleRm = new DoubleRoom();
        Room suite = new SuiteRoom();

        // Single source of truth for availability (how many we HAVE)
        RoomInventory inventory = new RoomInventory();

        // Show initial state
        inventory.printInventory();

        System.out.println("Room Catalog:");
        System.out.println("-------------");
        printRoomInfo(single, inventory);
        printRoomInfo(doubleRm, inventory);
        printRoomInfo(suite, inventory);

        // Demonstrate usage
        System.out.println("Simulating bookings...");
        boolean success1 = inventory.reserveRoom("Single");
        boolean success2 = inventory.reserveRoom("Suite");
        boolean success3 = inventory.reserveRoom("Family");   // doesn't exist

        System.out.println("Booking results:");
        System.out.println("  Single booked: " + success1);
        System.out.println("  Suite booked:  " + success2);
        System.out.println("  Family booked: " + success3 + " (type not registered)");

        // Show updated state
        System.out.println("\nAfter bookings:");
        inventory.printInventory();

        // Simulate check-in
        inventory.returnRoom("Single");
        System.out.println("After one Single check-in:");
        inventory.printInventory();

        System.out.println("Thank you for using the system!");
    }

    private static void printRoomInfo(Room room, RoomInventory inventory) {
        int available = inventory.getAvailableCount(room.getType());

        System.out.println(room.getDescription());
        System.out.println("  → " + room.getSpecialFeatures());
        System.out.println("  → Available now: " + available + " room" +
                (available == 1 ? "" : "s"));
        System.out.println();
    }
}