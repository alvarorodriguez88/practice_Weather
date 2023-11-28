package rodriguezgonzalez.control;

import javax.jms.JMSException;

public class Main {
    public static void main(String[] args) throws JMSException {
        EventStoreBuilder builder = new EventStoreBuilder();
        builder.subscribeAndStoreEvents();
    }
}
