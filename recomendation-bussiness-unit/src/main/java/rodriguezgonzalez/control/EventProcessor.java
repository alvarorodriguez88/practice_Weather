package rodriguezgonzalez.control;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import rodriguezgonzalez.control.exceptions.StoreException;
import rodriguezgonzalez.model.Lodging;
import rodriguezgonzalez.model.Ubication;

public class EventProcessor {
    private final RecommendationStorer storer;

    public EventProcessor() throws StoreException {
        this.storer = new RecommendationStorer();
        storer.saveRecommendations();
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
        System.out.println("Event processed");
        storer.saveUbicationRecommendation(ubication);
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
        System.out.println("Event processed");
        storer.saveLodgingRecommendation(lodging);
    }
}