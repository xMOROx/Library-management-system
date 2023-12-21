package pl.edu.agh.managementlibrarysystem.enums;

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

    public static BookAvailability fromString(String availability) {
        return availability.equals("Available") ? AVAILABLE : UNAVAILABLE;
    }

    public static Boolean fromStringToBoolean(String availability) {
        return availability.equals("Available");
    }
}
