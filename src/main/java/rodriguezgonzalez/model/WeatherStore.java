package rodriguezgonzalez.model;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public interface WeatherStore {
    void save(Statement statement, ArrayList<Weather> weathers) throws RuntimeException, SQLException;
}

