package rodriguezgonzalez.model;

public class Hotel {
    private String website;
    private String currency;
    private String checkIn;
    private String checkOut;
    private int rate;
    private HotelInfo hotelInfo;

    public Hotel(String website, String currency, String checkIn, String checkOut, int rate, HotelInfo hotelInfo) {
        this.website = website;
        this.currency = currency;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.rate = rate;
        this.hotelInfo = hotelInfo;
    }

    public String getWebsite() {
        return website;
    }

    public String getCurrency() {
        return currency;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public int getRate() {
        return rate;
    }

    public HotelInfo getHotelInfo() {
        return hotelInfo;
    }
}
