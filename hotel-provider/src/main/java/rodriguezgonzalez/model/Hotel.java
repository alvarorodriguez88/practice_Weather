package rodriguezgonzalez.model;

import java.time.Instant;

public class Hotel {
    private Instant ts;
    private String ss;
    private String website;
    private String currency;
    private String checkIn;
    private String checkOut;
    private int rate;
    private HotelInfo hotelInfo;

    public Hotel(String website, String currency, String checkIn, String checkOut, int rate, HotelInfo hotelInfo) {
        this.ts = Instant.now();
        this.website = website;
        this.ss = "hotel-provider";
        this.currency = currency;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.rate = rate;
        this.hotelInfo = hotelInfo;
    }
}
