package rodriguezgonzalez;

import rodriguezgonzalez.control.BusinessLeaker;
import rodriguezgonzalez.control.EventProcessor;
import rodriguezgonzalez.control.TopicSuscriber;
import rodriguezgonzalez.control.exceptions.StoreException;
import rodriguezgonzalez.view.Connector;

public class Main {

    public static void main(String[] args) throws StoreException {
        Connector connector = new Connector();
        connector.connection();
        EventProcessor processor = new EventProcessor();
        BusinessLeaker leaker = new BusinessLeaker(processor);
        TopicSuscriber subscriber = new TopicSuscriber();
        subscriber.start(leaker);
    }
}
