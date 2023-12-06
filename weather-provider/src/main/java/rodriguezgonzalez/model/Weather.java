package rodriguezgonzalez.model;

import java.time.Instant;

public class Weather {
    private Instant ts;
    private Instant predictionTime;
    private String ss;
    private double pop;
    private double windSpeed;
    private double temp;
    private int humidity;
    private int clouds;
    private Location location;

    public Weather(Instant predictionTime, double pop, double windSpeed, double temp, int humidity, int clouds, Location location) {
        this.ts = Instant.now();
        this.predictionTime = predictionTime;
        this.ss = "weather-provider";
        this.pop = pop;
        this.windSpeed = windSpeed;
        this.temp = temp;
        this.humidity = humidity;
        this.clouds = clouds;
        this.location = location;
    }
}
