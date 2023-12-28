package rodriguezgonzalez.model;

public class Recommendation {
    private String acronym;
    private String checkIn;
    private String checkOut;
    private String hotelName;
    private String website;
    private int pricePerNight;
    private double temp;
    private double pop;

    public Recommendation(String acronym, String checkIn, String checkOut, String hotelName, String website, int pricePerNight, double temp, double pop) {
        this.acronym = acronym;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.hotelName = hotelName;
        this.website = website;
        this.pricePerNight = pricePerNight;
        this.temp = temp;
        this.pop = pop;
    }

    public String getAcronym() {
        return acronym;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public String getHotelName() {
        return hotelName;
    }

    public String getWebsite() {
        return website;
    }

    public int getPricePerNight() {
        return pricePerNight;
    }

    public double getTemp() {
        return temp;
    }

    public double getPop() {
        return pop;
    }
}
