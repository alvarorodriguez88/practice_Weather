package rodriguezgonzalez.model;

import java.io.Serializable;

public class Location implements Serializable {
    private double lat;
    private double lon;
    private String isla;

    public Location(double lat, double lon, String isla) {
        this.lat = lat;
        this.lon = lon;
        this.isla = isla;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getIsla() {
        return isla;
    }
}
