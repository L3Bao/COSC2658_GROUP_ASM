package place;

import comparable.*;

public class Place implements SpatialComparable<Place> {
    private float x; // Using float instead of double
    private float y; // Using float instead of double
    private int serviceBitmask; // Using int directly for services, assuming bitmasking fits in int

    public Place(float x, float y, int serviceBitmask) {
        this.x = x;
        this.y = y;
        this.serviceBitmask = serviceBitmask;
    }

    @Override
    public int compareTo(Place other) {
        int result = Float.compare(this.x, other.x);
        if (result == 0) {
            result = Float.compare(this.y, other.y);
        }
        return result;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getServiceBitmask() {
        return serviceBitmask;
    }

    public void setServiceBitmask(int serviceBitmask) {
        this.serviceBitmask = serviceBitmask;
    }

    // This method now uses the bitmask to check service presence
    public String getServiceTypeNames() {
        StringBuilder sb = new StringBuilder();
        int bitmask = this.serviceBitmask;
        int index = 0;
        while (bitmask != 0) {
            if ((bitmask & 1) == 1) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(ServiceRegistry.getServiceType(index));  // Fetch the service name using the index
            }
            bitmask >>>= 1;
            index++;
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
        return "Place{x=" + x + ", y=" + y + ", services=" + getServiceTypeNames() + "}";
    }
}
