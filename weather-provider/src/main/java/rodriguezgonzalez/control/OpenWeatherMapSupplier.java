package rodriguezgonzalez.control;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import rodriguezgonzalez.model.Location;
import rodriguezgonzalez.model.Weather;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class OpenWeatherMapSupplier implements WeatherSupplier {
    private String url;

    public OpenWeatherMapSupplier() {
    }

    public void entireUrl(Location location, String apiKey){
        this.url = "https://api.openweathermap.org/data/2.5/forecast?lat=" + location.getLat() + "&lon=" + location.getLon() + "&appid=" + apiKey;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public ArrayList<Weather> getWeather(Location location, String apiKey) throws IOException {
        entireUrl(location, apiKey);
        String url = getUrl();
        Document doc = Jsoup.connect(url).ignoreContentType(true).get();
        String json = doc.body().text();

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(json).getAsJsonObject();
        JsonArray arrayObject = (JsonArray) jsonObject.get("list");
        ArrayList<Weather> weathers = new ArrayList<>();

        for (int i = 0; i < arrayObject.size(); i++) {

            JsonObject jsonObject1 = (JsonObject) arrayObject.get(i);
            JsonElement dateElement = jsonObject1.get("dt_txt");
            String date = dateElement.getAsString();
            String substring = date.substring(11, 13);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
            Instant predictionTime = dateTime.atZone(ZoneId.systemDefault()).toInstant();
            Instant ts = Instant.now();

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

                Weather weather = new Weather(ts, predictionTime, pop, windSpeed, temp, humidity, clouds, location);
                weathers.add(weather);
            }
        }
        return weathers;
    }
}
