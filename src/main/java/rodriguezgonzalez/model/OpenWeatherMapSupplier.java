package rodriguezgonzalez.model;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OpenWeatherMapSupplier implements WeatherSupplier{
    private String dataBase;
    private String apiKey;
    private String url;
    private List<Weather> weathers;

    public OpenWeatherMapSupplier(File file) throws IOException {
        this.weathers = new ArrayList<>();
    }

    public void findKey(File file){
        try (BufferedReader buffer = new BufferedReader(new FileReader(file.getPath()))){
            this.apiKey = buffer.readLine();
        } catch (IOException e){
            System.out.println("ERROR: " + e);
        }
    }
    public String findDB(File file){
        try (BufferedReader buffer = new BufferedReader(new FileReader(file.getPath()))){
            this.dataBase = buffer.readLine();
            return dataBase;
        } catch (IOException e){
            System.out.println("ERROR: " + e);
        }
        return null;
    }
    public void entireUrl(Location location){
        this.url = "https://api.openweathermap.org/data/2.5/forecast?lat=" + location.getLat() + "&lon=" + location.getLon() + "&appid=" + apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public ArrayList<Weather> getWeather(Location location) throws IOException {
        entireUrl(location);
        String url = getUrl();
        Document doc = Jsoup.connect(url).ignoreContentType(true).get();
        String json = doc.body().text();

        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(json).getAsJsonObject();
        JsonArray arrayObject = (JsonArray) jsonObject.get("list");
        for (int i = 0; i < arrayObject.size(); i++){

            JsonObject jsonObject1 = (JsonObject) arrayObject.get(i);
            JsonElement dateElement = jsonObject1.get("dt_txt");
            String date = dateElement.getAsString();
            String substring = date.substring(11, 13);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(date, formatter);
            Instant ts = dateTime.atZone(ZoneId.systemDefault()).toInstant();

            if(substring.equals("12")) {

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

                //TODO ponerle a weather una variable de id y cambiar la date a tipo instant
                Weather weather = new Weather(ts, pop, windSpeed, temp, humidity, clouds, location);
                weathers.add(weather);
            }
        }
        return (ArrayList<Weather>) weathers;
    }
}
