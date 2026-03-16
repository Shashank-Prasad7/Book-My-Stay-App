/**
 * HotelBookingApplication
 *
 * This class represents the entry point of the Hotel Booking System.
 * It is responsible for starting the application and displaying
 * an initial welcome message to the user.
 *
 * The JVM begins execution from the main() method defined in this class.
 *
 * @author YourName
 * @version 1.0
 */
public class BookMyStayApp {

    /**
     * The main method is the starting point of the Java application.
     * The JVM invokes this method when the program is executed.
     *
     * @param args command-line arguments passed to the application
     */
    public static void main(String[] args) {

        // Application Name and Version
        String appName = "Hotel Booking System";
        String version = "v1.0";

        // Display welcome message
        System.out.println("Welcome to the " + appName);
        System.out.println("Application Version: " + version);
        System.out.println("Application started successfully.");

    }
}