package rodriguezgonzalez.control;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import rodriguezgonzalez.control.exceptions.StoreException;
import rodriguezgonzalez.model.Location;
import rodriguezgonzalez.model.Weather;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class OpenWeatherMapSupplier implements WeatherSupplier {
    private final String apikey;
    private String url;

    public OpenWeatherMapSupplier(String apikey) {
        this.apikey = apikey;
    }

    public void entireUrl(Location location, String apiKey) {
        this.url = "https://api.openweathermap.org/data/2.5/forecast?lat=" + location.getLat() + "&lon=" + location.getLon() + "&appid=" + apiKey + "&units=metric";
    }

    public String getUrl() {
        return url;
    }

    @Override
    public ArrayList<Weather> getWeather(Location location) throws StoreException {
        try {
            entireUrl(location, apikey);
            String url = getUrl();
            Document doc = Jsoup.connect(url).ignoreContentType(true).get();
            String json = doc.body().text();
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = parser.parse(json).getAsJsonObject();
            JsonArray arrayObject = (JsonArray) jsonObject.get("list");
            ArrayList<Weather> weathers = new ArrayList<>();

            for (int i = 0; i < arrayObject.size(); i++) {
                process(location, arrayObject, i, weathers);
            }
            return weathers;
        } catch (IOException e) {
            throw new StoreException(e.getMessage());
        }
    }

    private static void process(Location location, JsonArray arrayObject, int i, ArrayList<Weather> weathers) {
        JsonObject jsonObject1 = (JsonObject) arrayObject.get(i);
        JsonElement dateElement = jsonObject1.get("dt_txt");
        String date = dateElement.getAsString();
        String substring = date.substring(11, 13);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
        Instant predictionTime = dateTime.atZone(ZoneId.systemDefault()).toInstant();

        if (substring.equals("12")) {

            String popString = jsonObject1.get("pop").toString();
            double pop = Double.parseDouble(popString);
            JsonObject windJson = jsonObject1.getAsJsonObject("wind");
            String windString = windJson.get("speed").toString();
            double windSpeed = Double.parseDouble(windString);
            JsonObject mainJson = jsonObject1.getAsJsonObject("main");
            String tempString = mainJson.get("temp").toString();
            double temp = Double.parseDouble(tempString);
            String humidityString = mainJson.get("humidity").toString();
            int humidity = Integer.parseInt(humidityString);
            JsonObject cloudsJson = jsonObject1.getAsJsonObject("clouds");
            String cloudsString = cloudsJson.get("all").toString();
            int clouds = Integer.parseInt(cloudsString);
            JsonArray jsonArray = jsonObject1.getAsJsonArray("weather");
            JsonObject jsonObject = (JsonObject) jsonArray.get(0);
            String weatherCondition = jsonObject.get("main").getAsString();
            Weather weather = new Weather(predictionTime, pop, windSpeed, temp, humidity, clouds, location, weatherCondition);
            weathers.add(weather);
        }
    }
}
