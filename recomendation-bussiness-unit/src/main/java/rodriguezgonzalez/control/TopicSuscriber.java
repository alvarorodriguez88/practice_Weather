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
    private EventProcessor processor;

    public TopicSuscriber(EventProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void start(RecommendationBuilder recommendationBuilder, RecommendationStore store) throws StoreException {
        try {
            factory = new ActiveMQConnectionFactory(brokerUrl);
            connection = factory.createConnection();
            connection.setClientID("recommendation-business-unit");
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(topicNames[0]);
            Topic topic1 = session.createTopic(topicNames[1]);
            createWeatherConsumer(recommendationBuilder, topic);
            createHotelConsumer(recommendationBuilder, topic1);
            EventProcessorTimer timer = new EventProcessorTimer(store, processor);
            timer.startProcessingWithTimer();
        } catch (JMSException e) {
            throw new StoreException(e.getMessage());
        }
    }

    private void createWeatherConsumer(RecommendationBuilder recommendationBuilder, Topic topic) throws JMSException {
        MessageConsumer consumer = session.createDurableSubscriber(topic, "recommendation-business-unit_" + topic.getTopicName());
        consumer.setMessageListener(message -> {
            try {
                String text = ((TextMessage) message).getText();
                recommendationBuilder.filter(text, topic.getTopicName());
            } catch (JMSException | StoreException e) {
                System.err.println(e.getMessage());
            }
        });
    }

    private void createHotelConsumer(RecommendationBuilder recommendationBuilder, Topic topic) throws JMSException {
        MessageConsumer consumer = session.createDurableSubscriber(topic, "recommendation-business-unit_" + topic.getTopicName());
        consumer.setMessageListener(message -> {
            try {
                String text = ((TextMessage) message).getText();
                recommendationBuilder.filter(text, topic.getTopicName());
            } catch (JMSException | StoreException e) {
                System.err.println(e.getMessage());
            }
        });
    }
}