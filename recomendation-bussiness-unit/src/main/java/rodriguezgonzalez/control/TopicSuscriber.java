package rodriguezgonzalez.control;

import org.apache.activemq.ActiveMQConnectionFactory;
import rodriguezgonzalez.control.exceptions.StoreException;

import javax.jms.*;
import java.time.Duration;
import java.time.Instant;

public class TopicSuscriber implements Suscriber {

    private final String brokerUrl = "tcp://localhost:61616";
    private final String[] topicNames = {"prediction.Weather", "information.Hotel"};
    private Connection connection;
    private ConnectionFactory factory;
    private Session session;

    public TopicSuscriber() {

    }

    @Override
    public void start(RecommendationBuilder recommendationBuilder) throws StoreException {
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
        } catch (JMSException e) {
            throw new StoreException(e.getMessage());
        }
    }

    private void createConsumer(RecommendationBuilder recommendationBuilder, Topic topic) throws JMSException {
        MessageConsumer consumer = session.createDurableSubscriber(topic, "recommendation-business-unit_" + topic.getTopicName());
        var ref = new Object() {
            Instant latestUpdate = Instant.now();
        };
        consumer.setMessageListener(message -> {
            try {
                Instant now = Instant.now();
                long diffInMinutes = Duration.between(ref.latestUpdate, now).toMinutes();
                if (diffInMinutes > 60 ) {
                    ref.latestUpdate = now;
                    RecommendationStorer storer = new RecommendationStorer();
                    storer.saveRecommendations();
                    System.out.println("DataBase Reinitialized");
                    String text = ((TextMessage) message).getText();
                    recommendationBuilder.filter(text, topic.getTopicName());
                } else {
                    String text = ((TextMessage) message).getText();
                    recommendationBuilder.filter(text, topic.getTopicName());
                }
            } catch (JMSException | StoreException e) {
                System.err.println(e.getMessage());
            }
        });
    }
}