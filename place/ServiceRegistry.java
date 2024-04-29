package place;

public class ServiceRegistry {

    // Static array containing all service types
    private static final String[] serviceTypes = {
            "Cafe", "Restaurant", "Gas Station", "Library", "Hospital",
            "School", "Store", "Park", "Hotel", "Gym"
    };

    // Method to get the entire list of service types (avoiding collections)
    public static String[] getServiceTypes() {
        return serviceTypes.clone(); // Return a copy to avoid modification
    }

    // Method to get a specific service type by index
    public String getServiceType(int index) {
        if (index >= 0 && index < serviceTypes.length) {
            return serviceTypes[index];
        } else {
            throw new IndexOutOfBoundsException("Invalid service type index: " + index);
        }
    }

    // Method to find the index of a service type by name
    public static int getServiceTypeIndex(String serviceName) {
        for (int i = 0; i < serviceTypes.length; i++) {
            if (serviceTypes[i].equalsIgnoreCase(serviceName)) { 
                return i;
           }
        }
        throw new IllegalArgumentException("Service type not found: " + serviceName);
     }
}