package rodriguezgonzalez.control;

import rodriguezgonzalez.control.exceptions.StoreException;

public interface EventStoreBuilder {
    void save(String json, String topicName);
}
