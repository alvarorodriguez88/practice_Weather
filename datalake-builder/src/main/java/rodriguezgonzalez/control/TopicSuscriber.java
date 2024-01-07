package rodriguezgonzalez.control;

import org.apache.activemq.ActiveMQConnectionFactory;
import rodriguezgonzalez.control.exceptions.StoreException;

import javax.jms.*;

public class TopicSuscriber implements Suscriber {

    private String brokerUrl = "tcp://localhost:61616";
    private String[] topicNames = {"prediction.Weather", "information.Hotel"};
    private Connection connection;
    private ConnectionFactory factory;
    private Session session;

    public TopicSuscriber() {
    }

    @Override
    public void start(EventStoreBuilder eventBuilder) throws StoreException {
        try {
            factory = new ActiveMQConnectionFactory(brokerUrl);
            connection = factory.createConnection();
            connection.setClientID("datalake-builder");
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            for (String topicName : topicNames) {
                Topic topic = session.createTopic(topicName);
                createConsumer(eventBuilder, topic);
            }
        } catch (JMSException e) {
            throw new StoreException(e.getMessage());
        }
    }

    private void createConsumer(EventStoreBuilder storeBuilder, Topic topic) throws JMSException {
        MessageConsumer consumer = session.createDurableSubscriber(topic, "datalake-builder_" + topic.getTopicName());
        consumer.setMessageListener(message -> {
            try {
                String text = ((TextMessage) message).getText();
                storeBuilder.save(text, topic.getTopicName());
            } catch (JMSException e) {
                System.err.println(e.getMessage());
            }
        });
    }

}
