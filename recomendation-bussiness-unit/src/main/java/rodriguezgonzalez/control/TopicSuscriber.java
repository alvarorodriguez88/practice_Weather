package rodriguezgonzalez.control;

import org.apache.activemq.ActiveMQConnectionFactory;
import rodriguezgonzalez.control.exceptions.StoreException;
import rodriguezgonzalez.model.Lodging;
import rodriguezgonzalez.model.Ubication;

import javax.jms.*;
import java.sql.SQLException;

public class TopicSuscriber implements Suscriber {

    private String brokerUrl = "tcp://localhost:61616";
    private String[] topicNames = {"prediction.Weather", "information.Hotel"};
    private Connection connection;
    private ConnectionFactory factory;
    private Session session;
    private DatalakeFileHandler handler;

    public TopicSuscriber() {
        this.handler = new DatalakeFileHandler();
    }

    @Override
    public void start(RecommendationBuilder recommendationBuilder, EventProcessor processor) throws StoreException {
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
            EventProcessorTimer timer = new EventProcessorTimer(recommendationBuilder, processor);
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
                if (text != null) {
                    recommendationBuilder.filter(text, topic.getTopicName());
                } else {
                    handler.findLastWeatherFile();
                }
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
                if (text != null) {
                    recommendationBuilder.filter(text, topic.getTopicName());
                } else {
                    handler.findLastHotelFile();
                }
            } catch (JMSException | StoreException e) {
                System.err.println(e.getMessage());
            }
        });
    }
}