package rodriguezgonzalez.model;

import java.io.Serializable;

public class Location implements Serializable {
    private final double lat;
    private final double lon;
    private final String place;

    public Location(double lat, double lon, String place) {
        this.lat = lat;
        this.lon = lon;
        this.place = place;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}
