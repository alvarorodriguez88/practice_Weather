package rodriguezgonzalez.model;

public class Ubication {
    private String acronym;
    private double temp;
    private double pop;

    public Ubication(String acronym, double temp, double pop) {
        this.acronym = acronym;
        this.temp = temp;
        this.pop = pop;
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
}