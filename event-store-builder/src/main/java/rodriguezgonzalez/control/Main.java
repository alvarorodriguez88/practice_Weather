package rodriguezgonzalez.control;

import rodriguezgonzalez.control.exceptions.StoreException;

public class Main {
    public static void main(String[] args) throws StoreException {
        TopicSuscriber suscriber = new TopicSuscriber();
        FileEventBuilder eventBuilder = new FileEventBuilder(args[0]);
        suscriber.start(eventBuilder);
    }
}
