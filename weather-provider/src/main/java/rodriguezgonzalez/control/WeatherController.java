package rodriguezgonzalez.control;

import rodriguezgonzalez.control.exceptions.StoreException;
import rodriguezgonzalez.model.Location;
import rodriguezgonzalez.model.Weather;

import javax.jms.JMSException;
import java.util.ArrayList;

public class WeatherController {
    private final ArrayList<Location> locations;
    private final JMSWeatherStore jms;
    private final OpenWeatherMapSupplier openWeatherMapSupplier;

    public WeatherController() {
        this.locations = new ArrayList<>() {{
            add(new Location(27.976897166863406, -15.581220101642044, "Gran_Canaria"));
            add(new Location(28.573841603162755, -13.976919911584199, "Fuerteventura"));
            add(new Location(29.006908264835392, -13.614044825597858, "Lanzarote"));
            add(new Location(29.25335827172253, -13.511141076072796, "La_Graciosa"));
            add(new Location(28.25803082700884, -16.598234004431564, "Tenerife"));
            add(new Location(28.115730902110283, -17.222529980788273, "La_Gomera"));
            add(new Location(27.74369055621178, -17.99302465337035, "El_Hierro"));
            add(new Location(28.741658780092063, -17.86465798737256, "La_Palma"));
        }};
        this.jms = new JMSWeatherStore();
        this.openWeatherMapSupplier = new OpenWeatherMapSupplier();
    }

    public void execute(String apiKey) throws StoreException {
        try {
            jms.connect();
            for (Location loc : locations) {
                ArrayList<Weather> weathers = openWeatherMapSupplier.getWeather(loc, apiKey);
                jms.save(weathers);
                System.out.println("Uploaded " + loc.getIsla());
            }
            jms.getConnection().close();
        } catch (JMSException e) {
            throw new StoreException(e.getMessage());
        }
    }
}

