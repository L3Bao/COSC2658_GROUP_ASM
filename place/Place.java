package place;

public class Place {
    private double x;
    private double y;
    private int serviceBitmask;  // Use a bitmask to store service presence

    public Place(double x, double y, int serviceBitmask) {
        this.x = x;
        this.y = y;
        this.serviceBitmask = serviceBitmask;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getServiceBitmask() {
        return serviceBitmask;
    }

    // This method now uses the bitmask to check service presence
    public String getServiceTypeNames(ServiceRegistry registry) {
        StringBuilder sb = new StringBuilder();
        int serviceIndex = 0;
        int bitmask = this.serviceBitmask;
        while (bitmask != 0) {
            if ((bitmask & 1) == 1) { // Check the lowest bit
                if (sb.length() > 0) sb.append(", ");
                sb.append(registry.getServiceType(serviceIndex));
            }
            bitmask >>>= 1; // Shift right and fill with zero
            serviceIndex++;
        }
        return sb.toString();
    }

    // To toggle a service
    public void toggleService(int serviceIndex, boolean enable) {
        if (enable) {
            serviceBitmask |= (1 << serviceIndex); // Set bit to 1
        } else {
            serviceBitmask &= ~(1 << serviceIndex); // Set bit to 0
        }
    }

    // Check if a specific service is enabled
    public boolean isServiceEnabled(int serviceIndex) {
        return (serviceBitmask & (1 << serviceIndex)) != 0;
    }

    @Override
    public String toString() {
        return "Place{x=" + x + ", y=" + y + ", serviceBitmask=" + Integer.toBinaryString(serviceBitmask) + "}";
    }
}
