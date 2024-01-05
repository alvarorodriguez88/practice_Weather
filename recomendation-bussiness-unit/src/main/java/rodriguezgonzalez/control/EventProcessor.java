package rodriguezgonzalez.control;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import rodriguezgonzalez.control.exceptions.StoreException;
import rodriguezgonzalez.model.Lodging;
import rodriguezgonzalez.model.Ubication;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class EventProcessor {
    private String weatherCondition;
    private int days;
    private ArrayList<Ubication> ubications;
    private ArrayList<Lodging> lodgings;
    private Set<String> filteredAcronyms;
    private String userCheckIn;

    public EventProcessor(String userCheckIn, String days, String weatherCondition) {
        this.weatherCondition = weatherCondition;
        this.days = Integer.parseInt(days);
        this.userCheckIn = userCheckIn;
        this.ubications = new ArrayList<>();
        this.lodgings = new ArrayList<>();
        this.filteredAcronyms = new HashSet<>();
    }

    public void processWeatherEvent(String json) throws StoreException {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        String weatherConditionString = jsonObject.get("weatherCondition").getAsString();
        if (weatherConditionString.replaceAll("\"", "").equals(weatherCondition)) {
            JsonObject jsonLocation = (JsonObject) jsonObject.get("location");
            String acronym = jsonLocation.get("place").getAsString();
            double temp = Double.parseDouble(jsonObject.get("temp").getAsString());
            double pop = Double.parseDouble(jsonObject.get("pop").getAsString());
            Ubication ubication = new Ubication(acronym, temp, pop);
            ubications.add(ubication);
            filteredAcronyms.add(acronym);
        }
    }

    public void processHotelEvent(String json) throws StoreException {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        JsonObject hotelInfo = (JsonObject) jsonObject.get("hotelInfo");
        String acronym = hotelInfo.get("acronym").toString().replaceAll("\"", "");
        if (filteredAcronyms.contains(acronym)) {
            String checkIn = jsonObject.get("checkIn").toString();
            String checkOut = jsonObject.get("checkOut").toString();
            String website = jsonObject.get("website").toString();
            double price = Double.parseDouble(jsonObject.get("rate").toString());
            String currency = jsonObject.get("currency").toString();
            String hotelName = hotelInfo.get("name").toString();
            updateOrAddLodging(acronym, hotelName, website, price, checkIn, checkOut, currency);
        }
    }

    private void updateOrAddLodging(String acronym, String hotelName, String website, double price,
                                    String checkIn, String checkOut, String currency) {
        boolean lodgingExists = false;
        LocalDate userCheckIn = LocalDate.parse(this.userCheckIn, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate userCheckOut = userCheckIn.plusDays(this.days);
        LocalDate jsonCheckIn = LocalDate.parse(checkIn.replaceAll("\"", ""), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate jsonCheckOut = LocalDate.parse(checkOut.replaceAll("\"", ""), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        boolean isInUserRange = !jsonCheckIn.isBefore(userCheckIn) && !jsonCheckOut.isAfter(userCheckOut);
        if (isInUserRange) {
            for (Lodging existingLodging : lodgings) {
                if (existingLodging.getAcronym().equals(acronym)
                        && existingLodging.getHotelName().equals(hotelName)
                        && existingLodging.getWebsite().equals(website)) {
                    existingLodging.setPrice(existingLodging.getPrice() + price);
                    existingLodging.setCheckOut(checkOut);
                    lodgingExists = true;
                    break;
                }
            }
            if (!lodgingExists) {
                Lodging lodging = new Lodging(acronym, checkIn, checkOut, hotelName, website, price, currency);
                lodgings.add(lodging);
            }
        }
    }

    public ArrayList<Ubication> getUbications() {
        return ubications;
    }

    public ArrayList<Lodging> getLodgings() {
        return lodgings;
    }
}