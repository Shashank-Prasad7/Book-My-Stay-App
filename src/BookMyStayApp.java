import java.io.*;
import java.util.*;

// ---------------- Custom Exceptions ----------------
class BookingException extends Exception {
    public BookingException(String message) {
        super(message);
    }
}

// ---------------- Booking Model ----------------
class Booking implements Serializable {
    private static final long serialVersionUID = 1L;

    String bookingId;
    String user;
    String roomType;
    int quantity;

    public Booking(String bookingId, String user, String roomType, int quantity) {
        this.bookingId = bookingId;
        this.user = user;
        this.roomType = roomType;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return bookingId + " | " + user + " | " + roomType + " | " + quantity;
    }
}

// ---------------- Booking System with Persistence ----------------
class PersistentBookingSystem implements Serializable {
    private static final long serialVersionUID = 1L;

    private Map<String, Integer> inventory = new HashMap<>();
    private Map<String, Booking> bookings = new HashMap<>();
    private int bookingCounter = 1;

    private static final String FILE_NAME = "booking_state.dat";

    public PersistentBookingSystem() {
        // Default inventory if no saved state
        inventory.put("single", 5);
        inventory.put("double", 3);
        inventory.put("suite", 2);
    }

    // ---------------- Booking Logic ----------------
    public String bookRoom(String user, String roomType, int quantity) throws BookingException {
        if (!inventory.containsKey(roomType))
            throw new BookingException("Invalid room type: " + roomType);
        if (quantity <= 0)
            throw new BookingException("Quantity must be greater than zero");
        if (inventory.get(roomType) < quantity)
            throw new BookingException("Not enough " + roomType + " rooms available");

        // Reduce inventory
        inventory.put(roomType, inventory.get(roomType) - quantity);

        String bookingId = "B" + bookingCounter++;
        Booking booking = new Booking(bookingId, user, roomType, quantity);
        bookings.put(bookingId, booking);

        return "Booking successful! ID: " + bookingId;
    }

    // ---------------- Display ----------------
    public void showInventory() {
        System.out.println("\nCurrent Inventory:");
        inventory.forEach((k, v) -> System.out.println(k + ": " + v));
    }

    public void showBookings() {
        System.out.println("\nCurrent Bookings:");
        if (bookings.isEmpty()) {
            System.out.println("No bookings yet.");
            return;
        }
        bookings.values().forEach(System.out::println);
    }

    // ---------------- Persistence ----------------
    public void saveState() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(this);
            System.out.println("System state saved successfully!");
        } catch (IOException e) {
            System.out.println("Failed to save state: " + e.getMessage());
        }
    }

    public static PersistentBookingSystem loadState() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("No saved state found. Starting fresh.");
            return new PersistentBookingSystem();
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            PersistentBookingSystem system = (PersistentBookingSystem) in.readObject();
            System.out.println("System state restored from file!");
            return system;
        } catch (Exception e) {
            System.out.println("Failed to load state: " + e.getMessage());
            System.out.println("Starting fresh system...");
            return new PersistentBookingSystem();
        }
    }
}

// ---------------- Main ----------------
public class BookMyStayApp {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        // Load persisted state if exists
        PersistentBookingSystem system = PersistentBookingSystem.loadState();

        while (true) {
            System.out.println("\n1. Book Room");
            System.out.println("2. Show Inventory");
            System.out.println("3. Show Bookings");
            System.out.println("4. Save & Exit");

            System.out.print("Choose option: ");
            String choice = sc.nextLine();

            try {
                switch (choice) {
                    case "1":
                        System.out.print("Enter your name: ");
                        String user = sc.nextLine();
                        System.out.print("Room type (single/double/suite): ");
                        String roomType = sc.nextLine().toLowerCase();
                        System.out.print("Quantity: ");
                        int qty = Integer.parseInt(sc.nextLine());

                        String result = system.bookRoom(user, roomType, qty);
                        System.out.println(result);
                        break;

                    case "2":
                        system.showInventory();
                        break;

                    case "3":
                        system.showBookings();
                        break;

                    case "4":
                        system.saveState();
                        System.out.println("Exiting...");
                        sc.close();
                        return;

                    default:
                        System.out.println("Invalid choice!");
                }
            } catch (BookingException e) {
                System.out.println("Booking failed: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity input!");
            }
        }
    }
}