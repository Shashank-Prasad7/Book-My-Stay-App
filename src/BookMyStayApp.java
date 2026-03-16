import java.util.*;

// Reservation - represents guest booking intent
class Reservation {
    private String guestName;
    private String roomType;
    private int nights;

    public Reservation(String guestName, String roomType, int nights) {
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNights() {
        return nights;
    }

    public void displayRequest() {
        System.out.println("Guest: " + guestName +
                " | Room Type: " + roomType +
                " | Nights: " + nights);
    }
}

// Booking Request Queue
class BookingRequestQueue {

    // Queue maintains FIFO order
    private Queue<Reservation> requestQueue = new LinkedList<>();

    // Accept booking request
    public void addRequest(Reservation reservation) {
        requestQueue.offer(reservation);
        System.out.println("Booking request added for " + reservation.getGuestName());
    }

    // View next request (without removing)
    public Reservation peekNextRequest() {
        return requestQueue.peek();
    }

    // Retrieve next request for processing
    public Reservation getNextRequest() {
        return requestQueue.poll();
    }

    // Display all queued requests
    public void displayQueue() {
        if (requestQueue.isEmpty()) {
            System.out.println("No booking requests in queue.");
            return;
        }

        System.out.println("\nBooking Requests in Arrival Order:");
        for (Reservation r : requestQueue) {
            r.displayRequest();
        }
    }
}

// Guest actor
class Guest {
    private String name;

    public Guest(String name) {
        this.name = name;
    }

    public void submitBooking(String roomType, int nights, BookingRequestQueue queue) {
        Reservation reservation = new Reservation(name, roomType, nights);
        queue.addRequest(reservation);
    }
}

// Main Program
public class BookMyStayApp {

    public static void main(String[] args) {

        // Booking request queue
        BookingRequestQueue queue = new BookingRequestQueue();

        // Guests
        Guest guest1 = new Guest("Alice");
        Guest guest2 = new Guest("Bob");
        Guest guest3 = new Guest("Charlie");

        // Guests submit booking requests
        guest1.submitBooking("Standard", 2, queue);
        guest2.submitBooking("Deluxe", 3, queue);
        guest3.submitBooking("Suite", 1, queue);

        // Display queue (FIFO order preserved)
        queue.displayQueue();

        // Show next request to be processed
        System.out.println("\nNext request to process:");
        Reservation next = queue.peekNextRequest();
        if (next != null) {
            next.displayRequest();
        }

        // NOTE: No inventory mutation or room allocation occurs here.
        // The queue only collects and orders booking requests.
    }
}