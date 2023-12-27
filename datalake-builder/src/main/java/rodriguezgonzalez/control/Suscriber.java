package rodriguezgonzalez.control;

import rodriguezgonzalez.control.exceptions.StoreException;

public interface Suscriber {
    void start(EventStoreBuilder eventBuilder) throws StoreException;
}
