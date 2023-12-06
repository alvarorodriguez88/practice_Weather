package rodriguezgonzalez.control;

import org.apache.activemq.ActiveMQConnectionFactory;
import rodriguezgonzalez.control.exceptions.StoreException;

import javax.jms.*;
import java.io.File;

public class MapSuscriber implements Suscriber {

    private String brokerUrl = "tcp://localhost:61616";
    private String topicName = "prediction.Weather";
    private Connection connection;
    private ConnectionFactory factory;
    private Session session;

    public MapSuscriber() {
    }

    public void start(FileEventBuilder eventBuilder) throws StoreException {
        try {
            factory = new ActiveMQConnectionFactory(brokerUrl);
            connection = factory.createConnection();
            connection.setClientID("weather-provider");
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(topicName);

            MessageConsumer consumer = session.createDurableSubscriber(topic, "weather-provider_" + topicName);
            consumer.setMessageListener(message -> {
                try {
                    String text = ((TextMessage) message).getText();
                    eventBuilder.save(text);
                } catch (JMSException | StoreException e) {
                    e.printStackTrace();
                }
            });
        } catch (JMSException e) {
            throw new StoreException(e.getMessage());
        }
    }

}
