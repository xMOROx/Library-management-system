package pl.edu.agh.managementlibrarysystem.utils.enums;

public enum BookAvailability {
    AVAILABLE,
    UNAVAILABLE;

    public static BookAvailability fromBoolean(Boolean availability) {
        return availability ? AVAILABLE : UNAVAILABLE;
    }

    public static Boolean toBoolean(BookAvailability availability) {
        return availability == AVAILABLE;
    }

    public static String toString(BookAvailability availability) {
        return availability == AVAILABLE ? "Available" : "Unavailable";
    }

    public static Boolean fromStringToBoolean(String availability) {
        return availability.equals("Available");
    }
}
