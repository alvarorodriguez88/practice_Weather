package rodriguezgonzalez.control;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.activemq.ActiveMQConnectionFactory;
import rodriguezgonzalez.control.exceptions.StoreException;
import rodriguezgonzalez.model.Hotel;

import javax.jms.*;
import java.util.ArrayList;

public class JMSHotelStore implements HotelStore{
    private final String brokerUrl = "tcp://localhost:61616";
    private final String subject = "information.Hotel";
    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private Destination destination;
    private MessageProducer producer;

    public JMSHotelStore() {
    }

    public void connect() throws StoreException {
        try {
            connectionFactory = new ActiveMQConnectionFactory(brokerUrl);
            connection = connectionFactory.createConnection();
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            destination = session.createTopic(subject);

            producer = session.createProducer(destination);
        } catch (JMSException e) {
            throw new StoreException(e.getMessage());
        }
    }

    public Connection getConnection() {
        return connection;
    }
    @Override
    public void save(ArrayList<Hotel> hotels) throws StoreException {
        try {
            for (Hotel hotel : hotels){
                String json = hotelToJson(hotel);
                TextMessage text = session.createTextMessage(json);
                producer.send(text);
            }
        } catch (JMSException e){
            throw new StoreException(e.getMessage());
        }
    }

    private String hotelToJson(Hotel hotel) {
        Gson gson = new Gson();
        return gson.toJson(hotel);
    }
}
