package place;
import arrayList.ArrayList; // Assuming this is correctly importing your custom ArrayList

public class Place {
    double x;
    double y;
    int serviceTypeIndexes; // Store indexes of the service types

    // Static list to hold all possible service types
    static ArrayList<String> allServiceTypes = new ArrayList<>(1024);

    public Place(double x, double y, int serviceTypeIndexes) {
        this.x = x;
        this.y = y;
        this.serviceTypeIndexes = serviceTypeIndexes;
    }

    

    public static int getServicesTypesIndex(String servicesTypes) {
        // Check if the service type already exists in the list
        for (int i = 0; i < allServiceTypes.size(); i++) {
            if (allServiceTypes.get(i).equals(servicesTypes)) {
                return i; // Return existing index
            }
        }
        // If not found, return -1
        return -1;
    }

    public void addServiceType(String serviceType) {
        String currentServices = allServiceTypes.get(serviceTypeIndexes);
        System.out.println(currentServices);
        if (currentServices.contains(serviceType)) {
            System.out.println("The place already have the inputted service!");
            return;
        }else if (currentServices == ""){
            currentServices += serviceType;
        } else if (currentServices != ""){
            currentServices += ", " + serviceType;
        }
        System.out.println(currentServices);
        for (int i = 0; i < allServiceTypes.size(); i++) {
            //if found the string with the correct services types, change the service type index
            if (allServiceTypes.get(i).contains(currentServices)) {
                this.serviceTypeIndexes = i;
            }
        }
    }

    public String getServiceTypeNames() {
        return allServiceTypes.get(serviceTypeIndexes);
    }

    @Override
    public String toString() {
        return "Place{x=" + x + ", y=" + y + ", serviceTypes=[" + getServiceTypeNames() + "]}";
    }

    public static void main(String[] args) {
        String[] serviceTypes = {"Cafe", "Restaurant", "Gas Station", "Library", "Hospital", "School", "Store", "Park", "Hotel", "Gym"};

        Place.setAllServiceTypes(ArrayList.generateStringArrayPowerSet(serviceTypes));

        Place place1 = new Place(1.0, 1.0, 3);
        System.out.println(place1);
        place1.addServiceType("Gym");
        System.out.println(place1);
        place1.addServiceType("Restaurant"); // Test adding an existing service type
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



    public int getServiceTypeIndexes() {
        return serviceTypeIndexes;
    }



    public void setServiceTypeIndexes(int serviceTypeIndex) {
        this.serviceTypeIndexes = serviceTypeIndex;
    }



    public static ArrayList<String> getAllServiceTypes() {
        return allServiceTypes;
    }



    public static void setAllServiceTypes(ArrayList<String> allServiceTypes) {
        Place.allServiceTypes = allServiceTypes;
    }
}
