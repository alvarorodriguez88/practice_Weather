package rodriguezgonzalez.control;

import rodriguezgonzalez.control.exceptions.StoreException;

public class Main {
    public static void main(String[] args) throws StoreException {
        EventProcessor processor = new EventProcessor(args[0], args[1], args[2]);
        BusinessLeaker leaker = new BusinessLeaker(processor);
        TopicSuscriber suscriber = new TopicSuscriber();
        suscriber.start(leaker, processor);
    }
}
