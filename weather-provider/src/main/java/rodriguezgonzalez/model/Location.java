package rodriguezgonzalez.model;

import java.io.Serializable;

public class Location implements Serializable {
    private double lat;
    private double lon;
    private String place;

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
