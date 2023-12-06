package rodriguezgonzalez.control;

import rodriguezgonzalez.control.exceptions.StoreException;
import rodriguezgonzalez.model.Location;
import rodriguezgonzalez.model.Weather;

import java.util.ArrayList;

public interface WeatherSupplier {
    ArrayList<Weather> getWeather(Location location, String apiKey) throws StoreException;
}
