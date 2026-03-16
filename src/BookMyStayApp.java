import java.util.*;

// Domain Model - Room
class Room {
    private String roomType;
    private double price;
    private List<String> amenities;

    public Room(String roomType, double price, List<String> amenities) {
        this.roomType = roomType;
        this.price = price;
        this.amenities = amenities;
    }

    public String getRoomType() {
        return roomType;
    }

    public double getPrice() {
        return price;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void displayDetails() {
        System.out.println("Room Type: " + roomType);
        System.out.println("Price: $" + price);
        System.out.println("Amenities: " + amenities);
        System.out.println("-----------------------------");
    }
}

// Inventory as State Holder
class Inventory {
    private Map<String, Integer> availability = new HashMap<>();

    public void addRoom(String roomType, int count) {
        availability.put(roomType, count);
    }

    // Read-only access
    public int getAvailability(String roomType) {
        return availability.getOrDefault(roomType, 0);
    }

    public Map<String, Integer> getAllAvailability() {
        return Collections.unmodifiableMap(availability); // defensive programming
    }
}

// Search Service (Read-only operations)
class SearchService {

    public List<Room> searchAvailableRooms(List<Room> rooms, Inventory inventory) {
        List<Room> availableRooms = new ArrayList<>();

        for (Room room : rooms) {
            int count = inventory.getAvailability(room.getRoomType());

            // Validation Logic: filter unavailable rooms
            if (count > 0) {
                availableRooms.add(room);
            }
        }

        return availableRooms;
    }
}

// Guest Actor
class Guest {
    public void searchRooms(SearchService service, List<Room> rooms, Inventory inventory) {
        System.out.println("Guest searching for available rooms...\n");

        List<Room> availableRooms = service.searchAvailableRooms(rooms, inventory);

        if (availableRooms.isEmpty()) {
            System.out.println("No rooms available.");
        } else {
            for (Room room : availableRooms) {
                room.displayDetails();
            }
        }
    }
}

// Main Program
public class BookMyStayApp {

    public static void main(String[] args) {

        // Room objects (Domain Model)
        Room standard = new Room(
                "Standard",
                100,
                Arrays.asList("WiFi", "TV", "Air Conditioning")
        );

        Room deluxe = new Room(
                "Deluxe",
                180,
                Arrays.asList("WiFi", "TV", "Air Conditioning", "Mini Bar")
        );

        Room suite = new Room(
                "Suite",
                300,
                Arrays.asList("WiFi", "TV", "Air Conditioning", "Mini Bar", "Jacuzzi")
        );

        List<Room> rooms = Arrays.asList(standard, deluxe, suite);

        // Inventory (State Holder)
        Inventory inventory = new Inventory();
        inventory.addRoom("Standard", 5);
        inventory.addRoom("Deluxe", 0);   // unavailable
        inventory.addRoom("Suite", 2);

        // Search Service
        SearchService searchService = new SearchService();

        // Guest Actor
        Guest guest = new Guest();

        // Guest performs search (Read-only)
        guest.searchRooms(searchService, rooms, inventory);

        // Verify system state unchanged
        System.out.println("\nInventory after search (unchanged):");
        for (Map.Entry<String, Integer> entry : inventory.getAllAvailability().entrySet()) {
            System.out.println(entry.getKey() + " rooms available: " + entry.getValue());
        }
    }
}