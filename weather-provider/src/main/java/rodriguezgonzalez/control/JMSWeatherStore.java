package rodriguezgonzalez.control;

import com.google.gson.*;
import org.apache.activemq.ActiveMQConnectionFactory;
import rodriguezgonzalez.control.exceptions.StoreException;
import rodriguezgonzalez.model.Weather;

import javax.jms.*;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.ArrayList;

public class JMSWeatherStore implements WeatherStore {
    private final String brokerUrl = "tcp://localhost:61616";
    private final String subject = "prediction.Weather";
    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private Destination destination;
    private MessageProducer producer;

    public JMSWeatherStore() {

    }

    public void connect() {
        try {
            connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
            connection = connectionFactory.createConnection();
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            destination = session.createTopic(subject);

            producer = session.createProducer(destination);
        } catch (JMSException e) {
            System.out.println(e.getMessage());
        }
    }

    public String weatherToJson(Weather weather) {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Instant.class, new InstantAdapter())
                .create();
        return gson.toJson(weather);
    }

    @Override
    public void save(ArrayList<Weather> weathers) throws StoreException {
        try {
            for (Weather weather : weathers) {
                String json = weatherToJson(weather);
                System.out.println(json);
                TextMessage text = session.createTextMessage(json);
                producer.send(text);
            }
            System.out.println("Messages already sent...");
        } catch (JMSException e) {
            throw new StoreException(e.getMessage());
        }
    }

    private static class InstantAdapter implements JsonSerializer<Instant> {

        @Override
        public JsonElement serialize(Instant src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
