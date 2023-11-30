package rodriguezgonzalez.control;

import rodriguezgonzalez.control.exceptions.StoreException;
import rodriguezgonzalez.model.Weather;

import javax.jms.JMSException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public interface WeatherStore {
    void save(ArrayList<Weather> weathers) throws StoreException;
}

