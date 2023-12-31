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
    private String weatherCondition;
    private int nights;
    private SQLiteRecommendationStore store;
    private ArrayList<Ubication> ubications;
    private ArrayList<Lodging> lodgings;
    private Set<String> filteredAcronyms;

    public EventProcessor(String weatherCondition, String nights) {
        this.weatherCondition = weatherCondition;
        this.nights = Integer.parseInt(nights);
        this.ubications = new ArrayList<>();
        this.lodgings = new ArrayList<>();
        this.filteredAcronyms = new HashSet<>();
    }

    public void processWeatherEvent(String json) throws StoreException {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        String weatherConditionString = jsonObject.get("weatherCondition").getAsString();
        if (weatherConditionString.replaceAll("\"", "").equals(weatherCondition)) {
            System.out.println(weatherConditionString);
            JsonObject jsonLocation = (JsonObject) jsonObject.get("location");
            String acronym = jsonLocation.get("place").getAsString();
            double temp = Double.parseDouble(jsonObject.get("temp").getAsString());
            double pop = Double.parseDouble(jsonObject.get("pop").getAsString());
            Ubication ubication = new Ubication(acronym, temp, pop);
            ubications.add(ubication);
            filteredAcronyms.add(acronym);
            System.out.println("Longitud de ubications: " + ubications.size());
            //ubications.add(new Ubication(acronym, temp, pop));
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
            double price = Double.parseDouble(jsonObject.get("rate").toString()) * nights;
            String currency = jsonObject.get("currency").toString();
            String hotelName = hotelInfo.get("name").toString();
            Lodging lodging = new Lodging(acronym, checkIn, checkOut, hotelName, website, price, currency);
            lodgings.add(lodging);
            //lodgings.add(new Lodging(acronym, checkIn, checkOut, hotelName, website, price, currency));
            System.out.println("Longitud de lodgings: " + lodgings.size());
        }
    }

    /*private void extractedUbication(ArrayList<Ubication> ubications) throws StoreException {
        try {
            store.saveUbications(ubications);
        } catch (StoreException e) {
            throw new StoreException(e.getMessage());
        }
    }

     */

    public ArrayList<Ubication> getUbications() {
        return ubications;
    }

    public ArrayList<Lodging> getLodgings() {
        return lodgings;
    }
}