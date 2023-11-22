package rodriguezgonzalez.control;

import rodriguezgonzalez.model.Weather;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public interface WeatherStore {
    void save(ArrayList<Weather> weathers) throws SQLException;
}

