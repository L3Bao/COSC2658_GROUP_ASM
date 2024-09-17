package place;
import arrayList.ArrayList; // Assuming this is correctly importing your custom ArrayList

public class Place {
    double x;
    double y;
    ArrayList<Integer> serviceTypeIndexes; // Store indexes of the service types

    // Static list to hold all possible service types
    static ArrayList<String> allServiceTypes = new ArrayList<>();

    public Place(double x, double y, String serviceTypes) {
        this.x = x;
        this.y = y;
        this.serviceTypeIndexes = new ArrayList<Integer>();
        
        // Split and process multiple service types separated by commas
        for (String serviceType : serviceTypes.split(",")) {
            addServiceType(serviceType.trim());
        }
    }

    

    public static int getServiceTypeIndex(String serviceType) {
        // Check if the service type already exists in the list
        for (int i = 0; i < allServiceTypes.size(); i++) {
            if (allServiceTypes.get(i).equals(serviceType)) {
                return i; // Return existing index
            }
        }
        // If not found, add it to the list
        allServiceTypes.insertAt(allServiceTypes.size(), serviceType);
        return allServiceTypes.size() - 1; // Return new index
    }

    public void addServiceType(String serviceType) {
        int index = getServiceTypeIndex(serviceType);
        serviceTypeIndexes.insertAt(serviceTypeIndexes.size(), index);
    }

    public String getServiceTypeNames() {
        StringBuilder result = new StringBuilder();
        // Use traditional for loop for compatibility with custom ArrayList
        for (int i = 0; i < serviceTypeIndexes.size(); i++) {
            int index = serviceTypeIndexes.get(i); // Access by index
            result.append(allServiceTypes.get(index)); // Get the actual service type name using the index
            if (i < serviceTypeIndexes.size() - 1) {
                result.append(", ");
            }
        }
        return result.toString();
    }    

    @Override
    public String toString() {
        return "Place{x=" + x + ", y=" + y + ", serviceTypes=[" + getServiceTypeNames() + "]}";
    }

    public static void main(String[] args) {
        Place place1 = new Place(1.0, 1.0, "Donut shop, Coffee shop");
        System.out.println(place1);
        place1.addServiceType("Bakery");
        System.out.println(place1);
        place1.addServiceType("Coffee shop"); // Test adding an existing service type
        System.out.println(place1);
    }



    public double getX() {
        return x;
    }



    public void setX(double x) {
        this.x = x;
    }



    public double getY() {
        return y;
    }



    public void setY(double y) {
        this.y = y;
    }



    public ArrayList<Integer> getServiceTypeIndexes() {
        return serviceTypeIndexes;
    }



    public void setServiceTypeIndexes(ArrayList<Integer> serviceTypeIndexes) {
        this.serviceTypeIndexes = serviceTypeIndexes;
    }



    public static ArrayList<String> getAllServiceTypes() {
        return allServiceTypes;
    }



    public static void setAllServiceTypes(ArrayList<String> allServiceTypes) {
        Place.allServiceTypes = allServiceTypes;
    }
}
