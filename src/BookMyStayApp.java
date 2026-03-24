import java.util.*;

// ---------------- Booking Request ----------------
class BookingRequest {
    String user;
    String roomType;
    int quantity;

    public BookingRequest(String user, String roomType, int quantity) {
        this.user = user;
        this.roomType = roomType;
        this.quantity = quantity;
    }
}

// ---------------- Shared System ----------------
class ConcurrentBookingSystem {

    private Map<String, Integer> inventory = new HashMap<>();
    private Queue<BookingRequest> queue = new LinkedList<>();

    public ConcurrentBookingSystem() {
        inventory.put("single", 5);
        inventory.put("double", 3);
        inventory.put("suite", 2);
    }

    // Add request (thread-safe)
    public synchronized void addRequest(BookingRequest req) {
        queue.add(req);
    }

    // Get request (thread-safe)
    public synchronized BookingRequest getRequest() {
        return queue.poll();
    }

    // Critical section
    public void processBooking(BookingRequest req) {
        if (req == null) return;

        synchronized (this) {
            System.out.println(Thread.currentThread().getName() +
                    " processing " + req.user);

            int available = inventory.getOrDefault(req.roomType, 0);

            try { Thread.sleep(100); } catch (Exception e) {}

            if (available >= req.quantity) {
                inventory.put(req.roomType, available - req.quantity);

                System.out.println("✅ SUCCESS: " + req.user +
                        " booked " + req.quantity + " " + req.roomType);
            } else {
                System.out.println("❌ FAILED: " + req.user +
                        " (Not enough " + req.roomType + ")");
            }
        }
    }

    public void showInventory() {
        System.out.println("\nFinal Inventory: " + inventory);
    }
}

// ---------------- Thread ----------------
class BookingProcessor extends Thread {

    private ConcurrentBookingSystem system;

    public BookingProcessor(ConcurrentBookingSystem system, String name) {
        super(name);
        this.system = system;
    }

    public void run() {
        while (true) {
            BookingRequest req = system.getRequest();
            if (req == null) break;
            system.processBooking(req);
        }
    }
}

// ---------------- Main ----------------
public class BookMyStayApp {
    public static void main(String[] args) throws Exception {

        Scanner sc = new Scanner(System.in);
        ConcurrentBookingSystem system = new ConcurrentBookingSystem();

        // -------- USER INPUT --------
        System.out.print("Enter number of booking requests: ");
        int n = Integer.parseInt(sc.nextLine());

        for (int i = 1; i <= n; i++) {
            System.out.println("\nRequest " + i);

            System.out.print("User name: ");
            String user = sc.nextLine();

            System.out.print("Room type (single/double/suite): ");
            String type = sc.nextLine().toLowerCase();

            System.out.print("Quantity: ");
            int qty = Integer.parseInt(sc.nextLine());

            system.addRequest(new BookingRequest(user, type, qty));
        }

        // -------- MULTIPLE THREADS --------
        BookingProcessor t1 = new BookingProcessor(system, "Thread-1");
        BookingProcessor t2 = new BookingProcessor(system, "Thread-2");
        BookingProcessor t3 = new BookingProcessor(system, "Thread-3");

        t1.start();
        t2.start();
        t3.start();

        t1.join();
        t2.join();
        t3.join();

        system.showInventory();
    }
}