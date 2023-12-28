package rodriguezgonzalez.control;

import rodriguezgonzalez.control.exceptions.StoreException;

public class Main {
    public static void main(String[] args) throws StoreException {
        BusinessLeaker leaker = new BusinessLeaker(args[0], args[1]);
        TopicSuscriber suscriber = new TopicSuscriber();
        System.out.println("Estoy en el main");
        suscriber.start(leaker);
    }
}
