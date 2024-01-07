package rodriguezgonzalez.model;

public class HotelInfo {
    private String name;
    private String hotelKey;
    private String acronym;

    public HotelInfo(String name, String hotelKey, String acronym) {
        this.name = name;
        this.hotelKey = hotelKey;
        this.acronym = acronym;
    }

    public String getName() {
        return name;
    }

    public String getHotelKey() {
        return hotelKey;
    }

    public String getAcronym() {
        return acronym;
    }
}
