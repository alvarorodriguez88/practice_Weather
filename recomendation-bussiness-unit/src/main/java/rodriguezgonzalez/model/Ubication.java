package rodriguezgonzalez.model;

public class Ubication {
    private final String acronym;
    private final double temp;
    private final double pop;
    private final String weatherCondition;

    public Ubication(String acronym, double temp, double pop, String weatherCondition) {
        this.acronym = acronym;
        this.temp = temp;
        this.pop = pop;
        this.weatherCondition = weatherCondition;
    }

    public String getAcronym() {
        return acronym;
    }

    public double getTemp() {
        return temp;
    }

    public double getPop() {
        return pop;
    }

    public String getWeatherCondition() {
        return weatherCondition;
    }
}
