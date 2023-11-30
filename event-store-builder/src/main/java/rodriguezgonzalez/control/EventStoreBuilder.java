package rodriguezgonzalez.control;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import java.io.*;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

public class EventStoreBuilder {

    private String brokerUrl = "tcp://localhost:61616";
    private String topicName = "prediction.Weather";
    private StoreEvents storeEvents;

    public EventStoreBuilder() {
        this.storeEvents = new StoreEvents();
    }

    public void subscribeAndStoreEvents() throws JMSException {
        ConnectionFactory connFactory = new ActiveMQConnectionFactory(brokerUrl);
        Connection connection = connFactory.createConnection();
        connection.setClientID("Alvaro");
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic(topicName);

        MessageConsumer consumer = session.createDurableSubscriber(topic, "Alvaro");
        consumer.setMessageListener(message -> {
            try {
                String text = ((TextMessage) message).getText();
                storeEvents.store(text);
                System.out.println("Received message: " + text);
            } catch (JMSException | IOException e) {
                e.printStackTrace();
            }
        });
    }

}
