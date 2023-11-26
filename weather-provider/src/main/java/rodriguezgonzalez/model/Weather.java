package rodriguezgonzalez.model;

import java.time.Instant;

public class Weather {
    private Instant ts;
    private Instant predictionTime;
    private double pop;
    private double windSpeed;
    private double temp;
    private int humidity;
    private int clouds;
    private Location location;

    public Weather(Instant ts, Instant predictionTime, double pop, double windSpeed, double temp, int humidity, int clouds, Location location) {
        this.ts = ts;
        this.predictionTime = predictionTime;
        this.pop = pop;
        this.windSpeed = windSpeed;
        this.temp = temp;
        this.humidity = humidity;
        this.clouds = clouds;
        this.location = location;
    }

    public Instant getTs() {
        return ts;
    }

    public Instant getPredictionTime() {
        return predictionTime;
    }

    public double getPop() {
        return pop;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getTemp() {
        return temp;
    }

    public int getHumidity() {
        return humidity;
    }

    public Location getLocation() {
        return location;
    }

    public int getClouds() {
        return clouds;
    }
}
