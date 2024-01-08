package rodriguezgonzalez.model;

public class Lodging {
    private final String acronym;
    private final String checkIn;
    private final String checkOut;
    private final String hotelName;
    private final String website;
    private double price;
    private final String currency;

    public Lodging(String acronym, String checkIn, String checkOut, String hotelName, String website, double price, String currency) {
        this.acronym = acronym;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.hotelName = hotelName;
        this.website = website;
        this.price = price;
        this.currency = currency;
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

    public double getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }
}

