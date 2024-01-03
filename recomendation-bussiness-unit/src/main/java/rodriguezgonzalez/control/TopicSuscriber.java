package rodriguezgonzalez.control;

import org.apache.activemq.ActiveMQConnectionFactory;
import rodriguezgonzalez.control.exceptions.StoreException;

import javax.jms.*;
import java.sql.SQLException;

public class TopicSuscriber implements Suscriber {

    private String brokerUrl = "tcp://localhost:61616";
    private String[] topicNames = {"prediction.Weather", "information.Hotel"};
    private Connection connection;
    private ConnectionFactory factory;
    private Session session;
    private SQLiteRecommendationStore storer;
    private DatalakeFileHandler handler;

    public TopicSuscriber() throws StoreException {
        try {
            this.storer = new SQLiteRecommendationStore();
            this.handler = new DatalakeFileHandler();
        } catch (SQLException e) {
            throw new StoreException(e.getMessage());
        }
    }

    @Override
    public void start(RecommendationFilter recommendationBuilder, EventProcessor processor) throws StoreException {
        try {
            factory = new ActiveMQConnectionFactory(brokerUrl);
            connection = factory.createConnection();
            connection.setClientID("recommendation-business-unit");
            connection.start();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Topic topic = session.createTopic(topicNames[0]);
            Topic topic1 = session.createTopic(topicNames[1]);
            createWeatherConsumer(recommendationBuilder, topic, processor);
            createHotelConsumer(recommendationBuilder, topic1, processor);
        } catch (JMSException e) {
            throw new StoreException(e.getMessage());
        }
    }

    private void createWeatherConsumer(RecommendationFilter recommendationBuilder, Topic topic, EventProcessor processor) throws JMSException {
        MessageConsumer consumer = session.createDurableSubscriber(topic, "recommendation-business-unit_" + topic.getTopicName());
        consumer.setMessageListener(message -> {
            try {
                String text = ((TextMessage) message).getText();
                if (text != null) {
                    recommendationBuilder.filter(text, topic.getTopicName());
                    //storer.saveUbications(processor.getUbications());
                } else {
                    handler.findLastWeatherFile();
                }
            } catch (JMSException | StoreException e) {
                System.err.println(e.getMessage());
            }
        });
    }

    private void createHotelConsumer(RecommendationFilter recommendationBuilder, Topic topic, EventProcessor processor) throws JMSException {
        MessageConsumer consumer = session.createDurableSubscriber(topic, "recommendation-business-unit_" + topic.getTopicName());
        consumer.setMessageListener(message -> {
            try {
                String text = ((TextMessage) message).getText();
                if (text != null) {
                    recommendationBuilder.filter(text, topic.getTopicName());
                    //storer.saveLodgings(processor.getLodgings());
                } else {
                    handler.findLastHotelFile();
                }
            } catch (JMSException | StoreException e) {
                System.err.println(e.getMessage());
            }
        });
    }

}
