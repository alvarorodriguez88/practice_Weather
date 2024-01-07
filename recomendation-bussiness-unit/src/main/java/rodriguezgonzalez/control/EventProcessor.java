package rodriguezgonzalez.control;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import rodriguezgonzalez.control.exceptions.StoreException;
import rodriguezgonzalez.model.Lodging;
import rodriguezgonzalez.model.Ubication;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class EventProcessor {
    private ArrayList<Ubication> ubications;
    private ArrayList<Lodging> lodgings;

    public EventProcessor(String userCheckIn, String days, String weatherCondition) {
        this.ubications = new ArrayList<>();
        this.lodgings = new ArrayList<>();
    }

    public void processWeatherEvent(String json) throws StoreException {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        String weatherConditionString = jsonObject.get("weatherCondition").getAsString();
        JsonObject jsonLocation = (JsonObject) jsonObject.get("location");
        String acronym = jsonLocation.get("place").getAsString();
        double temp = Double.parseDouble(jsonObject.get("temp").getAsString());
        double pop = Double.parseDouble(jsonObject.get("pop").getAsString());
        Ubication ubication = new Ubication(acronym, temp, pop, weatherConditionString.replaceAll("\"", ""));
        ubications.add(ubication);
        System.out.println("Evento de weather guardado.");
    }

    public void processHotelEvent(String json) throws StoreException {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        String checkIn = jsonObject.get("checkIn").toString().replaceAll("\"", "");
        String checkOut = jsonObject.get("checkOut").toString().replaceAll("\"", "");
        String website = jsonObject.get("website").toString().replaceAll("\"", "");
        double price = Double.parseDouble(jsonObject.get("rate").toString());
        String currency = jsonObject.get("currency").toString().replaceAll("\"", "");
        JsonObject hotelInfo = (JsonObject) jsonObject.get("hotelInfo");
        String acronym = hotelInfo.get("acronym").toString().replaceAll("\"", "");
        String hotelName = hotelInfo.get("name").toString().replaceAll("\"", "");
        Lodging lodging = new Lodging(acronym, checkIn, checkOut, hotelName, website, price, currency);
        lodgings.add(lodging);
        System.out.println("Evento de hotel guardado.");
    }

    private void updateOrAddLodging(String acronym, String hotelName, String website, double price,
                                    String checkIn, String checkOut, String currency) {
        boolean lodgingExists = false;
        for (Lodging existingLodging : lodgings) {
            if (existingLodging.getAcronym().equals(acronym)
                    && existingLodging.getHotelName().equals(hotelName)
                    && existingLodging.getWebsite().equals(website)
                    && existingLodging.getCheckIn().equals(checkIn)
                    && existingLodging.getCheckOut().equals(checkOut)) {
                lodgingExists = true;
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