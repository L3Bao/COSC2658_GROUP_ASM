public class Place {
    double x;
    double y;
    String serviceType;

    public Place() {
        this.x = 0;
        this.y = 0;
        this.serviceType = "";
    }

    public Place(double x, double y, String serviceType) {
        this.x = x;
        this.y = y;
        this.serviceType = serviceType;
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

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public void updateServiceType(String serviceType) {
        setServiceType(serviceType);
        System.out.println("New Service Type: " + serviceType);
    }


    @Override
    public String toString() {
        return "Place{" +
                "x=" + x +
                ", y=" + y +
                ", serviceType='" + serviceType + '\'' +
                '}';
    }

    public static void main(String[] args) {
        Place place1 = new Place(1,1,"Donut shop");
        System.out.println(place1.toString());
        place1.updateServiceType("Pee");
        System.out.println(place1.toString());
    }

}
