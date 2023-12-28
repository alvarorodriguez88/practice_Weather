package rodriguezgonzalez.model;

public class Lodging {
    private String acronym;
    private String checkIn;
    private String checkOut;
    private String hotelName;
    private String website;
    private int price;
    private String currency;

    public Lodging(String acronym, String checkIn, String checkOut, String hotelName, String website, int price, String currency) {
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

    public int getPrice() {
        return price;
    }

    public String getCurrency() {
        return currency;
    }
}

