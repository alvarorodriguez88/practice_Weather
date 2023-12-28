package rodriguezgonzalez.control;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import rodriguezgonzalez.control.exceptions.StoreException;
import rodriguezgonzalez.model.Lodging;
import rodriguezgonzalez.model.Ubication;

import java.util.ArrayList;

public class EventProcessor {
    private String weatherCondition;
    private int nights;
    private SQLiteRecommendationStore store;
    private ArrayList<Ubication> ubications;
    private ArrayList<Lodging> lodgings;

    public EventProcessor(String weatherCondition, String nights) {
        this.weatherCondition = weatherCondition;
        this.nights = Integer.parseInt(nights);
    }

    public void processWeatherEvent(String json) throws StoreException {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        String weatherConditionString = jsonObject.get("weatherCondition").toString();
        if (weatherConditionString.equals(weatherCondition.replaceAll("\"", ""))) {
            String acronym = jsonObject.get("place").toString();
            double temp = Double.parseDouble(jsonObject.get("temp").toString());
            double pop = Double.parseDouble(jsonObject.get("pop").toString());
            ubications.add(new Ubication(acronym, temp, pop));
        }
    }
    public void processHotelEvent(String json) throws StoreException {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        String checkIn = jsonObject.get("checkIn").toString();
        String checkOut = jsonObject.get("checkOut").toString();
        String website = jsonObject.get("website").toString();
        int price = Integer.parseInt(jsonObject.get("rate").toString()) * nights;
        String currency = jsonObject.get("currency").toString();
        JsonObject hotelInfo = (JsonObject) jsonObject.get("hotelInfo");
        String acronym = hotelInfo.get("acronym").toString();
        String hotelName = hotelInfo.get("name").toString();
        lodgings.add(new Lodging(acronym, checkIn, checkOut, hotelName, website, price, currency));
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
