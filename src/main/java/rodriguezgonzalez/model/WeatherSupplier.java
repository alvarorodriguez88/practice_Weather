package rodriguezgonzalez.model;

import java.io.IOException;
import java.util.ArrayList;

public interface WeatherSupplier {
    ArrayList<Weather> getWeather(Location location, String apiKey) throws IOException;
}
