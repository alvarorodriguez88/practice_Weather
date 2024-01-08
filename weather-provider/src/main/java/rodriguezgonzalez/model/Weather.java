package rodriguezgonzalez.model;

import java.time.Instant;

public class Weather {
    private final Instant ts;
    private final Instant predictionTime;
    private final String ss;
    private final double pop;
    private final double windSpeed;
    private final double temp;
    private final int humidity;
    private final int clouds;
    private final Location location;
    private final String weatherCondition;

    public Weather(Instant predictionTime, double pop, double windSpeed, double temp, int humidity, int clouds, Location location, String weatherCondition) {
        this.ts = Instant.now();
        this.predictionTime = predictionTime;
        this.ss = "weather-provider";
        this.pop = pop;
        this.windSpeed = windSpeed;
        this.temp = temp;
        this.humidity = humidity;
        this.clouds = clouds;
        this.location = location;
        this.weatherCondition = weatherCondition;
    }
}
