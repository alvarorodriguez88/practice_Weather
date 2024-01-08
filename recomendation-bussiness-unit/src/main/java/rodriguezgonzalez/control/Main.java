package rodriguezgonzalez.control;

import rodriguezgonzalez.control.exceptions.StoreException;

public class Main {
    public static void main(String[] args) throws StoreException {
        EventProcessor processor = new EventProcessor();
        BusinessLeaker leaker = new BusinessLeaker(processor);
        TopicSuscriber subscriber = new TopicSuscriber();
        subscriber.start(leaker);
        rodriguezgonzalez.view.Main.main(args);
    }
}
