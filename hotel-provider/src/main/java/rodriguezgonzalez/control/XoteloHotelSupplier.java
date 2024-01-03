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
import java.util.List;

public class XoteloHotelSupplier implements HotelSupplier {
    private final int DAYS_TO_FETCH = 5;

    public XoteloHotelSupplier() {
    }

    @Override
    public ArrayList<Hotel> getHotel(HotelInfo hotelInfo) throws StoreException {
        ArrayList<Hotel> hotels = new ArrayList<>();

        try {
            LocalDate today = LocalDate.now();

            for (LocalDate checkInDate : getNextNDays(today, DAYS_TO_FETCH)) {
                LocalDate checkOutDate = checkInDate.plusDays(1);

                String url = buildUrl(hotelInfo, checkInDate, checkOutDate);
                Document doc = Jsoup.connect(url).ignoreContentType(true).get();
                String json = doc.body().text();
                JsonParser parser = new JsonParser();
                JsonObject jsonObject = parser.parse(json).getAsJsonObject();
                process(hotelInfo, jsonObject, hotels);
            }
        } catch (IOException e) {
            throw new StoreException(e.getMessage());
        }

        return hotels;
    }

    private String buildUrl(HotelInfo hotelInfo, LocalDate checkInDate, LocalDate checkOutDate) {
        return "https://data.xotelo.com/api/rates" +
                "?hotel_key=" + hotelInfo.getHotelKey() +
                "&chk_in=" + checkInDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) +
                "&chk_out=" + checkOutDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private void process(HotelInfo hotelInfo, JsonObject jsonObject, ArrayList<Hotel> hotels) throws StoreException {
        try {
            JsonObject resultObject = jsonObject.get("result").getAsJsonObject();
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
        } catch (NullPointerException e) {
            throw new StoreException(e.getMessage());
        }
    }

    private List<LocalDate> getNextNDays(LocalDate startDate, int days) {
        List<LocalDate> dates = new ArrayList<>();
        for (int i = 0; i < days; i++) {
            dates.add(startDate.plusDays(i));
        }
        return dates;
    }
}