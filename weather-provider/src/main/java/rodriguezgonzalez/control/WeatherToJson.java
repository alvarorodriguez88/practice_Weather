package rodriguezgonzalez.control;

import com.google.gson.Gson;
import rodriguezgonzalez.model.Weather;

import java.util.ArrayList;
import java.util.List;

public class WeatherToJson {
    private List<String> weathers;

    public WeatherToJson() {
        this.weathers = new ArrayList<>();
    }
    public List<String> toJson(ArrayList<Weather> weathersList){
        Gson gson = new Gson();
        for (Weather weather : weathersList){
            String weatherJson = gson.toJson(weather);
            weathers.add(weatherJson);
        }
        return weathers;
    }
}
