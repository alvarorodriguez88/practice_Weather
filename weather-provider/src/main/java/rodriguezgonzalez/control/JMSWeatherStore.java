package rodriguezgonzalez.control;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.activemq.ActiveMQConnectionFactory;
import rodriguezgonzalez.model.Weather;
import java.time.Instant;
import com.google.gson.*;

import javax.jms.*;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class JMSWeatherStore implements WeatherStore {
    private String brokerUrl = "tcp://localhost:61616";
    public JMSWeatherStore() {

    }

    public String weatherToJson(Weather weather) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantAdapter())
                .create();
        return gson.toJson(weather);
    }

    @Override
    public void save(ArrayList<Weather> weathers) throws JMSException {
        ConnectionFactory connFactory = new ActiveMQConnectionFactory(brokerUrl);
        Connection connection = connFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createTopic("prediction.Weather");
        MessageProducer producer = session.createProducer(destination);
        for (Weather weather : weathers){
            String json = weatherToJson(weather);
            TextMessage text = session.createTextMessage(json);
            producer.send(text);
        }
            connection.close();
            System.out.println("Messages already sent...");
    }
    private static class InstantAdapter implements JsonSerializer<Instant> {

        @Override
        public JsonElement serialize(Instant src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }
}
