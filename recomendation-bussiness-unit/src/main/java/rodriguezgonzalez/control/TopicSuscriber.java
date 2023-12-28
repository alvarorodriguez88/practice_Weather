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

    public TopicSuscriber(String dbPath) throws StoreException {
        try {
            this.storer = new SQLiteRecommendationStore(dbPath);
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
            for (String topicName : topicNames) {
                Topic topic = session.createTopic(topicName);
                createConsumer(recommendationBuilder, topic);
            }
            storer.saveUbications(processor.getUbications());
            storer.saveLodgings(processor.getLodgings());
        } catch (JMSException e) {
            throw new StoreException(e.getMessage());
        }
    }

    private void createConsumer(RecommendationFilter recommendationBuilder, Topic topic) throws JMSException {
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
