package rodriguezgonzalez.control;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import rodriguezgonzalez.control.exceptions.StoreException;
import rodriguezgonzalez.model.Hotel;
import rodriguezgonzalez.model.HotelInfo;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

public class XoteloHotelSupplier implements HotelSupplier {
    private String url;
    private String checkIn;
    private String checkOut;
    private HotelController reader;

    public XoteloHotelSupplier() {
        LocalDate today = LocalDate.now();
        this.checkIn = today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate fiveDaysLater = today.plusDays(5);
        this.checkOut = fiveDaysLater.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    public String entireUrl(HotelInfo hotelInfo) {
        return url = "https://data.xotelo.com/api/rates" +
                "? hotel_key = " + hotelInfo.getHotelKey() +
                "& chk_in = " + checkIn +
                "& chk_out = " + checkOut;
    }
    @Override
    public ArrayList<Hotel> getHotel(HotelInfo hotelInfo) throws StoreException {
        try {
            String url = entireUrl(hotelInfo);
            Document doc = Jsoup.connect(url).ignoreContentType(true).get();
            String json = doc.body().text();
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(json).getAsJsonObject();
            ArrayList<Hotel> hotels = new ArrayList<>();
            for (int i = 0; i < jsonObject.size(); i++) {
                process(hotelInfo, jsonObject, hotels);
            }
            return hotels;
        } catch (IOException e){
            throw new StoreException(e.getMessage());
        }
    }

    private void process(HotelInfo hotelInfo, JsonObject jsonObject, ArrayList<Hotel> hotels) {
        JsonObject resultObject = jsonObject.getAsJsonObject("result");
        String currency = resultObject.get("currency").getAsString();
        String checkIn = resultObject.get("chk_in").getAsString();
        String checkOut = resultObject.get("chk_out").getAsString();
        JsonArray ratesArray = resultObject.getAsJsonArray("rates");
        for (JsonElement element : ratesArray) {
            JsonObject rateObject = element.getAsJsonObject();
            String website = rateObject.get("name").getAsString();
            int rate = rateObject.get("rate").getAsInt();
            Hotel hotel = new Hotel(website, currency, checkIn, checkOut, rate, hotelInfo);
            hotels.add(hotel);
        }
    }
}
