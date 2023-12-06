package rodriguezgonzalez.control;

import rodriguezgonzalez.control.exceptions.StoreException;

public class Main {
    public static void main(String[] args) throws StoreException {
        MapSuscriber suscriber = new MapSuscriber();
        FileEventBuilder eventBuilder = new FileEventBuilder(args[0]);
        suscriber.start(eventBuilder);
    }
}
