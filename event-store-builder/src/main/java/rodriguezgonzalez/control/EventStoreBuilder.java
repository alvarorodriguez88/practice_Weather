package rodriguezgonzalez.control;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class EventStoreBuilder {

    private String brokerUrl = "tcp://localhost:61616";
    private String topicName = "prediction.Weather";
    private String eventStoreDirectory = "eventstore";

    public EventStoreBuilder() {

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
                System.out.println("Received message: " + text);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        });
    }
}
