package place;
import arrayList.ArrayList; // Correct import of your custom ArrayList class

public class Place {
    double x;
    double y;
    ArrayList<String> serviceTypes;

    public Place() {
        this.x = 0.0;
        this.y = 0.0;
        // this.serviceTypes = new ArrayList<String>();
    }

    public Place(double x, double y, String serviceType) {
        this.x = x;
        this.y = y;
        this.serviceTypes = new ArrayList<String>();
        String[] tempServiceTypes = serviceType.split(",");
        for (int i = 0; i < tempServiceTypes.length; i++){
            this.serviceTypes.insertAt(i, tempServiceTypes[i].trim()); // Correct use of insertAt
        }

    }

    public double getX() {
        return this.x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return this.y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public ArrayList<String> getServiceTypes() {
        return this.serviceTypes;
    }

    public void addServiceType(String serviceType) {
        this.serviceTypes.insertAt(this.serviceTypes.size(), serviceType); // Insert at the end
    }

    public void updateServiceType(int index, String serviceType) {
        if (index >= 0 && index < this.serviceTypes.size()) {
            this.serviceTypes.replaceAt(index, serviceType); // Use replaceAt
            System.out.println("Service Type Updated: " + serviceType);
        } else {
            System.out.println("Invalid index for service types.");
        }
    }

    public void removeAt(int index) {
        serviceTypes.removeAt(index);
    }

    public void remove(String serviceType) {
        serviceTypes.remove(serviceType);
    }

    public String toString() {
        StringBuilder result = new StringBuilder("Place{x=" + this.x + ", y=" + this.y + ", serviceTypes=[");
        for (int i = 0; i < serviceTypes.size(); i++) {
            result.append(serviceTypes.get(i));
            if (i < serviceTypes.size() - 1) result.append(", ");
        }
        result.append("]}");
        return result.toString();
    }

    public static void main(String[] args) {
        Place place1 = new Place(1.0, 1.0, "Donut shop");
        System.out.println(place1);
        place1.addServiceType("Coffee shop");
        System.out.println(place1);
        place1.updateServiceType(1, "Bakery");
        System.out.println(place1);
        place1.remove("Bakery");
        System.out.println(place1);
    }
}
